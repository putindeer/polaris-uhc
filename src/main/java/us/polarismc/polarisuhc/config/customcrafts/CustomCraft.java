package us.polarismc.polarisuhc.config.customcrafts;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.*;

@Getter
public abstract class CustomCraft {
    protected final Main plugin = Main.getInstance();
    protected final NamespacedKey key;
    protected final Recipe recipe;
    @Getter private boolean enabled = false;
    private final Map<NamespacedKey, List<Recipe>> vanillaBackup = new HashMap<>();
    private final Set<NamespacedKey> overriddenVanilla = new HashSet<>();

    protected CustomCraft() {
        CraftInfo info = getClass().getAnnotation(CraftInfo.class);
        if (info == null) {
            throw new IllegalStateException("Missing @CraftInfo on " + getClass().getSimpleName());
        }

        this.key = new NamespacedKey(plugin, info.id());

        for (String recipeId : info.removedVanillaRecipes()) {
            NamespacedKey vanillaKey = NamespacedKey.minecraft(recipeId);
            removeVanillaRecipe(vanillaKey);
        }

        this.recipe = buildRecipe();
        setDefault(info);
    }

    protected abstract Recipe buildRecipe();

    public final void enable() {
        if (enabled) return;
        enabled = true;
        register();
    }

    public final void disable() {
        if (!enabled) return;
        enabled = false;
        unregister();
    }

    private void setDefault(CraftInfo info) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle.customcrafts");
        if (config == null) return;
        enabled = config.getBoolean(info.id());
    }

    private void removeVanillaRecipe(NamespacedKey key) {
        List<Recipe> recipes = getRecipesByKey(key);
        vanillaBackup.put(key, recipes);
    }

    private void register() {
        removeRecipes();
        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(recipe);
    }

    private void unregister() {
        Bukkit.removeRecipe(key);
        restoreRecipes();
    }

    private void removeRecipes() {
        vanillaBackup.forEach((namespacedKey, recipes) -> {
            if (Bukkit.getRecipe(namespacedKey) != null) {
                Bukkit.removeRecipe(namespacedKey);
                overriddenVanilla.add(namespacedKey);
            }
        });
    }

    private void restoreRecipes() {
        vanillaBackup.forEach((namespacedKey, recipes) -> {
            if (!overriddenVanilla.remove(namespacedKey)) return;

            Bukkit.removeRecipe(namespacedKey);
            recipes.forEach(Bukkit::addRecipe);
        });
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
