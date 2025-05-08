package us.polarismc.polarisuhc;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.polarismc.api.util.PluginUtils;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

public final class Main extends JavaPlugin {
    @Getter
    public Main instance;
    public PluginUtils utils;
    public StartThings start;
    public UHCManager uhc;
    @Override
    public void onEnable() {
        instance = this;
        utils = new PluginUtils(this, "<blue><bold>UHC <dark_gray>» <reset>");
        start = new StartThings(this);
    }

    @Override
    public void onDisable() {
        start.disable();
    }
}
