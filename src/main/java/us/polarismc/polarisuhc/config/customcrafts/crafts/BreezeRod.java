package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "breeze_rod", icon = Material.BREEZE_ROD, displayName = "Breeze Rods")
public class BreezeRod extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.BREEZE_ROD, 2);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("  C", " D ", "C  ");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('C', Material.LIGHTNING_ROD);
        return recipe;
    }
}