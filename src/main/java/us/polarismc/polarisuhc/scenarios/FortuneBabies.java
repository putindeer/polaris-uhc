package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioType;

@Scenario(name = "FortuneBabies", author = "putindeer", icon = Material.COPPER_PICKAXE,
        description = "Every tool has Fortune I.",
        incompatibleWith = {ScenarioType.FORTUNE_BOYS, ScenarioType.FORTUNE_BOYS_PLUS})
public class FortuneBabies extends BaseScenario {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (!plugin.utils.isTool(item)) return;
        if (item.getEnchantmentLevel(Enchantment.FORTUNE) >= 1) return;

        addEnchantments(item);
    }

    private void addEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.FORTUNE,1,true);
        item.setItemMeta(meta);
    }
}