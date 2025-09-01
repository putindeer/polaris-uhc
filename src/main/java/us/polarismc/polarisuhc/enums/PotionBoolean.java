package us.polarismc.polarisuhc.enums;

public enum PotionBoolean {
    OFF,
    TIER1,
    ON;

    public PotionBoolean next() {
        return switch (this) {
            case OFF -> TIER1;
            case TIER1 -> ON;
            case ON -> OFF;
        };
    }
}
