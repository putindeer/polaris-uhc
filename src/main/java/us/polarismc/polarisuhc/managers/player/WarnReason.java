package us.polarismc.polarisuhc.managers.player;

import lombok.Getter;

@Getter
public enum WarnReason {
    CROSSTEAM("Crossteam"),
    SKYBASE("Skybasing in Meetup"),
    RUNNING("Running in Meetup"),
    MINING("Mining in Meetup"),
    CAMPING("Camping in Meetup"),
    STALKING("Stalking");

    private final String displayName;

    WarnReason(String displayName) {
        this.displayName = displayName;
    }
}
