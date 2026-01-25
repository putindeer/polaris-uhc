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

@Scenario(name = "HasteyBoys+", author = "putindeer", icon = Material.DIAMOND_PICKAXE,
        description = "Every tool has Efficiency V and Unbreaking III.",
        incompatibleWith = {ScenarioType.HASTEY_BABIES, ScenarioType.HASTEY_BOYS})
public class HasteyBoysPlus extends BaseScenario {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (!plugin.utils.isTool(item)) return;
        if (item.getEnchantmentLevel(Enchantment.EFFICIENCY) >= 5) return;

        addEnchantments(item);
    }

    private void addEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.EFFICIENCY,5,true);
        if (!plugin.scen.get(ScenarioType.UNBREAKABLE).isEnabled()) {
            meta.addEnchant(Enchantment.UNBREAKING,3,true);
        }
        item.setItemMeta(meta);
    }
}