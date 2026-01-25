package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "spectral_arrows", icon = Material.SPECTRAL_ARROW, displayName = "Spectral Arrows")
public class SpectralArrows extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.SPECTRAL_ARROW, 2);
        NamespacedKey key = new NamespacedKey(plugin, "spectral_arrows");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" C ", "CAC", " C ");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('A', Material.ARROW);
        return recipe;
    }
}