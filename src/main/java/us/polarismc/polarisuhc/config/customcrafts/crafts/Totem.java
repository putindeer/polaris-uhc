package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "totem", icon = Material.TOTEM_OF_UNDYING, displayName = "Totem of Undying")
public class Totem extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        NamespacedKey key = new NamespacedKey(plugin, "totem");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("EGE", "GGG", " G ");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('E', Material.EMERALD);
        return recipe;
    }
}