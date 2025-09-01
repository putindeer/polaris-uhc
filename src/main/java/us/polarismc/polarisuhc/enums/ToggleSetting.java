package us.polarismc.polarisuhc.enums;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ToggleSetting {
    ADVANCEMENTS(Material.WRITABLE_BOOK,"toggle.advancements",UHCManager::isAdvancements,UHCManager::setAdvancements),
    ANTIBURN(Material.LAVA_BUCKET,"toggle.antiburn",UHCManager::isAntiBurn,UHCManager::setAntiBurn),
    AUTOLS(Material.PLAYER_HEAD,"toggle.autols",UHCManager::isAutoLS,UHCManager::setAutoLS),
    BOOKSHELVES(Material.BOOKSHELF,"toggle.bookshelves",UHCManager::isBookshelves,UHCManager::setBookshelves),
    END(Material.END_STONE,"toggle.end",UHCManager::isEnd,UHCManager::setEnd),
    EXPLOSIVES(Material.TNT,"toggle.explosives",UHCManager::isExplosives,UHCManager::setExplosives),
    FIRE_ASPECT(Material.IRON_SWORD,"toggle.fire_aspect",UHCManager::isFireAspect,UHCManager::setFireAspect),
    FLAME(Material.BOW,"toggle.flame",UHCManager::isFlame,UHCManager::setFlame),
    HORSES(Material.HORSE_SPAWN_EGG,"toggle.horses",UHCManager::isHorses,UHCManager::setHorses),
    MOBS(Material.ZOMBIE_SPAWN_EGG,"toggle.mobs",UHCManager::isMobs,UHCManager::setMobs),
    NETHER(Material.CRIMSON_NYLIUM,"toggle.nether",UHCManager::isNether,UHCManager::setNether),
    NOTCH(Material.ENCHANTED_GOLDEN_APPLE,"toggle.notch",UHCManager::isNotch,UHCManager::setNotch),
    STARTER_BOOKS(Material.BOOK,"toggle.starter_books",UHCManager::isStarterBooks,UHCManager::setStarterBooks),
    STATS(Material.KNOWLEDGE_BOOK,"toggle.stats",UHCManager::isStats,UHCManager::setStats),
    NERFED_STRENGTH(Material.POTION,"toggle.nerfed_strength",UHCManager::isNerfedStrength,UHCManager::setNerfedStrength),
    TRADES(Material.VILLAGER_SPAWN_EGG,"toggle.trades",UHCManager::isTrades,UHCManager::setTrades),
    TP_BORDER(Material.COMPASS,"toggle.tp_border",UHCManager::isTpBorder,UHCManager::setTpBorder),
    TOTEM_CRAFT(Material.TOTEM_OF_UNDYING,"customcrafts.totem",UHCManager::isTotemCraft,UHCManager::setTotemCraft),
    MACE_CRAFT(Material.MACE,"customcrafts.mace",UHCManager::isMaceCraft,UHCManager::setMaceCraft),
    BREEZE_ROD_CRAFT(Material.BREEZE_ROD,"customcrafts.breeze_rod",UHCManager::isBreezeRodCraft,UHCManager::setBreezeRodCraft),
    TRIDENT_CRAFT(Material.TRIDENT,"customcrafts.trident",UHCManager::isTridentCraft,UHCManager::setTridentCraft),
    ELYTRA_CRAFT(Material.ELYTRA,"customcrafts.elytra",UHCManager::isElytraCraft,UHCManager::setElytraCraft),
    GOLDEN_HEAD_CRAFT(Material.GOLDEN_APPLE,"customcrafts.golden_head",UHCManager::isGoldenHeadCraft,UHCManager::setGoldenHeadCraft),
    SPECTRAL_ARROW_CRAFT(Material.SPECTRAL_ARROW,"customcrafts.spectral_arrow",UHCManager::isSpectralArrowCraft,UHCManager::setSpectralArrowCraft),
    GLISTERING_MELON_CRAFT(Material.GLISTERING_MELON_SLICE,"customcrafts.glistering_melon",UHCManager::isGlisteringMelonCraft,UHCManager::setGlisteringMelonCraft),
    TNT_MINECART_CRAFT(Material.TNT_MINECART,"customcrafts.tnt_minecart",UHCManager::isTntMinecartCraft,UHCManager::setTntMinecartCraft),
    LECTERN_CRAFT(Material.LECTERN,"customcrafts.lectern",UHCManager::isLecternCraft,UHCManager::setLecternCraft);

    private final Material icon;
    private final String key;
    private final Function<UHCManager, Boolean> getter;
    private final BiConsumer<UHCManager, Boolean> setter;

    ToggleSetting(Material icon, String key, Function<UHCManager, Boolean> getter, BiConsumer<UHCManager, Boolean> setter) {
        this.icon = icon;
        this.key = key;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean get(UHCManager manager) {
        return getter.apply(manager);
    }

    public void set(UHCManager manager, boolean value) {
        setter.accept(manager, value);
    }

    public void toggle(UHCManager manager) {
        set(manager, !get(manager));
    }

    public ItemBuilder buildIcon(Main plugin) {
        boolean enabled = get(plugin.uhc);
        String section = key.startsWith("customcrafts.") ? "customcrafts" : "toggle";
        String loreKey = section + "." + (enabled ? "to_off" : "to_on");
        return plugin.utils.ib(icon).customName((enabled ? "<green>" : "<red>") + "[lang]" + key + "[/lang]").lore("[lang]" + loreKey + "[/lang]");
    }

    public ItemBuilder buildToggleGlass(Main plugin) {
        return plugin.utils.ib(get(plugin.uhc) ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).hideTooltip();
    }
}

