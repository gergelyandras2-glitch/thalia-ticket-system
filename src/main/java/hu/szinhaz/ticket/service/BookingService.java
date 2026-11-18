package hu.szinhaz.ticket.service;

import hu.szinhaz.ticket.dto.BookingRequestDto;
import hu.szinhaz.ticket.dto.BookingResponseDto;
import hu.szinhaz.ticket.dto.DashboardStatsDto;
import hu.szinhaz.ticket.dto.SeatDto;
import hu.szinhaz.ticket.model.*;
import hu.szinhaz.ticket.repository.BookingRepository;
import hu.szinhaz.ticket.repository.PerformanceRepository;
import hu.szinhaz.ticket.repository.PerformanceSeatRepository;
import hu.szinhaz.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PerformanceSeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final PerformanceRepository performanceRepository;

    public BookingService(BookingRepository bookingRepository,
                          PerformanceSeatRepository seatRepository,
                          TicketRepository ticketRepository,
                          PerformanceRepository performanceRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.performanceRepository = performanceRepository;
    }

    public List<SeatDto> getSeatsForPerformance(Long performanceId) {
        return seatRepository.findByPerformanceIdOrderByRowNumberAscSeatNumberAsc(performanceId)
                .stream()
                .map(SeatDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Atomically creates a booking using Pessimistic Write Lock on the selected seats
     * to eliminate double-booking / race conditions under heavy load (ADR-002).
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto createBooking(BookingRequestDto req) {
        if (req.getSeatIds() == null || req.getSeatIds().isEmpty()) {
            throw new IllegalArgumentException("Legalább egy ülőhely kiválasztása kötelező a vásárláshoz!");
        }

        // PESSIMISTIC LOCK: Lock seat rows in DB to prevent concurrent updates
        List<PerformanceSeat> seats = seatRepository.findByIdInWithLock(req.getSeatIds());

        if (seats.size() != req.getSeatIds().size()) {
            throw new IllegalArgumentException("Egy vagy több kiválasztott ülőhely nem található az adatbázisban!");
        }

        // Validate that all seats are FREE
        for (PerformanceSeat seat : seats) {
            if (seat.getStatus() != SeatStatus.FREE) {
                throw new IllegalStateException("A(z) " + seat.getSeatLabel() +
                        " helyet időközben már lefoglalták vagy megvásárolták! Kérjük, válasszon másik helyet.");
            }
        }

        Performance performance = seats.get(0).getPerformance();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PerformanceSeat seat : seats) {
            totalAmount = totalAmount.add(seat.calculatePrice());
        }

        // Generate unique booking code: THX-YYYY-XXXX
        String bookingCode = "THX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Booking booking = new Booking();
        booking.setBookingCode(bookingCode);
        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerEmail(req.getCustomerEmail().trim().toLowerCase());
        booking.setCustomerPhone(req.getCustomerPhone());
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        List<Ticket> tickets = new ArrayList<>();
        List<String> ticketCodes = new ArrayList<>();
        List<String> seatLabels = new ArrayList<>();

        for (PerformanceSeat seat : seats) {
            seat.setStatus(SeatStatus.SOLD);
            seatRepository.save(seat);

            String ticketCode = "TKT-" + LocalDateTime.now().getYear() + "-" +
                    UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            Ticket ticket = new Ticket();
            ticket.setTicketCode(ticketCode);
            ticket.setPerformanceSeat(seat);
            ticket.setBooking(savedBooking);
            ticket.setPrice(seat.calculatePrice());
            ticket.setIssueTime(LocalDateTime.now());

            tickets.add(ticket);
            ticketCodes.add(ticketCode);
            seatLabels.add(seat.getSeatLabel());
        }

        ticketRepository.saveAll(tickets);
        savedBooking.setTickets(tickets);

        BookingResponseDto response = new BookingResponseDto();
        response.setBookingCode(bookingCode);
        response.setCustomerName(savedBooking.getCustomerName());
        response.setCustomerEmail(savedBooking.getCustomerEmail());
        response.setTotalAmount(savedBooking.getTotalAmount());
        response.setPerformanceTitle(performance.getTitle());
        response.setPerformanceDate(performance.getFormattedDate());
        response.setHallName(performance.getHall().getName());
        response.setSeatLabels(seatLabels);
        response.setTicketCodes(ticketCodes);

        return response;
    }

    /**
     * Cancels an existing booking and frees up all corresponding seats (FR-06).
     */
    @Transactional
    public boolean cancelBooking(String bookingCode, String customerEmail) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Nem található foglalás a megadott kóddal: " + bookingCode));

        if (!booking.getCustomerEmail().equalsIgnoreCase(customerEmail.trim())) {
            throw new IllegalArgumentException("A megadott e-mail cím nem egyezik a foglalásban szereplővel!");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Ez a foglalás már korábban lemondásra került!");
        }

        // Release seats back to FREE
        for (Ticket ticket : booking.getTickets()) {
            PerformanceSeat seat = ticket.getPerformanceSeat();
            seat.setStatus(SeatStatus.FREE);
            seatRepository.save(seat);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return true;
    }

    public Booking getBookingByCode(String code) {
        return bookingRepository.findByBookingCode(code.trim())
                .orElseThrow(() -> new IllegalArgumentException("A keresett foglalás nem található: " + code));
    }

    public Ticket getTicketByCode(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("A keresett jegy nem található: " + ticketCode));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingTimeDesc();
    }

    public DashboardStatsDto getDashboardStats() {
        BigDecimal totalRevenue = bookingRepository.sumTotalRevenueByStatus(BookingStatus.CONFIRMED);
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        long totalBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        long totalTickets = ticketRepository.count();

        // Calculate seats occupancy
        long allSeatsCount = seatRepository.count();
        long soldSeatsCount = seatRepository.countByPerformanceIdAndStatus(null, SeatStatus.SOLD); // count across all performances
        // In case custom query:
        List<PerformanceSeat> allSeats = seatRepository.findAll();
        long totalSeats = allSeats.size();
        long soldSeats = allSeats.stream().filter(s -> s.getStatus() == SeatStatus.SOLD).count();
        long freeSeats = allSeats.stream().filter(s -> s.getStatus() == SeatStatus.FREE).count();

        double occupancyRate = (totalSeats > 0) ? ((double) soldSeats / totalSeats) * 100.0 : 0.0;
        occupancyRate = BigDecimal.valueOf(occupancyRate).setScale(1, RoundingMode.HALF_UP).doubleValue();

        return new DashboardStatsDto(
                totalRevenue,
                totalBookings,
                soldSeats,
                freeSeats,
                occupancyRate
        );
    }
}
