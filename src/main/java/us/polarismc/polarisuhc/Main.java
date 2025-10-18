package us.polarismc.polarisuhc;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import me.putindeer.api.util.PluginUtils;
import us.polarismc.polarisuhc.managers.scenario.ScenarioManager;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

public final class Main extends JavaPlugin {
    @Getter
    public static Main instance;
    public PluginUtils utils;
    public StartThings start;
    public ScenarioManager scen;
    public UHCManager uhc;
    @Override
    public void onEnable() {
        instance = this;
        utils = new PluginUtils(this, "<blue><bold>UHC</bold></blue> <dark_gray>»</dark_gray> <reset>");
        start = new StartThings(this);
    }

    @Override
    public void onDisable() {
        start.disable();
    }
}
