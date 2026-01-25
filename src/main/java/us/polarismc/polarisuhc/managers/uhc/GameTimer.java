package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import us.polarismc.polarisuhc.Main;

public class GameTimer extends BukkitRunnable {
    private final Main plugin;
    @Getter
    private boolean running = false;
    private long elapsedTicks = 0;
    private long lastElapsedSeconds = -1;
    private long finalheal;
    private long pvptime;
    private long meetuptime;
    @Getter
    private String formatted;

    public GameTimer(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (running) return;
        running = true;
        elapsedTicks = 0;

        finalheal = plugin.uhc.duration.getFinalHealTime() * 60L;
        pvptime = plugin.uhc.duration.getPvpTime() * 60L;
        meetuptime = plugin.uhc.duration.getMeetupTime() * 60L;

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

        elapsedTicks += 20L;
        long elapsedSeconds = elapsedTicks / 20L;

        if (elapsedSeconds == lastElapsedSeconds) return;

        lastElapsedSeconds = elapsedSeconds;

        int hor = (int) (elapsedSeconds / 3600L);
        int min = (int) ((elapsedSeconds % 3600L) / 60L);
        int sec = (int) (elapsedSeconds % 60L);

        formatted = String.format("%02d:%02d:%02d", hor, min, sec);

        if (elapsedSeconds == finalheal) {
            plugin.uhc.duration.handleFinalHeal();
        }

        if (elapsedSeconds == pvptime) {
            plugin.uhc.duration.handlePvpEnable();
        }

        if (elapsedSeconds == pvptime + meetuptime) {
            plugin.uhc.duration.handleMeetup();
        }
    }
}