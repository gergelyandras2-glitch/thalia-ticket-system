package hu.szinhaz.ticket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class BookingRequestDto {

    private Long performanceId;

    @NotEmpty(message = "Legalább egy ülőhelyet ki kell választani!")
    private List<Long> seatIds;

    @NotBlank(message = "A név megadása kötelező!")
    private String customerName;

    @NotBlank(message = "Az e-mail cím megadása kötelező!")
    @Email(message = "Kérjük, érvényes e-mail címet adjon meg!")
    private String customerEmail;

    @NotBlank(message = "A telefonszám megadása kötelező!")
    @Pattern(regexp = "^[+0-9\\s-]{7,20}$", message = "Kérjük, érvényes telefonszámot adjon meg!")
    private String customerPhone;

    public BookingRequestDto() {}

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
