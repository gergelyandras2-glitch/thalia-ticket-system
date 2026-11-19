package hu.szinhaz.ticket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String ticketCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performance_seat_id", nullable = false)
    private PerformanceSeat performanceSeat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime issueTime;

    public Ticket() {}

    public Ticket(String ticketCode, PerformanceSeat performanceSeat, Booking booking, BigDecimal price, LocalDateTime issueTime) {
        this.ticketCode = ticketCode;
        this.performanceSeat = performanceSeat;
        this.booking = booking;
        this.price = price;
        this.issueTime = issueTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public PerformanceSeat getPerformanceSeat() {
        return performanceSeat;
    }

    public void setPerformanceSeat(PerformanceSeat performanceSeat) {
        this.performanceSeat = performanceSeat;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public String getFormattedIssueTime() {
        if (issueTime == null) return "";
        return issueTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }
}
