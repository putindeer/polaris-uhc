package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "golden_head", icon = Material.GOLDEN_APPLE, displayName = "Golden Head")
public class GoldenHead extends CustomCraft {
    public GoldenHead(Main plugin) {
        super(plugin, new NamespacedKey(plugin, "golden_head"), build(plugin));
    }

    private static ShapedRecipe build(Main plugin) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "golden_head"), plugin.utils.head());
        recipe.shape("GGG", "GHG", "GGG");
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('H', Material.PLAYER_HEAD);
        return recipe;
    }
}

