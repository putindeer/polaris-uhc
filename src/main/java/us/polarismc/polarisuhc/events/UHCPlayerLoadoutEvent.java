package us.polarismc.polarisuhc.events;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public abstract class UHCPlayerLoadoutEvent extends UHCEvent {
    private final Main plugin;
    private final Player player;
    private final UHCPlayer uhcPlayer;
    private final List<ItemStack> items = new ArrayList<>();
    private final List<PotionEffect> potionEffects = new ArrayList<>();

    protected UHCPlayerLoadoutEvent(UHCPlayer uhcPlayer, Main plugin) {
        this.uhcPlayer = uhcPlayer;
        this.player = uhcPlayer.getPlayer();
        this.plugin = plugin;
        items.add(new ItemStack(Material.COOKED_BEEF, 2));
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

    public void addItems(Collection<ItemStack> items) {
        this.items.addAll(items);
    }

    public void removeItem(Material material) {
        items.removeIf(item -> item != null && item.getType() == material);
    }

    public void removeItems(Collection<Material> materials) {
        items.removeIf(item -> item != null && materials.contains(item.getType()));
    }

    public void replaceItem(Material material, ItemStack replacement) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).getType() == material) {
                items.set(i, replacement);
            }
        }
    }

    public void replaceItems(Map<Material, ItemStack> replacements) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            if (item != null && replacements.containsKey(item.getType())) {
                items.set(i, replacements.get(item.getType()));
            }
        }
    }

    public void addPotionEffect(PotionEffect effect) {
        potionEffects.add(effect);
    }

    public void addPotionEffects(Collection<PotionEffect> effects) {
        potionEffects.addAll(effects);
    }

    public void give() {
        if (player == null) return;
        plugin.utils.giveOrDrop(player, items);
        player.addPotionEffects(potionEffects);
    }
}

