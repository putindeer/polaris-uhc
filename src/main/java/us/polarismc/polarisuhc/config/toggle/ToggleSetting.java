package us.polarismc.polarisuhc.config.toggle;

import org.bukkit.Material;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ToggleSetting {
    ADVANCEMENTS(Material.WRITABLE_BOOK, "Advancements", ToggleManager::isAdvancements, ToggleManager::setAdvancements),
    ANTIBURN(Material.LAVA_BUCKET, "Anti Burn", ToggleManager::isAntiBurn, ToggleManager::setAntiBurn),
    AUTOLS(Material.PLAYER_HEAD, "Auto Late Scatter", ToggleManager::isAutoLS, ToggleManager::setAutoLS),
    BOOKSHELVES(Material.BOOKSHELF, "Bookshelves", ToggleManager::isBookshelves, ToggleManager::setBookshelves),
    END(Material.END_STONE, "End", ToggleManager::isEnd, ToggleManager::setEnd),
    EXPLOSIVES(Material.TNT, "Explosives", ToggleManager::isExplosives, ToggleManager::setExplosives),
    FIRE_ASPECT(Material.IRON_SWORD, "Fire Aspect", ToggleManager::isFireAspect, ToggleManager::setFireAspect),
    FLAME(Material.BOW, "Flame", ToggleManager::isFlame, ToggleManager::setFlame),
    HORSES(Material.HORSE_SPAWN_EGG, "Horses", ToggleManager::isHorses, ToggleManager::setHorses),
    MOBS(Material.ZOMBIE_SPAWN_EGG, "Mobs", ToggleManager::isMobs, ToggleManager::setMobs),
    NETHER(Material.CRIMSON_NYLIUM, "Nether", ToggleManager::isNether, ToggleManager::setNether),
    NOTCH(Material.ENCHANTED_GOLDEN_APPLE, "Notch Apples", ToggleManager::isNotch, ToggleManager::setNotch),
    STARTER_BOOKS(Material.BOOK, "Starter Books", ToggleManager::isStarterBooks, ToggleManager::setStarterBooks),
    STATS(Material.KNOWLEDGE_BOOK, "Stats", ToggleManager::isStats, ToggleManager::setStats),
    NERFED_STRENGTH(Material.POTION, "Nerfed Strength", ToggleManager::isNerfedStrength, ToggleManager::setNerfedStrength),
    TRADES(Material.VILLAGER_SPAWN_EGG, "Trades", ToggleManager::isTrades, ToggleManager::setTrades);

    private final Material icon;
    private final String name;
    private final Function<ToggleManager, Boolean> getter;
    private final BiConsumer<ToggleManager, Boolean> setter;

    ToggleSetting(Material icon, String name, Function<ToggleManager, Boolean> getter, BiConsumer<ToggleManager, Boolean> setter) {
        this.icon = icon;
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean get(Main plugin) {
        return getter.apply(plugin.uhc.getToggle());
    }

    public void set(Main plugin, boolean value) {
        setter.accept(plugin.uhc.getToggle(), value);
    }

    public void toggle(Main plugin) {
        set(plugin, !get(plugin));
    }

    public ItemBuilder buildIcon(Main plugin) {
        boolean enabled = get(plugin);
        String lore = enabled
                ? "Click to toggle <red>OFF</red> this setting."
                : "Click to toggle <green>ON</green> this setting.";
        return plugin.utils.ib(icon).customName((enabled ? "<green>" : "<red>") + name).lore(lore);
    }

    public ItemBuilder buildToggleGlass(Main plugin) {
        return plugin.utils.ib(get(plugin) ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).hideTooltip();
    }
}