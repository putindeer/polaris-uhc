package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "tnt_minecart", icon = Material.TNT_MINECART, displayName = "TNT Minecart",
        lore = { "Overrides vanilla tnt minecart recipe." })
public class TntMinecart extends CustomCraft {
    public TntMinecart(Main plugin) {
        super(plugin, NamespacedKey.minecraft("tnt_minecart"), build());
        removeVanillaRecipe(NamespacedKey.minecraft("tnt_minecart"));
    }

    private static ShapedRecipe build() {
        ItemStack result = new ItemStack(Material.TNT_MINECART, 1);
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("tnt_minecart"), result);

        recipe.shape("III", "ITI", "RRR");
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('T', Material.TNT);
        return recipe;
    }
}
