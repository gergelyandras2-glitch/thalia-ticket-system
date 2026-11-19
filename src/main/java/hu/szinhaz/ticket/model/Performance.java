package hu.szinhaz.ticket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "performances")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    private int durationMinutes;

    @Column(length = 255)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private TheaterHall hall;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceSeat> seats = new ArrayList<>();

    public Performance() {}

    public Performance(String title, String author, String description, String genre,
                       LocalDateTime dateTime, BigDecimal basePrice, int durationMinutes,
                       String imageUrl, TheaterHall hall) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.dateTime = dateTime;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.imageUrl = imageUrl;
        this.hall = hall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TheaterHall getHall() {
        return hall;
    }

    public void setHall(TheaterHall hall) {
        this.hall = hall;
    }

    public List<PerformanceSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<PerformanceSeat> seats) {
        this.seats = seats;
    }

    public String getFormattedDate() {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MMMM d. (EEEE) HH:mm", java.util.Locale.forLanguageTag("hu-HU"));
        return dateTime.format(formatter);
    }

    public String getShortDate() {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
        return dateTime.format(formatter);
    }

    public long getAvailableSeatsCount() {
        if (seats == null) return 0;
        return seats.stream().filter(s -> s.getStatus() == SeatStatus.FREE).count();
    }

    public long getTotalSeatsCount() {
        if (seats == null) return 0;
        return seats.size();
    }
}
