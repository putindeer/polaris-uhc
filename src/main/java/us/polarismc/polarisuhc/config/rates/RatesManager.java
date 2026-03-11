package us.polarismc.polarisuhc.config.rates;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import us.polarismc.polarisuhc.Main;

@Getter
@Setter
public class RatesManager {
    private final Main plugin;
    public RatesManager(Main plugin) {
        this.plugin = plugin;
        initializeRates();
    }

    private int xpKillRate;
    private int flintRate;
    private int appleRate;
    private int glassRate;

    private void initializeRates() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("rates");
        assert config != null;
        xpKillRate = config.getInt("xp-kill");
        flintRate = config.getInt("flint");
        appleRate = config.getInt("apple");
        glassRate = config.getInt("glass");
    }
}
