package us.polarismc.polarisuhc.managers.gui.ui;

import fr.mrmicky.fastinv.FastInv;
import me.putindeer.api.util.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraftSetting;

import java.util.function.Consumer;

public class CustomCraftsGUI extends FastInv {

    private final Main plugin;
    private final Player player;

    public CustomCraftsGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>Custom Crafts Settings</blue>")));
        this.plugin = plugin;
        this.player = player;

        int[] glass = new int[]{0, 1, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 34, 35, 36, 37, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        setItem(49, plugin.utils.goBack(), e -> new UHCGUI(player, plugin));

        addToggle(2, CustomCraftSetting.TOTEM_CRAFT, ib -> {});
        addToggle(3, CustomCraftSetting.SPECTRAL_ARROW_CRAFT, ib -> {});
        addToggle(4, CustomCraftSetting.GLISTERING_MELON_CRAFT, ib -> {});
        addToggle(5, CustomCraftSetting.TNT_MINECART_CRAFT, ib -> {});
        addToggle(6, CustomCraftSetting.LECTERN_CRAFT, ib -> {});
        addToggle(29, CustomCraftSetting.GOLDEN_HEAD_CRAFT, ib -> {});
        addToggle(30, CustomCraftSetting.TRIDENT_CRAFT, ib -> {});
        addToggle(31, CustomCraftSetting.ELYTRA_CRAFT, ib -> {});
        // addToggle(32, CustomCraftSetting.MACE_CRAFT, ib -> {});
        // addToggle(33, CustomCraftSetting.BREEZE_ROD_CRAFT, ib -> {});

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }

    private void addToggle(int slot, CustomCraftSetting setting, Consumer<ItemBuilder> iconConfig) {
        boolean enabled = plugin.uhc.getCrafts().isEnabled(setting);

        ItemBuilder ib = setting.buildIcon(plugin, enabled);
        iconConfig.accept(ib);

        setItem(slot, ib.build(), e -> {
            if (e.isRightClick()) {
                plugin.uhc.getCrafts().openRecipePreview(player, setting);
                return;
            }
            plugin.uhc.getCrafts().toggle(setting);
            new CustomCraftsGUI(player, plugin);
        });

        setItem(slot + 9, setting.buildToggleGlass(plugin, enabled).build(), e -> {
            if (e.isRightClick()) {
                plugin.uhc.getCrafts().openRecipePreview(player, setting);
                return;
            }
            plugin.uhc.getCrafts().toggle(setting);
            new CustomCraftsGUI(player, plugin);
        });
    }
}
