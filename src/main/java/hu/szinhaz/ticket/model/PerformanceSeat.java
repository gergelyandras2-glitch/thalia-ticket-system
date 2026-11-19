package hu.szinhaz.ticket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "performance_seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"performance_id", "row_number", "seat_number"})
})
public class PerformanceSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sector sector;

    private double priceMultiplier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status = SeatStatus.FREE;

    @Version
    private Long version;

    public PerformanceSeat() {}

    public PerformanceSeat(Performance performance, int rowNumber, int seatNumber, Sector sector) {
        this.performance = performance;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.sector = sector;
        this.priceMultiplier = sector.getPriceMultiplier();
        this.status = SeatStatus.FREE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
        this.priceMultiplier = sector.getPriceMultiplier();
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal calculatePrice() {
        if (performance == null || performance.getBasePrice() == null) {
            return BigDecimal.ZERO;
        }
        return performance.getBasePrice()
                .multiply(BigDecimal.valueOf(priceMultiplier))
                .setScale(0, RoundingMode.HALF_UP);
    }

    public String getSeatLabel() {
        return sector.getDisplayName() + " - " + rowNumber + ". sor " + seatNumber + ". szék";
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.FREE;
    }
}
