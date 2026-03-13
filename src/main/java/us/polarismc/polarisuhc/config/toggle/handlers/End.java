package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "end", icon = Material.END_STONE, displayName = "End", listenerWhenDisabled = true)
public class End extends ToggleHandler {
    @EventHandler
    public void teleportingIntoEnd(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;

        //TODO - add meetup listener when enabled

        event.setCancelled(true);
        plugin.utils.message(event.getPlayer(), "<red>The End dimension is disabled.");
    }
}
