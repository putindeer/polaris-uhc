package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "trident", icon = Material.TRIDENT, displayName = "Trident")
public class Trident extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = plugin.utils.ib(Material.TRIDENT).enchant(Enchantment.LOYALTY, 1).build();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" dd", " sd", "s  ");
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('s', Material.STICK);
        return recipe;
    }
}