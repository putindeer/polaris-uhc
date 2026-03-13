package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "anti-item-destruction", icon = Material.LAVA_BUCKET, displayName = "Anti Item Destruction")
public class AntiItemDestruction extends ToggleHandler {
    @EventHandler(ignoreCancelled = true)
    public void onItemDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Item item) {
            event.setCancelled(true);
        }
    }
}