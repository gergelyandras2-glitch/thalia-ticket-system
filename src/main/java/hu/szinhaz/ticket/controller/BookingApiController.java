package hu.szinhaz.ticket.controller;

import hu.szinhaz.ticket.dto.DashboardStatsDto;
import hu.szinhaz.ticket.dto.SeatDto;
import hu.szinhaz.ticket.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingApiController {

    private final BookingService bookingService;

    public BookingApiController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/performances/{id}/seats")
    public ResponseEntity<List<SeatDto>> getSeats(@PathVariable("id") Long id) {
        List<SeatDto> seats = bookingService.getSeatsForPerformance(id);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        DashboardStatsDto stats = bookingService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
