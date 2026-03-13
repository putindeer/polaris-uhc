package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;
import us.polarismc.polarisuhc.events.UHCPlayerLoadoutEvent;

@ToggleInfo(id = "starter-books", icon = Material.BOOK, displayName = "Starter Books")
public class StarterBooks extends ToggleHandler {
    @EventHandler
    public void onLoadout(UHCPlayerLoadoutEvent event) {
        event.addItem(new ItemStack(Material.BOOK, 3));
    }
}