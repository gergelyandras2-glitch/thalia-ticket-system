package hu.szinhaz.ticket.controller;

import hu.szinhaz.ticket.model.Performance;
import hu.szinhaz.ticket.service.PerformanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping
    public String index() {
        return "redirect:/performances";
    }

    @GetMapping("/performances")
    public String listPerformances(@RequestParam(value = "query", required = false) String query,
                                   @RequestParam(value = "genre", required = false) String genre,
                                   Model model) {
        List<Performance> performances = performanceService.search(query, genre, LocalDateTime.now());
        List<String> genres = performanceService.getAllGenres();

        model.addAttribute("performances", performances);
        model.addAttribute("genres", genres);
        model.addAttribute("selectedQuery", query != null ? query : "");
        model.addAttribute("selectedGenre", genre != null ? genre : "ALL");
        return "performances";
    }

    @GetMapping("/performances/{id}")
    public String showPerformanceDetails(@PathVariable("id") Long id, Model model) {
        Performance performance = performanceService.findById(id);
        model.addAttribute("performance", performance);
        return "performance-details";
    }
}
