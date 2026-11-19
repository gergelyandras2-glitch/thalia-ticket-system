package hu.szinhaz.ticket.model;

public enum Sector {
    ZSOLLYE("Zsöllye (Földszint)", 1.0, "#22c55e"),
    ERKELY("Erkély", 1.2, "#3b82f6"),
    PAHOLY("VIP Páholy", 1.6, "#eab308");

    private final String displayName;
    private final double priceMultiplier;
    private final String colorCode;

    Sector(String displayName, double priceMultiplier, String colorCode) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public String getColorCode() {
        return colorCode;
    }
}
