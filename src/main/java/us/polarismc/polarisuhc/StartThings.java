package us.polarismc.polarisuhc;

import fr.mrmicky.fastinv.FastInvManager;
import us.polarismc.polarisuhc.commands.debug.Debug;
import us.polarismc.polarisuhc.commands.debug.GUI;
import us.polarismc.polarisuhc.commands.host.CreateWorld;
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
        registerLanguage();
    }

    public void disable() {

    }

    public void registerCommands() {
        // Dev commands (uhc.dev)
        new Debug(plugin);
        new GUI(plugin);
        // Host commands (uhc.host)
        new CreateWorld(plugin);
    }

    public void registerListeners() {

    }

    public void registerManagers() {
        FastInvManager.register(plugin);
        plugin.uhc = new UHCManager(plugin);
    }

    public void registerLanguage() {
        //TODO - rehacer esto pls
    }
}
