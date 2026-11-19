package hu.szinhaz.ticket.controller;

import hu.szinhaz.ticket.model.Booking;
import hu.szinhaz.ticket.model.Ticket;
import hu.szinhaz.ticket.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final BookingService bookingService;

    public TicketController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{ticketCode}")
    public String viewTicket(@PathVariable("ticketCode") String ticketCode, Model model) {
        Ticket ticket = bookingService.getTicketByCode(ticketCode);
        model.addAttribute("ticket", ticket);
        model.addAttribute("booking", ticket.getBooking());
        model.addAttribute("seat", ticket.getPerformanceSeat());
        model.addAttribute("performance", ticket.getPerformanceSeat().getPerformance());
        return "ticket-view";
    }

    @GetMapping("/lookup")
    public String showLookupForm(@RequestParam(value = "code", required = false) String code,
                                 @RequestParam(value = "email", required = false) String email,
                                 Model model) {
        if (code != null && !code.trim().isEmpty()) {
            try {
                Booking booking = bookingService.getBookingByCode(code.trim());
                if (email != null && !email.trim().isEmpty() &&
                        !booking.getCustomerEmail().equalsIgnoreCase(email.trim())) {
                    model.addAttribute("errorMessage", "A megadott e-mail cím nem egyezik a foglaláshoz tartozóval!");
                } else {
                    model.addAttribute("foundBooking", booking);
                }
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Nem található foglalás ezzel az azonosítóval: " + code);
            }
        }
        model.addAttribute("searchedCode", code != null ? code : "");
        model.addAttribute("searchedEmail", email != null ? email : "");
        return "ticket-lookup";
    }
}
