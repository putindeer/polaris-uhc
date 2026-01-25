package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "spectral_arrows", icon = Material.SPECTRAL_ARROW, displayName = "Spectral Arrows")
public class SpectralArrows extends CustomCraft {
    public SpectralArrows(Main plugin) {
        super(plugin, NamespacedKey.minecraft("spectral_arrows"), build());
    }

    private static ShapedRecipe build() {
        ItemStack result = new ItemStack(Material.SPECTRAL_ARROW, 2);
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("spectral_arrows"), result);
        recipe.shape(" C ", "CAC", " C ");
        recipe.setIngredient('C', Material.COPPER_INGOT);
        recipe.setIngredient('A', Material.ARROW);
        return recipe;
    }
}
