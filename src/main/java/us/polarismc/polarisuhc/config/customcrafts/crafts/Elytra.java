package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(id = "elytra", icon = Material.ELYTRA, displayName = "Elytra")
public class Elytra extends CustomCraft {
    @Override
    protected Recipe buildRecipe() {
        ItemStack result = plugin.utils.ib(new ItemStack(Material.ELYTRA, 1)).lore("Maximum durability reduced by 50%.").maxDurabilityPercentage(50).build();
        NamespacedKey key = new NamespacedKey(plugin, "elytra");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(" D ", "CSC", "F F");
        recipe.setIngredient('D', Material.DIAMOND_CHESTPLATE);
        recipe.setIngredient('C', Material.AMETHYST_SHARD);
        recipe.setIngredient('S', Material.SADDLE);
        recipe.setIngredient('F', Material.FEATHER);
        return recipe;
    }
}