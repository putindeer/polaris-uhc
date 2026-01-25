package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "totem", icon = Material.TOTEM_OF_UNDYING, displayName = "Totem of Undying")
public class Totem extends CustomCraft {
    public Totem(Main plugin) {
        super(plugin, NamespacedKey.minecraft("totem_of_undying"), build());
    }

    private static ShapedRecipe build() {
        ItemStack result = new ItemStack(Material.TOTEM_OF_UNDYING, 1);

        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("totem_of_undying"), result);
        recipe.shape("EGE", "GGG", " G ");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('E', Material.EMERALD);
        return recipe;
    }
}
