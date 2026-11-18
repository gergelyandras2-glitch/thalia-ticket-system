package hu.szinhaz.ticket.controller;

import hu.szinhaz.ticket.dto.BookingRequestDto;
import hu.szinhaz.ticket.dto.BookingResponseDto;
import hu.szinhaz.ticket.dto.SeatDto;
import hu.szinhaz.ticket.model.Booking;
import hu.szinhaz.ticket.model.Performance;
import hu.szinhaz.ticket.model.PerformanceSeat;
import hu.szinhaz.ticket.repository.PerformanceSeatRepository;
import hu.szinhaz.ticket.service.BookingService;
import hu.szinhaz.ticket.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PerformanceService performanceService;
    private final PerformanceSeatRepository seatRepository;

    public BookingController(BookingService bookingService,
                             PerformanceService performanceService,
                             PerformanceSeatRepository seatRepository) {
        this.bookingService = bookingService;
        this.performanceService = performanceService;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/select-seats/{performanceId}")
    public String showSeatSelection(@PathVariable("performanceId") Long performanceId, Model model) {
        Performance performance = performanceService.findById(performanceId);
        List<SeatDto> seats = bookingService.getSeatsForPerformance(performanceId);

        model.addAttribute("performance", performance);
        model.addAttribute("seats", seats);
        return "seat-selection";
    }

    @PostMapping("/checkout-prep")
    public String prepareCheckout(@RequestParam("performanceId") Long performanceId,
                                  @RequestParam("selectedSeatIds") List<Long> seatIds,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (seatIds == null || seatIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kérjük, jelöljön ki legalább egy ülőhelyet a vásárláshoz!");
            return "redirect:/bookings/select-seats/" + performanceId;
        }

        Performance performance = performanceService.findById(performanceId);
        List<PerformanceSeat> selectedSeats = seatRepository.findAllById(seatIds);

        BigDecimal total = BigDecimal.ZERO;
        for (PerformanceSeat seat : selectedSeats) {
            total = total.add(seat.calculatePrice());
        }

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setPerformanceId(performanceId);
        bookingRequest.setSeatIds(seatIds);

        model.addAttribute("performance", performance);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("totalAmount", total);
        model.addAttribute("bookingRequest", bookingRequest);
        return "checkout";
    }

    @PostMapping("/confirm-order")
    public String confirmOrder(@Valid @ModelAttribute("bookingRequest") BookingRequestDto bookingRequest,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Performance performance = performanceService.findById(bookingRequest.getPerformanceId());
            List<PerformanceSeat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds());
            BigDecimal total = BigDecimal.ZERO;
            for (PerformanceSeat seat : selectedSeats) {
                total = total.add(seat.calculatePrice());
            }
            model.addAttribute("performance", performance);
            model.addAttribute("selectedSeats", selectedSeats);
            model.addAttribute("totalAmount", total);
            return "checkout";
        }

        try {
            BookingResponseDto response = bookingService.createBooking(bookingRequest);
            redirectAttributes.addFlashAttribute("bookingResponse", response);
            return "redirect:/bookings/success?code=" + response.getBookingCode();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bookings/select-seats/" + bookingRequest.getPerformanceId();
        }
    }

    @GetMapping("/success")
    public String showSuccess(@RequestParam("code") String code, Model model) {
        Booking booking = bookingService.getBookingByCode(code);
        model.addAttribute("booking", booking);
        return "confirmation";
    }

    @PostMapping("/cancel")
    public String cancelBooking(@RequestParam("bookingCode") String bookingCode,
                                @RequestParam("customerEmail") String customerEmail,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(bookingCode, customerEmail);
            redirectAttributes.addFlashAttribute("successMessage",
                    "A(z) " + bookingCode + " számú foglalás sikeresen törölve, a helyek újra felszabadultak!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/tickets/lookup";
    }
}
