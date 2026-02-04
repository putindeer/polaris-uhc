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

