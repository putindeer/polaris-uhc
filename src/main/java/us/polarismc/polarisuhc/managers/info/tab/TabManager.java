package us.polarismc.polarisuhc.managers.info.tab;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class TabManager implements Listener {
    private final Main plugin;
    public TabManager(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(event.getPlayer());
        uhcPlayer.updateDisplayName();
    }
}
