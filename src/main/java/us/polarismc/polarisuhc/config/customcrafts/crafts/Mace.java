package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "mace", icon = Material.MACE, displayName = "Mace")
public class Mace extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.MACE);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" GD", " BG", "I  ");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('B', Material.BREEZE_ROD);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        return recipe;
    }
}