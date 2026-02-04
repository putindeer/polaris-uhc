package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;

@Getter
public enum UHCState {
    IDLE(false, false),
    PRESTARTED(true, false),
    SCATTERING(true, false),
    SCATTERED(true, false),
    STARTED(false, true),
    PVP(false, true),
    MEETUP(false, true),
    FINALIZED(false, true);

    private final boolean starting;
    private final boolean started;

    /**
     * Constructs a UHCState enum constant representing a specific state in a UHC game.
     *
     * @param starting Indicates whether this state represents a starting state.
     * @param started  Indicates whether this state represents an already started state.
     */
    UHCState(boolean starting, boolean started) {
        this.started = started;
        this.starting = starting;
    }
}