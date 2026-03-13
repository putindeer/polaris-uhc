package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "ghast-drop", icon = Material.GHAST_TEAR, displayName = "Ghast Drop")
public class GhastDrop extends ToggleHandler {
    @EventHandler
    public void onGhastDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.GHAST) return;

        event.getDrops().replaceAll(item -> item.getType() == Material.GHAST_TEAR ? new ItemStack(Material.GOLD_INGOT, item.getAmount()) : item);
    }
}
