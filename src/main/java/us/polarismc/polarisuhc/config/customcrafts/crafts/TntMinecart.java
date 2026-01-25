package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "tnt_minecart", icon = Material.TNT_MINECART, displayName = "TNT Minecart",
        lore = "Overrides vanilla tnt minecart recipe.",
        removedVanillaRecipes = "tnt_minecart")
public class TntMinecart extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.TNT_MINECART, 1);
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("III", "ITI", "RRR");
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('T', Material.TNT);
        return recipe;
    }
}