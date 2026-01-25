package us.polarismc.polarisuhc.config.duration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import us.polarismc.polarisuhc.Main;

@Getter
@Setter
public class DurationManager {
    private final Main plugin;
    public DurationManager(Main plugin) {
        this.plugin = plugin;
    }

    public void handleFinalHeal() {
        plugin.utils.broadcast("FINAL HEAL");
    }

    public void handlePvpEnable() {
        plugin.utils.broadcast("PVP ENABLED");
    }

    public void handleMeetup() {
        plugin.utils.broadcast("MEETUP ON");
    }

    private int pvpTime;
    private int meetupTime;
    private int finalHealTime;

    private void initializeDuration() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("duration");
        assert config != null;
        pvpTime = config.getInt("pvp");
        meetupTime = config.getInt("meetup");
        finalHealTime = config.getInt("finalheal");
    }
}
