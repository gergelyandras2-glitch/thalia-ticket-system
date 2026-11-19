package hu.szinhaz.ticket.dto;

import hu.szinhaz.ticket.model.PerformanceSeat;
import hu.szinhaz.ticket.model.SeatStatus;
import hu.szinhaz.ticket.model.Sector;

import java.math.BigDecimal;

public class SeatDto {
    private Long id;
    private int rowNumber;
    private int seatNumber;
    private Sector sector;
    private String sectorDisplayName;
    private String sectorColor;
    private SeatStatus status;
    private BigDecimal price;

    public SeatDto() {}

    public SeatDto(PerformanceSeat seat) {
        this.id = seat.getId();
        this.rowNumber = seat.getRowNumber();
        this.seatNumber = seat.getSeatNumber();
        this.sector = seat.getSector();
        this.sectorDisplayName = seat.getSector().getDisplayName();
        this.sectorColor = seat.getSector().getColorCode();
        this.status = seat.getStatus();
        this.price = seat.calculatePrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    }

    public String getSectorDisplayName() {
        return sectorDisplayName;
    }

    public void setSectorDisplayName(String sectorDisplayName) {
        this.sectorDisplayName = sectorDisplayName;
    }

    public String getSectorColor() {
        return sectorColor;
    }

    public void setSectorColor(String sectorColor) {
        this.sectorColor = sectorColor;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
