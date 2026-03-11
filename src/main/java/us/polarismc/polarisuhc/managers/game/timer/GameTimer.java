package us.polarismc.polarisuhc.managers.game.timer;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;

public class GameTimer extends BukkitRunnable {
    private final Main plugin;

    @Getter private boolean running = false;
    private int elapsedSeconds;

    private int finalheal;
    private int pvptime;
    private int meetuptime;

    @Getter private String formatted;

    private final int[] pvpReminders    = { 60*60, 30*60, 15*60, 10*60, 5*60, 60 };
    private final int[] meetupReminders = { 60*60, 30*60, 15*60, 10*60, 5*60, 60 };

    private final List<GameEvent> events = new ArrayList<>();

    public GameTimer(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (running) return;
        running = true;
        elapsedSeconds = 0;

        finalheal = plugin.uhc.duration.getFinalHealTime() * 60;
        if (finalheal <= 0) finalheal = -1;

        pvptime = plugin.uhc.duration.getPvpTime() * 60;
        if (pvptime <= 0) pvptime = 1;

        meetuptime = plugin.uhc.duration.getMeetupTime() * 60;
        if (meetuptime <= 0) meetuptime = 1;

        this.runTaskTimer(plugin, 20L, 20L);
    }

    public void stop() {
        running = false;
        this.cancel();
    }

    @Override
    public void run() {
        if (!running || plugin.uhc.isFinalized()) {
            stop();
            return;
        }

        int hor = elapsedSeconds / 3600;
        int min = (elapsedSeconds % 3600) / 60;
        int sec = elapsedSeconds % 60;
        formatted = String.format("%02d:%02d:%02d", hor, min, sec);

        checkReminders(pvptime, pvpReminders, "<blue>PvP On<gray> is in <aqua>%s</aqua>");
        checkReminders(pvptime + meetuptime, meetupReminders, "<gold>Meetup<gray> is in <yellow>%s</yellow>");

        if (finalheal > 0 && elapsedSeconds == finalheal) {
            plugin.game.finalHeal();
        }
        if (elapsedSeconds == pvptime) {
            plugin.game.startPvP();
        }
        if (elapsedSeconds == pvptime + meetuptime) {
            plugin.game.startMeetup();
        }

        for (GameEvent event : events) {
            if (event.fired) continue;

            int target = event.absoluteSecond(finalheal, pvptime, meetuptime);
            if (target < 0) continue;

            if (elapsedSeconds == target) {
                event.fired = true;
                event.action.run();
            }
        }

        elapsedSeconds++;
    }

    public void addEvent(EventAnchor anchor, int seconds, Runnable action) {
        events.add(new GameEvent(anchor, seconds, action));
    }

    public boolean setFinalHealTime(int minutes) {
        if (plugin.uhc.hasPvPStarted()) return false;
        int newValue = minutes * 60;
        if (newValue <= 0) {
            finalheal = -1;
        } else {
            finalheal = newValue;
        }
        resetUnfiredEventsForAnchors(EventAnchor.PRE_FINAL_HEAL, EventAnchor.POST_FINAL_HEAL);
        return true;
    }

    public boolean setPvpTime(int minutes) {
        if (plugin.uhc.hasPvPStarted()) return false;
        pvptime = Math.max(1, minutes * 60);
        resetUnfiredEventsForAnchors(EventAnchor.PRE_PVP, EventAnchor.POST_PVP, EventAnchor.PRE_MEETUP, EventAnchor.POST_MEETUP);
        return true;
    }

    public boolean setMeetupTime(int minutes) {
        if (plugin.uhc.hasMeetupStarted()) return false;
        meetuptime = Math.max(1, minutes * 60);
        resetUnfiredEventsForAnchors(EventAnchor.PRE_MEETUP, EventAnchor.POST_MEETUP);
        return true;
    }

    private void resetUnfiredEventsForAnchors(EventAnchor... anchors) {
        for (GameEvent event : events) {
            for (EventAnchor anchor : anchors) {
                if (event.anchor == anchor) {
                    int newTarget = event.absoluteSecond(finalheal, pvptime, meetuptime);
                    if (newTarget > elapsedSeconds) {
                        event.fired = false;
                    }
                }
            }
        }
    }

    private void checkReminders(int targetSeconds, int[] milestones, String messageFormat) {
        int remaining = targetSeconds - elapsedSeconds;
        if (remaining < 0) return;

        for (int milestone : milestones) {
            if (remaining == milestone) {
                plugin.utils.broadcast(messageFormat, formatDuration(milestone));
                return;
            }
        }
    }

    private String formatDuration(int totalSeconds) {
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0 && minutes == 0 && seconds == 0) return hours + "h";
        if (hours == 0 && minutes > 0 && seconds == 0) return minutes + "m";

        StringBuilder builder = new StringBuilder();
        if (hours   > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0 || builder.isEmpty()) builder.append(seconds).append("s");

        return builder.toString().trim();
    }

    public String remainingNext() {
        if (!plugin.uhc.hasPvPStarted()) return remainingPvP();
        if (!plugin.uhc.hasMeetupStarted()) return remainingMeetup();
        if (plugin.uhc.isFinalized()) return "Finalized";
        return "";
    }

    public String remainingFinalHeal() {
        return remainingUntil("Final Heal", finalheal);
    }

    public String remainingPvP() {
        return remainingUntil("PvP", pvptime);
    }

    public String remainingMeetup() {
        return remainingUntil("Meetup", pvptime + meetuptime);
    }

    private String remainingUntil(String label, int targetSeconds) {
        int remaining = Math.max(0, targetSeconds - elapsedSeconds);

        int hours   = remaining / 3600;
        int minutes = (remaining % 3600) / 60;
        int seconds = remaining % 60;

        StringBuilder time = new StringBuilder();
        if (hours > 0) time.append(hours).append("h ");
        time.append(minutes).append("m ").append(seconds).append("s");

        return "<aqua>" + label + " <dark_gray>» <gray>" + time;
    }
}