package hu.szinhaz.ticket;

import hu.szinhaz.ticket.dto.BookingRequestDto;
import hu.szinhaz.ticket.dto.BookingResponseDto;
import hu.szinhaz.ticket.model.*;
import hu.szinhaz.ticket.repository.BookingRepository;
import hu.szinhaz.ticket.repository.PerformanceSeatRepository;
import hu.szinhaz.ticket.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PerformanceSeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @Transactional
    @DisplayName("FR-04: Sikeres jegyvásárlás székek zárolásával és jegykiállítással")
    void testSuccessfulBooking() {
        // Find 2 free seats
        List<PerformanceSeat> freeSeats = seatRepository.findAll().stream()
                .filter(s -> s.getStatus() == SeatStatus.FREE)
                .limit(2)
                .toList();

        assertFalse(freeSeats.isEmpty(), "Kell lennie szabad székeknek az inicializált adatbázisban");
        List<Long> seatIds = freeSeats.stream().map(PerformanceSeat::getId).toList();

        BookingRequestDto req = new BookingRequestDto();
        req.setPerformanceId(freeSeats.get(0).getPerformance().getId());
        req.setSeatIds(seatIds);
        req.setCustomerName("Teszt Elek");
        req.setCustomerEmail("teszt.elek@example.com");
        req.setCustomerPhone("+36309876543");

        BookingResponseDto response = bookingService.createBooking(req);

        assertNotNull(response);
        assertNotNull(response.getBookingCode());
        assertEquals("Teszt Elek", response.getCustomerName());
        assertEquals(2, response.getTicketCodes().size());

        // Verify seats are now SOLD
        for (Long sid : seatIds) {
            PerformanceSeat seat = seatRepository.findById(sid).orElseThrow();
            assertEquals(SeatStatus.SOLD, seat.getStatus());
        }
    }

    @Test
    @Transactional
    @DisplayName("ADR-002: Dupla foglalás megakadályozása - már eladott szék nem foglalható újra")
    void testDoubleBookingPrevention() {
        // Find 1 free seat
        PerformanceSeat freeSeat = seatRepository.findAll().stream()
                .filter(s -> s.getStatus() == SeatStatus.FREE)
                .findFirst()
                .orElseThrow();

        Long seatId = freeSeat.getId();

        // 1st booking
        BookingRequestDto req1 = new BookingRequestDto();
        req1.setPerformanceId(freeSeat.getPerformance().getId());
        req1.setSeatIds(List.of(seatId));
        req1.setCustomerName("Első Vásárló");
        req1.setCustomerEmail("elso@example.com");
        req1.setCustomerPhone("+36301111111");

        bookingService.createBooking(req1);

        // 2nd booking attempt for the same seat
        BookingRequestDto req2 = new BookingRequestDto();
        req2.setPerformanceId(freeSeat.getPerformance().getId());
        req2.setSeatIds(List.of(seatId));
        req2.setCustomerName("Második Vásárló");
        req2.setCustomerEmail("masodik@example.com");
        req2.setCustomerPhone("+36302222222");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(req2);
        });

        assertTrue(ex.getMessage().contains("már lefoglalták vagy megvásárolták"));
    }

    @Test
    @Transactional
    @DisplayName("FR-06: Jegy lemondása/visszaváltása és a székek újbóli felszabadítása")
    void testCancelBooking() {
        // Book a seat first
        PerformanceSeat freeSeat = seatRepository.findAll().stream()
                .filter(s -> s.getStatus() == SeatStatus.FREE)
                .findFirst()
                .orElseThrow();

        Long seatId = freeSeat.getId();

        BookingRequestDto req = new BookingRequestDto();
        req.setPerformanceId(freeSeat.getPerformance().getId());
        req.setSeatIds(List.of(seatId));
        req.setCustomerName("Lemondó Ügyfél");
        req.setCustomerEmail("lemondo@example.com");
        req.setCustomerPhone("+36303333333");

        BookingResponseDto response = bookingService.createBooking(req);
        String bookingCode = response.getBookingCode();

        // Cancel
        boolean cancelled = bookingService.cancelBooking(bookingCode, "lemondo@example.com");
        assertTrue(cancelled);

        // Verify status
        Booking booking = bookingRepository.findByBookingCode(bookingCode).orElseThrow();
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());

        // Verify seat is free again
        PerformanceSeat seatAfterCancel = seatRepository.findById(seatId).orElseThrow();
        assertEquals(SeatStatus.FREE, seatAfterCancel.getStatus());
    }
}
