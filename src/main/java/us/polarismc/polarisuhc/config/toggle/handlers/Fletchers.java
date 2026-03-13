package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "fletchers", icon = Material.FLETCHING_TABLE, displayName = "Fletchers", listenerWhenDisabled = true)
public class Fletchers extends ToggleHandler {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;
        if (villager.getProfession() != Villager.Profession.FLETCHER) return;

        event.setCancelled(true);
        plugin.utils.message(event.getPlayer(), "<red>Fletchers are disabled.");
    }
}
