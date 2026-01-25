package us.polarismc.polarisuhc.config.customcrafts;

import me.putindeer.api.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.crafts.Totem;
import us.polarismc.polarisuhc.config.customcrafts.crafts.Trident;
import us.polarismc.polarisuhc.config.customcrafts.crafts.Elytra;
import us.polarismc.polarisuhc.config.customcrafts.crafts.GoldenHead;
import us.polarismc.polarisuhc.config.customcrafts.crafts.SpectralArrows;
import us.polarismc.polarisuhc.config.customcrafts.crafts.GlisteringMelon;
import us.polarismc.polarisuhc.config.customcrafts.crafts.TntMinecart;
import us.polarismc.polarisuhc.config.customcrafts.crafts.Lectern;

import java.util.function.Function;

public enum CustomCraftSetting {
    TOTEM_CRAFT(Totem.class, Totem::new),
    TRIDENT_CRAFT(Trident.class, Trident::new),
    ELYTRA_CRAFT(Elytra.class, Elytra::new),
    GOLDEN_HEAD_CRAFT(GoldenHead.class, GoldenHead::new),
    SPECTRAL_ARROW_CRAFT(SpectralArrows.class, SpectralArrows::new),
    GLISTERING_MELON_CRAFT(GlisteringMelon.class, GlisteringMelon::new),
    TNT_MINECART_CRAFT(TntMinecart.class, TntMinecart::new),
    LECTERN_CRAFT(Lectern.class, Lectern::new),
    // MACE_CRAFT(Mace.class, Mace::new),
    // BREEZE_ROD_CRAFT(BreezeRod.class, BreezeRod::new),
    ;

    private final Function<Main, ? extends CustomCraft> factory;
    private final CraftInfo info;

    CustomCraftSetting(Class<? extends CustomCraft> craftClass, Function<Main, ? extends CustomCraft> factory) {
        this.factory = factory;

        CraftInfo found = craftClass.getAnnotation(CraftInfo.class);
        if (found == null) {
            throw new IllegalStateException("Missing @CraftInfo on craft class: " + craftClass.getName());
        }
        this.info = found;
    }

    public @NotNull CustomCraft create(Main plugin) {
        return factory.apply(plugin);
    }

    private final String path = "toggle.customcrafts.";

    public boolean getDefault(Main plugin) {
        return plugin.getConfig().getBoolean(info.configPath());
    }

    public CraftInfo info() {
        return info;
    }

    public ItemBuilder buildIcon(Main plugin, boolean enabled) {
        String lore = enabled
                ? "Click to toggle <red>OFF</red> this crafting recipe."
                : "Click to toggle <green>ON</green> this crafting recipe.";

        return plugin.utils.ib(info.icon()).lore(lore)
                .customName((enabled ? "<green>" : "<red>") + info.displayName());
    }

    public ItemBuilder buildToggleGlass(Main plugin, boolean enabled) {
        Material glass = enabled ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        return plugin.utils.ib(glass).hideTooltip();
    }
}
