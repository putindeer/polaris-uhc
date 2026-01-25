package us.polarismc.polarisuhc.config.customcrafts;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.*;

@Getter
public abstract class CustomCraft {
    protected final Main plugin;
    protected final NamespacedKey key;
    protected final Recipe recipe;
    @Getter private boolean enabled = false;
    private final Map<NamespacedKey, List<Recipe>> vanillaBackup = new HashMap<>();

    protected CustomCraft(Main plugin, NamespacedKey key, Recipe recipe) {
        this.plugin = plugin;
        this.key = key;
        this.recipe = recipe;
    }

    public final void enable() {
        enabled = true;
        register();
    }

    public final void disable() {
        enabled = false;
        unregister();
    }

    public final void removeVanillaRecipe(NamespacedKey key) {
        List<Recipe> recipes = getRecipesByKey(key);
        vanillaBackup.put(key, recipes);
    }

    private void register() {
        Bukkit.addRecipe(recipe);
        removeRecipes();
    }

    private void unregister() {
        Bukkit.removeRecipe(key);
        restoreRecipes();
    }

    private void removeRecipes() {
        vanillaBackup.forEach((namespacedKey, recipes) -> Bukkit.removeRecipe(namespacedKey));
    }

    private void restoreRecipes() {
        vanillaBackup.forEach(((namespacedKey, recipes) -> recipes.forEach(Bukkit::addRecipe)));
    }

    private List<Recipe> getRecipesByKey(NamespacedKey key) {
        List<Recipe> list = new ArrayList<>();

        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof Keyed keyed && keyed.getKey().equals(key)) {
                list.add(recipe);
            }
        }

        return list;
    }

    public final void openPreview(Player player, String title) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, plugin.utils.chat(title));

        inventory.setItem(0, safeClone(recipe.getResult()));

        if (recipe instanceof ShapedRecipe shaped) fillShaped(inventory, shaped);
        else if (recipe instanceof ShapelessRecipe shapeless) fillShapeless(inventory, shapeless);

        player.openInventory(inventory);
    }

    private void fillShaped(Inventory inv, ShapedRecipe shaped) {
        for (int i = 1; i <= 9; i++) inv.setItem(i, null);

        String[] shape = shaped.getShape();
        Map<Character, RecipeChoice> choiceMap = shaped.getChoiceMap();

        int slot = 1;
        for (int row = 0; row < 3; row++) {
            String line = row < shape.length ? shape[row] : "";
            for (int col = 0; col < 3; col++) {
                char character = col < line.length() ? line.charAt(col) : ' ';
                inv.setItem(slot++, character == ' ' ? null : choiceToIcon(choiceMap.get(character)));
            }
        }
    }

    private void fillShapeless(Inventory inv, ShapelessRecipe shapeless) {
        for (int i = 1; i <= 9; i++) inv.setItem(i, null);

        int slot = 1;
        for (RecipeChoice choice : shapeless.getChoiceList()) {
            if (slot > 9) break;
            inv.setItem(slot++, choiceToIcon(choice));
        }
    }

    private @Nullable ItemStack choiceToIcon(@Nullable RecipeChoice choice) {
        switch (choice) {
            case null -> {
                return null;
            }
            case RecipeChoice.ExactChoice exact -> {
                List<ItemStack> list = exact.getChoices();
                return list.isEmpty() ? null : safeClone(list.getFirst());
            }
            case RecipeChoice.MaterialChoice mats -> {
                List<Material> list = mats.getChoices();
                return list.isEmpty() ? null : new ItemStack(list.getFirst());
            }
            default -> {
            }
        }

        return null;
    }

    private ItemStack safeClone(ItemStack item) {
        return item == null ? new ItemStack(Material.AIR) : item.clone();
    }
}
