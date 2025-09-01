package us.polarismc.polarisuhc.managers.uhc.gui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.enums.ToggleSetting;

import java.util.function.Consumer;

public class CustomCraftsGUI extends FastInv {
    private final Main plugin;
    private final Player player;

    public CustomCraftsGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>[lang]customcrafts.gui_title[/lang]</blue>")));
        this.plugin = plugin;
        this.player = player;

        int[] glass = new int[]{0, 1, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 34, 35, 36, 37, 43, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        setItem(49, plugin.uhc.goBack(player), e -> new UHCGUI(player, plugin));

        addToggle(2, ToggleSetting.TOTEM_CRAFT, ib -> {});
        addToggle(3, ToggleSetting.MACE_CRAFT, ib -> {});
        addToggle(4, ToggleSetting.BREEZE_ROD_CRAFT, ib -> {});
        addToggle(5, ToggleSetting.TRIDENT_CRAFT, ib -> {});
        addToggle(6, ToggleSetting.ELYTRA_CRAFT, ib -> {});
        addToggle(29, ToggleSetting.GOLDEN_HEAD_CRAFT, ib -> {});
        addToggle(30, ToggleSetting.SPECTRAL_ARROW_CRAFT, ib -> {});
        addToggle(31, ToggleSetting.GLISTERING_MELON_CRAFT, ib -> {});
        addToggle(32, ToggleSetting.TNT_MINECART_CRAFT, ib -> {});
        addToggle(33, ToggleSetting.LECTERN_CRAFT, ib -> {});

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }

    private void addToggle(int slot, ToggleSetting setting, Consumer<ItemBuilder> iconConfig) {
        ItemBuilder ib = setting.buildIcon(plugin);
        iconConfig.accept(ib);
        setItem(slot, ib.build(), e -> plugin.uhc.toggleSetting(player, setting, CustomCraftsGUI::new));
        setItem(slot + 9, setting.buildToggleGlass(plugin).build(), e -> plugin.uhc.toggleSetting(player, setting, CustomCraftsGUI::new));
    }
}