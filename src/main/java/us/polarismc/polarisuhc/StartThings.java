package us.polarismc.polarisuhc;

import us.polarismc.api.managers.StartupManager;
import us.polarismc.polarisuhc.commands.staff.CreateWorld;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

public class StartThings {
    private final Main plugin;
    public StartThings(Main plugin) {
        this.plugin = plugin;
        enable();
    }

    public void enable() {
        registerCommands();
        registerListeners();
        registerManagers();
        StartupManager.registerMessage(plugin, "hola");
        StartupManager.registerMessage(plugin, "holaasdfa");
        StartupManager.registerMessage(plugin, "holaweagweg");
        StartupManager.registerMessage(plugin, "holaasdfgdvc");
    }

    public void disable() {

    }

    public void registerCommands() {
        new CreateWorld(plugin);
    }

    public void registerListeners() {

    }

    public void registerManagers() {
        plugin.uhc = new UHCManager(plugin);
    }
}
