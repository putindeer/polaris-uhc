package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "trident", icon = Material.TRIDENT, displayName = "Trident")
public class Trident extends CustomCraft {
    public Trident(Main plugin) {
        super(plugin, new NamespacedKey(plugin, "trident"), build(plugin));
    }

    private static ShapedRecipe build(Main plugin) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "trident"),
                plugin.utils.ib(Material.TRIDENT).enchant(Enchantment.LOYALTY, 1).build());

        recipe.shape(" dd", " sd", "s  ");
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('s', Material.STICK);
        return recipe;
    }
}
