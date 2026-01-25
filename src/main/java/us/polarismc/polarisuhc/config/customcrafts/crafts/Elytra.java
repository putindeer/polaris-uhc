package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "elytra", icon = Material.ELYTRA, displayName = "Elytra (50% Durability)")
public class Elytra extends CustomCraft {
    public Elytra(Main plugin) {
        super(plugin, NamespacedKey.minecraft("elytra"), build(plugin));
    }

    private static ShapedRecipe build(Main plugin) {
        ItemStack result = plugin.utils.ib(new org.bukkit.inventory.ItemStack(Material.ELYTRA, 1)).durability(175).build();

        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("elytra"), result);
        recipe.shape(" D ", "CSC", "F F");
        recipe.setIngredient('D', Material.DIAMOND_CHESTPLATE);
        recipe.setIngredient('C', Material.AMETHYST_SHARD);
        recipe.setIngredient('S', Material.SADDLE);
        recipe.setIngredient('F', Material.FEATHER);
        return recipe;
    }
}
