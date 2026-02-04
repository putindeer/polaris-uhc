package us.polarismc.polarisuhc.managers.game;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import us.polarismc.polarisuhc.Main;

public class GameTimer extends BukkitRunnable {
    private final Main plugin;
    @Getter private boolean running = false;
    private int elapsedSeconds = 0;
    private int finalheal;
    private int pvptime;
    private int meetuptime;
    @Getter private String formatted;
    private final int[] pvpReminders = { 60*60, 30*60, 15*60, 10*60, 5*60, 60 };
    private final int[] meetupReminders = { 60*60, 30*60, 15*60, 10*60, 5*60, 60 };

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

        elapsedSeconds += 1;

        int hor = (elapsedSeconds / 3600);
        int min = ((elapsedSeconds % 3600) / 60);
        int sec = (elapsedSeconds % 60);

        formatted = String.format("%02d:%02d:%02d", hor, min, sec);

        if
        checkReminders(pvptime, pvpReminders, "<blue>PvP On<gray> is in <aqua>%s</aqua>");
        checkReminders(pvptime + meetuptime, meetupReminders, "<gold>Meetup<gray> is in <yellow>%s</yellow>");

        if (elapsedSeconds == finalheal) {
            plugin.game.finalHeal();
        }

        if (elapsedSeconds == pvptime) {
            plugin.game.startPvP();
        }

        if (elapsedSeconds == pvptime + meetuptime) {
            plugin.game.startMeetup();
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
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0 && minutes == 0 && seconds == 0) {
            return hours + "h";
        }

        if (hours == 0 && minutes > 0 && seconds == 0) {
            return minutes + "m";
        }

        StringBuilder builder = new StringBuilder();

        if (hours > 0) {
            builder.append(hours).append("h ");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }
        if (seconds > 0 || builder.isEmpty()) {
            builder.append(seconds).append("s");
        }

        return builder.toString().trim();
    }

    public String remainingFinalHeal() {
        return remainingUntil("Final Heal", finalheal);
    }

    public String remainingPvP() {
        return remainingUntil("PvP", pvptime);
    }

    public String remainingMeetup() {
        return remainingUntil("Meetup", meetuptime);
    }

    private String remainingUntil(String label, int targetSeconds) {
        int remaining = Math.max(0, targetSeconds - elapsedSeconds);

        int hours = remaining / 3600;
        int minutes = (remaining % 3600) / 60;
        int seconds = remaining % 60;

        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            time.append(hours).append("h ");
        }
        time.append(minutes).append("m ").append(seconds).append("s");

        return "<aqua>" + label + " <dark_gray>» <gray>" + time;
    }
}