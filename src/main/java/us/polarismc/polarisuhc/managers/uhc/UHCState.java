package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;

@Getter
public enum UHCState {
    IDLE,
    // Starting phase
    PRESTARTED,
    SCATTERING,
    SCATTERED,
    // Started phase
    STARTED,
    PVP,
    MEETUP,
    FINALIZED;

    private final int priority = ordinal();
}