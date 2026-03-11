package us.polarismc.polarisuhc.managers.game.timer;

public class GameEvent {
    public final EventAnchor anchor;
    public final int offsetSeconds;
    public final Runnable action;
    public boolean fired;

    public GameEvent(EventAnchor anchor, int offsetSeconds, Runnable action) {
        this.anchor = anchor;
        this.offsetSeconds = offsetSeconds;
        this.action = action;
        this.fired = false;
    }

    public int absoluteSecond(int finalheal, int pvptime, int meetuptime) {
        return switch (anchor) {
            case PRE_FINAL_HEAL -> finalheal - offsetSeconds;
            case POST_FINAL_HEAL -> finalheal + offsetSeconds;
            case PRE_PVP -> pvptime - offsetSeconds;
            case POST_PVP -> pvptime + offsetSeconds;
            case PRE_MEETUP -> (pvptime + meetuptime) - offsetSeconds;
            case POST_MEETUP -> (pvptime + meetuptime) + offsetSeconds;
        };
    }
}
