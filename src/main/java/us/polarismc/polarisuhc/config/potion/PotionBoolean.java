package us.polarismc.polarisuhc.config.potion;

public enum PotionBoolean {
    FALSE,
    TIER1,
    TRUE;

    public PotionBoolean next() {
        return switch (this) {
            case FALSE -> TIER1;
            case TIER1 -> TRUE;
            case TRUE -> FALSE;
        };
    }
}
