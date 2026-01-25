package us.polarismc.polarisuhc.config.customcrafts.crafts;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CraftInfo;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraft;

@CraftInfo(configPath = "glistering_melon", icon = Material.GLISTERING_MELON_SLICE, displayName = "Glistering Melon",
        lore = { "Overrides vanilla glistering melon recipe." })
public class GlisteringMelon extends CustomCraft {
    public GlisteringMelon(Main plugin) {
        super(plugin, new NamespacedKey(plugin, "melon"), build(plugin));
        removeVanillaRecipe(NamespacedKey.minecraft("glistering_melon_slice"));
    }

    private static ShapedRecipe build(Main plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "melon");
        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(Material.GLISTERING_MELON_SLICE));
        recipe.shape("...", ".,.", "...");
        recipe.setIngredient('.', Material.GOLD_INGOT);
        recipe.setIngredient(',', Material.MELON_SLICE);
        return recipe;
    }
}
