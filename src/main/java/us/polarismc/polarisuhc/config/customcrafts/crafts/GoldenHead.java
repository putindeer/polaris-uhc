package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "golden_head", icon = Material.GOLDEN_APPLE, displayName = "Golden Head")
public class GoldenHead extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = plugin.utils.head();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("GGG", "GHG", "GGG");
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('H', Material.PLAYER_HEAD);
        return recipe;
    }
}