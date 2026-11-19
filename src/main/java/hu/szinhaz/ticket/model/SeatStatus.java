package hu.szinhaz.ticket.model;

public enum SeatStatus {
    FREE("Szabad"),
    RESERVED("Foglalt"),
    SOLD("Eladva");

    private final String displayName;

    SeatStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
