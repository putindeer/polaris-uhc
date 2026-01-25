package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "glistering_melon", icon = Material.GLISTERING_MELON_SLICE, displayName = "Glistering Melon",
        lore = "Overrides vanilla glistering melon recipe.",
        removedVanillaRecipes = "glistering_melon_slice")
public class GlisteringMelon extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.GLISTERING_MELON_SLICE);
        NamespacedKey key = new NamespacedKey(plugin, "melon");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("GGG", "GMG", "GGG");
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('M', Material.MELON_SLICE);
        return recipe;
    }
}