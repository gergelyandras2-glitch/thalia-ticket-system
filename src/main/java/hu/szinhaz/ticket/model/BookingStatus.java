package hu.szinhaz.ticket.model;

public enum BookingStatus {
    CONFIRMED("Véglegesített"),
    CANCELLED("Lemondva");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
