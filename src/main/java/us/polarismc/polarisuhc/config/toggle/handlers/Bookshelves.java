package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "bookshelves", icon = Material.BOOKSHELF, displayName = "Bookshelves", listenerWhenDisabled = true)
public class Bookshelves extends ToggleHandler {
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getRecipe().getResult().getType() != Material.BOOKSHELF) return;

        plugin.utils.message(player, "<red>Bookshelves are disabled, you're still able to craft them but you won't be able to put them.");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placed = event.getBlock();
        Material type = placed.getType();

        if (type != Material.BOOKSHELF && type != Material.ENCHANTING_TABLE) return;

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    Material nearby = placed.getRelative(x, y, z).getType();
                    if (type == Material.BOOKSHELF && nearby == Material.ENCHANTING_TABLE) {
                        event.setCancelled(true);
                        plugin.utils.message(event.getPlayer(), "<red>You can't place bookshelves near enchanting tables!");
                        return;
                    }
                    if (type == Material.ENCHANTING_TABLE && nearby == Material.BOOKSHELF) {
                        event.setCancelled(true);
                        plugin.utils.message(event.getPlayer(), "<red>You can't place enchanting tables near bookshelves!");
                        return;
                    }
                }
            }
        }
    }
}
