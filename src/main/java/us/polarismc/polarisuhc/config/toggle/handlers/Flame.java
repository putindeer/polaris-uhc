package us.polarismc.polarisuhc.config.toggle.handlers;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

import java.util.Map;

@ToggleInfo(id = "flame", icon = Material.BOW, displayName = "Flame", listenerWhenDisabled = true)
public class Flame extends ToggleHandler {
    Enchantment enchant = Enchantment.FLAME;
    Enchantment replaceEnchant = Enchantment.POWER;
    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Map<Enchantment, Integer> enchants = event.getEnchantsToAdd();
        if (!enchants.containsKey(enchant)) return;

        int level = enchants.get(enchant);
        enchants.remove(enchant);

        plugin.utils.message(event.getEnchanter(), "<red>Flame enchantment is disabled. You got Power instead.");

        if (enchants.containsKey(Enchantment.SMITE) || enchants.containsKey(Enchantment.BANE_OF_ARTHROPODS)) return;

        if (enchants.containsKey(replaceEnchant)) {
            int currentLevel = enchants.get(replaceEnchant);
            if (currentLevel >= level) {
                return;
            }
        }

        enchants.put(replaceEnchant, level);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        ItemEnchantments enchantData = item.getData(DataComponentTypes.ENCHANTMENTS);
        if (enchantData == null) return;

        Map<Enchantment, Integer> enchants = enchantData.enchantments();

        if (!enchants.containsKey(enchant)) return;

        int level = item.getEnchantmentLevel(enchant);
        item.removeEnchantment(enchant);

        plugin.utils.message(event.getWhoClicked(), "<red>Flame enchantment is disabled. You got Power instead.");

        if (enchants.containsKey(replaceEnchant)) {
            int currentLevel = enchants.get(replaceEnchant);
            if (currentLevel >= level) {
                return;
            }
        }

        item.addEnchantment(enchant, level);
    }
}