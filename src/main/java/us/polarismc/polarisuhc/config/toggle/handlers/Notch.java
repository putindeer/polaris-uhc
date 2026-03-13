package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

import java.util.ArrayList;
import java.util.List;

@ToggleInfo(id = "notch", icon = Material.ENCHANTED_GOLDEN_APPLE, displayName = "Notch Apples", listenerWhenDisabled = true)
public class Notch extends ToggleHandler {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack notch = event.getCurrentItem();
        if (notch == null || notch.getType() != Material.ENCHANTED_GOLDEN_APPLE) return;
        int compensationAmount = notch.getAmount() * 5;

        plugin.utils.message(player, "<red>Notch apples are disabled. Here is a compensation of " + compensationAmount + " golden apples.");

        if (compensationAmount > 64) {
            int leftover = compensationAmount - 64;
            compensationAmount = 64;

            List<ItemStack> stacks = new ArrayList<>();
            while (leftover > 0) {
                int chunk = Math.min(leftover, 64);
                stacks.add(new ItemStack(Material.GOLDEN_APPLE, chunk));
                leftover -= chunk;
            }
            plugin.utils.giveOrDrop(player, stacks);
        }
        event.setCurrentItem(new ItemStack(Material.GOLDEN_APPLE, compensationAmount));
    }
}