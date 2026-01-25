package us.polarismc.polarisuhc.events.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.UHCDeathEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class DeathHandler implements Listener {
    private final Main plugin;
    public DeathHandler(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        //TODO - port death event
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        Bukkit.getPluginManager().callEvent(new UHCDeathEvent(event, uhcPlayer));
    }
}
