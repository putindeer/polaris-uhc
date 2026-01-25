package us.polarismc.polarisuhc.config.customcrafts;

import lombok.Getter;
import me.putindeer.api.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.crafts.*;

public enum CustomCraftSetting {
    TOTEM_CRAFT(Totem.class),
    TRIDENT_CRAFT(Trident.class),
    ELYTRA_CRAFT(Elytra.class),
    GOLDEN_HEAD_CRAFT(GoldenHead.class),
    SPECTRAL_ARROW_CRAFT(SpectralArrows.class),
    GLISTERING_MELON_CRAFT(GlisteringMelon.class),
    TNT_MINECART_CRAFT(TntMinecart.class),
    LECTERN_CRAFT(Lectern.class),
    MACE_CRAFT(Mace.class),
    BREEZE_ROD_CRAFT(BreezeRod.class);

    private final Class<? extends CustomCraft> craftClass;
    @Getter private final CraftInfo info;

    CustomCraftSetting(Class<? extends CustomCraft> craftClass) {
        this.craftClass = craftClass;

        CraftInfo found = craftClass.getAnnotation(CraftInfo.class);
        if (found == null) {
            throw new IllegalStateException("Missing @CraftInfo on craft class: " + craftClass.getName());
        }
        this.info = found;
    }

    public @NotNull CustomCraft create() {
        try {
            return craftClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Invalid craft class: " + craftClass.getName() + " (needs a public no-args constructor)", e);
        }
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
