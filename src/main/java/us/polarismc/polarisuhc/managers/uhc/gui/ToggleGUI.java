package us.polarismc.polarisuhc.managers.uhc.gui;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;
import us.polarismc.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.enums.ToggleSetting;

import java.util.function.Consumer;

public class ToggleGUI extends FastInv {
    private final Main plugin;
    private final Player player;

    public ToggleGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>[lang]toggle.gui_title[/lang]</blue>", player)));
        this.plugin = plugin;
        this.player = player;

        int[] glass = new int[]{0, 1, 7, 8, 9, 10, 16, 17, 19, 25, 28, 34, 36, 37, 40, 43, 44, 45, 46, 52, 53};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        setItem(49, plugin.uhc.goBack(player), e -> new UHCGUI(player, plugin));

        addToggle(2, ToggleSetting.BOOKSHELVES, ib -> {});
        addToggle(3, ToggleSetting.AUTOLS, ib -> ib.owner(player));
        addToggle(4, ToggleSetting.ADVANCEMENTS, ib -> {});
        addToggle(5, ToggleSetting.STATS, ib -> {});
        addToggle(6, ToggleSetting.HORSES, ib -> {});
        addToggle(18, ToggleSetting.STARTER_BOOKS, ib -> {});
        addToggle(20, ToggleSetting.MOBS, ib -> {});
        addToggle(21, ToggleSetting.TRADES, ib -> {});
        addToggle(22, ToggleSetting.EXPLOSIVES, ib -> {});
        addToggle(23, ToggleSetting.FLAME, ib -> {});
        addToggle(24, ToggleSetting.FIRE_ASPECT, ib -> {});
        addToggle(26, ToggleSetting.ANTIBURN, ib -> {});
        addToggle(38, ToggleSetting.END, ib -> {});
        addToggle(39, ToggleSetting.NERFED_STRENGTH, ib -> ib.potionType(PotionType.STRENGTH).hidePotionEffects());
        addToggle(41, ToggleSetting.NOTCH, ib -> {});
        addToggle(42, ToggleSetting.NETHER, ib -> {});

        open(player);
    }

    private void addToggle(int slot, ToggleSetting setting, Consumer<ItemBuilder> iconConfig) {
        ItemBuilder ib = setting.buildIcon(plugin, player);
        iconConfig.accept(ib);
        setItem(slot, ib.build(), e -> plugin.uhc.toggleSetting(player, setting, ToggleGUI::new));
        setItem(slot + 9, setting.buildToggleGlass(plugin, player).build(), e -> plugin.uhc.toggleSetting(player, setting, ToggleGUI::new));
    }
}
