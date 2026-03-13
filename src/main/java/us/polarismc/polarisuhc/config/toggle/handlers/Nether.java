package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "nether", icon = Material.CRIMSON_NYLIUM, displayName = "Nether", listenerWhenDisabled = true)
public class Nether extends ToggleHandler {
    @EventHandler
    public void teleportingIntoNether(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return;

        //TODO - add meetup listener when enabled

        event.setCancelled(true);
        plugin.utils.message(event.getPlayer(), "<red>The Nether dimension is disabled.");
    }
}