package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "lectern", icon = Material.LECTERN, displayName = "Lectern")
public class Lectern extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.LECTERN, 1);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("WWW", "BBB", "WWW");

        RecipeChoice.MaterialChoice woodSlabs = new RecipeChoice.MaterialChoice(Material.OAK_SLAB, Material.DARK_OAK_SLAB, Material.ACACIA_SLAB, Material.BIRCH_SLAB,
                Material.JUNGLE_SLAB, Material.SPRUCE_SLAB, Material.CRIMSON_SLAB, Material.WARPED_SLAB);

        recipe.setIngredient('W', woodSlabs);
        recipe.setIngredient('B', Material.BOOK);
        return recipe;
    }
}