package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "horses", icon = Material.HORSE_SPAWN_EGG, displayName = "Horses", listenerWhenDisabled = true)
public class Horses extends ToggleHandler {
    @EventHandler
    public void onMount(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player player)) return;
        switch (event.getVehicle().getType()) {
            case HORSE, SKELETON_HORSE, ZOMBIE_HORSE -> {
                plugin.utils.message(player, "<red>Horses are disabled.");
                event.setCancelled(true);
            }
        }
    }
}