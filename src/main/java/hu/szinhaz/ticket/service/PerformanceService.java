package hu.szinhaz.ticket.service;

import hu.szinhaz.ticket.dto.PerformanceCreateDto;
import hu.szinhaz.ticket.model.*;
import hu.szinhaz.ticket.repository.PerformanceRepository;
import hu.szinhaz.ticket.repository.PerformanceSeatRepository;
import hu.szinhaz.ticket.repository.TheaterHallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceSeatRepository seatRepository;
    private final TheaterHallRepository hallRepository;

    public PerformanceService(PerformanceRepository performanceRepository,
                              PerformanceSeatRepository seatRepository,
                              TheaterHallRepository hallRepository) {
        this.performanceRepository = performanceRepository;
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

    public List<Performance> findAll() {
        return performanceRepository.findAllByOrderByDateTimeAsc();
    }

    public Performance findById(Long id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Az előadás nem található (ID: " + id + ")"));
    }

    public List<Performance> search(String query, String genre, LocalDateTime fromTime) {
        String cleanQuery = (query != null && !query.trim().isEmpty()) ? query.trim() : null;
        String cleanGenre = (genre != null && !genre.trim().isEmpty() && !genre.equalsIgnoreCase("ALL")) ? genre.trim() : null;
        return performanceRepository.searchPerformances(cleanQuery, cleanGenre, fromTime);
    }

    public List<String> getAllGenres() {
        return performanceRepository.findAllDistinctGenres();
    }

    @Transactional
    public Performance createPerformance(PerformanceCreateDto dto) {
        TheaterHall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new IllegalArgumentException("A megadott terem nem található: " + dto.getHallId()));

        Performance performance = new Performance();
        performance.setTitle(dto.getTitle());
        performance.setAuthor(dto.getAuthor());
        performance.setDescription(dto.getDescription());
        performance.setGenre(dto.getGenre());
        performance.setDateTime(dto.getDateTime());
        performance.setBasePrice(dto.getBasePrice());
        performance.setDurationMinutes(dto.getDurationMinutes());
        performance.setImageUrl(dto.getImageUrl());
        performance.setHall(hall);

        Performance savedPerformance = performanceRepository.save(performance);

        // Auto-generate seats for this hall
        List<PerformanceSeat> seats = new ArrayList<>();
        int rows = hall.getRowsCount();
        int cols = hall.getColsCount();

        for (int r = 1; r <= rows; r++) {
            Sector sector;
            if (r <= Math.max(1, rows / 3)) {
                sector = Sector.ZSOLLYE; // Ground floor front / center
            } else if (r <= Math.max(2, (2 * rows) / 3)) {
                sector = Sector.ERKELY; // Balcony
            } else {
                sector = Sector.PAHOLY; // VIP Boxes / Upper Balcony
            }

            for (int c = 1; c <= cols; c++) {
                PerformanceSeat seat = new PerformanceSeat(savedPerformance, r, c, sector);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        savedPerformance.setSeats(seats);

        return savedPerformance;
    }
}
