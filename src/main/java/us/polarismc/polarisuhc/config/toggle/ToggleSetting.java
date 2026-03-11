package us.polarismc.polarisuhc.config.toggle;

import lombok.Getter;
import org.bukkit.Material;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.toggle.handlers.*;


public enum ToggleSetting {
    ADVANCEMENTS(Advancements.class),
    ANTIBURN(AntiBurn.class),
    AUTOLS(AutoLateScatter.class),
    AUTO_MINING_WARN(AutoMiningWarn.class),
    BOOKSHELVES(Bookshelves.class),
    END(End.class),
    EXPLOSIVES(Explosives.class),
    FIRE_ASPECT(FireAspect.class),
    FLAME(Flame.class),
    HORSES(Horses.class),
    MOBS(Mobs.class),
    NERFED_STRENGTH(NerfedStrength.class),
    NETHER(Nether.class),
    NOTCH(Notch.class),
    STARTER_BOOKS(StarterBooks.class),
    STATS(Stats.class),
    TRADES(Trades.class);

    private final Class<? extends ToggleHandler> handlerClass;
    @Getter private final ToggleInfo info;

    ToggleSetting(Class<? extends ToggleHandler> handlerClass) {
        this.handlerClass = handlerClass;
        this.info = handlerClass.getAnnotation(ToggleInfo.class);
        if (info == null) {
            throw new IllegalStateException("Missing @ToggleInfo on " + handlerClass.getSimpleName());
        }
    }

    public ToggleHandler create() {
        try {
            return handlerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Invalid handler class: " + handlerClass.getName(), e);
        }
    }

    public ItemBuilder buildIcon(Main plugin, boolean enabled) {
        String lore = enabled
                ? "Click to toggle <red>OFF</red> this setting."
                : "Click to toggle <green>ON</green> this setting.";
        return plugin.utils.ib(info.icon())
                .customName((enabled ? "<green>" : "<red>") + info.displayName())
                .lore(lore);
    }

    public ItemBuilder buildToggleGlass(Main plugin, boolean enabled) {
        return plugin.utils.ib(enabled ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).hideTooltip();
    }
}