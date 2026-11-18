package hu.szinhaz.ticket.controller;

import hu.szinhaz.ticket.dto.DashboardStatsDto;
import hu.szinhaz.ticket.dto.PerformanceCreateDto;
import hu.szinhaz.ticket.model.Booking;
import hu.szinhaz.ticket.model.TheaterHall;
import hu.szinhaz.ticket.repository.TheaterHallRepository;
import hu.szinhaz.ticket.service.BookingService;
import hu.szinhaz.ticket.service.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookingService bookingService;
    private final PerformanceService performanceService;
    private final TheaterHallRepository hallRepository;

    public AdminController(BookingService bookingService,
                           PerformanceService performanceService,
                           TheaterHallRepository hallRepository) {
        this.bookingService = bookingService;
        this.performanceService = performanceService;
        this.hallRepository = hallRepository;
    }

    @GetMapping
    public String showDashboard(Model model) {
        DashboardStatsDto stats = bookingService.getDashboardStats();
        List<Booking> bookings = bookingService.getAllBookings();

        model.addAttribute("stats", stats);
        model.addAttribute("bookings", bookings);
        return "admin-dashboard";
    }

    @GetMapping("/performances/new")
    public String showNewPerformanceForm(Model model) {
        List<TheaterHall> halls = hallRepository.findAll();
        model.addAttribute("halls", halls);
        model.addAttribute("performanceDto", new PerformanceCreateDto());
        return "admin-new-performance";
    }

    @PostMapping("/performances/new")
    public String savePerformance(@Valid @ModelAttribute("performanceDto") PerformanceCreateDto performanceDto,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("halls", hallRepository.findAll());
            return "admin-new-performance";
        }

        try {
            performanceService.createPerformance(performanceDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Az új előadás ('" + performanceDto.getTitle() + "') sikeresen rögzítve, a nézőtéri helyek legenerálva!");
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("halls", hallRepository.findAll());
            model.addAttribute("errorMessage", "Hiba az előadás mentésekor: " + e.getMessage());
            return "admin-new-performance";
        }
    }
}
