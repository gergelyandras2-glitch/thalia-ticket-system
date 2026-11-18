package hu.szinhaz.ticket.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {
    private BigDecimal totalRevenue;
    private long totalBookings;
    private long totalTicketsSold;
    private long totalAvailableSeats;
    private double overallOccupancyRate;

    public DashboardStatsDto() {
        this.totalRevenue = BigDecimal.ZERO;
    }

    public DashboardStatsDto(BigDecimal totalRevenue, long totalBookings, long totalTicketsSold, long totalAvailableSeats, double overallOccupancyRate) {
        this.totalRevenue = totalRevenue;
        this.totalBookings = totalBookings;
        this.totalTicketsSold = totalTicketsSold;
        this.totalAvailableSeats = totalAvailableSeats;
        this.overallOccupancyRate = overallOccupancyRate;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(long totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public long getTotalAvailableSeats() {
        return totalAvailableSeats;
    }

    public void setTotalAvailableSeats(long totalAvailableSeats) {
        this.totalAvailableSeats = totalAvailableSeats;
    }

    public double getOverallOccupancyRate() {
        return overallOccupancyRate;
    }

    public void setOverallOccupancyRate(double overallOccupancyRate) {
        this.overallOccupancyRate = overallOccupancyRate;
    }
}
