package us.polarismc.polarisuhc.managers.uhc.gui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.enums.ToggleSetting;
import us.polarismc.polarisuhc.managers.border.BorderHandler;


public class BorderWorldGUI extends FastInv {
    public BorderWorldGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>[lang]bandwgui.gui_title[/lang]", player)));

        BorderHandler overworld = new BorderHandler(plugin, "border.guidefaultlist.overworld", player, plugin.uhc::getBorder, plugin.uhc::setBorder, BorderWorldGUI::new);
        BorderHandler nether = new BorderHandler(plugin, "border.guidefaultlist.nether", player, plugin.uhc::getNetherBorder, plugin.uhc::setNetherBorder, BorderWorldGUI::new);

        int[] glass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 16, 17, 18, 19, 20, 25, 26, 27, 29, 34, 35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 50, 51, 52};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());

        // Create Worlds & TP Border

        setItem(10, plugin.utils.ib(plugin.uhc.areWorldsCreated() ? Material.GRASS_BLOCK : Material.BEDROCK, player).name("<blue>[lang]bandwgui.create_worlds[/lang]")
                .lore(plugin.uhc.areWorldsCreated() ? "<green>[lang]bandwgui.already_created[/lang]" : "[lang]bandwgui.create_needed_worlds[/lang]").build(), e -> plugin.uhc.createWorlds(player));
        setItem(28, plugin.utils.ib(Material.CHORUS_FRUIT, player).name("<light_purple>[lang]bandwgui.tpborder[/lang]")
                        .lore("[lang]bandwgui.tpborder_desc[/lang]").build(),
                e -> plugin.uhc.toggleSetting(player, ToggleSetting.TP_BORDER, BorderWorldGUI::new));
        setItem(37, plugin.utils.ib(plugin.uhc.isTpBorder() ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).name("").hideTooltip().build(),
                e -> plugin.uhc.toggleSetting(player, ToggleSetting.TP_BORDER, BorderWorldGUI::new));

        // Overworld Border
        int border = plugin.uhc.getBorder();
        setItem(12, plugin.utils.ib(Material.GRASS_BLOCK, player).name("<green>[lang]bandwgui.overworld_border[/lang]")
                .lore("[lang]bandwgui.overworld_border_desc[/lang]").build());
        setItem(13, plugin.utils.ib(overworld.isMinimum() ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, player)
                        .name(overworld.isMinimum() ? "<dark_gray>-" : "<red>-")
                        .lore("[lang]bandwgui.current_border[/lang] <red>" + formatBorder(border))
                        .addLoreIf(!overworld.isMinimum(), "[lang]bandwgui.decrease_border[/lang] <red>" + formatBorder(overworld.closestLowestValue()) + "</red>.").build(),
                e -> overworld.decrease());
        setItem(14, plugin.utils.ib(Material.PAPER, player)
                        .name("<yellow>" + formatBorder(border))
                        .lore("[lang]common.custom_value[/lang]").build(),
                e -> plugin.uhc.openIntInputSign(player, plugin.uhc::getBorder, plugin.uhc::setBorder, BorderWorldGUI::new));
        setItem(15, plugin.utils.ib(overworld.isMaximum() ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE, player)
                        .name(overworld.isMaximum() ? "<dark_gray>+" : "<green>+")
                        .lore("[lang]bandwgui.current_border[/lang] <green>" + formatBorder(border))
                        .addLoreIf(!overworld.isMaximum(), "[lang]bandwgui.increase_border[/lang] <green>" + formatBorder(overworld.closestHighestValue()) + "</green>.").build(),
                e -> overworld.increase());

        // Nether Border
        int netherBorder = plugin.uhc.getNetherBorder();
        setItem(21, plugin.utils.ib(Material.CRIMSON_NYLIUM, player).name("<red>[lang]bandwgui.nether_border[/lang]")
                .lore("[lang]bandwgui.nether_border_desc[/lang]").build());
        setItem(22, plugin.utils.ib(nether.isMinimum() ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, player)
                        .name(nether.isMinimum() ? "<dark_gray>-" : "<red>-")
                        .lore("[lang]bandwgui.current_border[/lang] <red>" + formatBorder(netherBorder))
                        .addLoreIf(!nether.isMinimum(), "[lang]bandwgui.decrease_border[/lang] <red>" + formatBorder(nether.closestLowestValue()) + "</red>.").build(),
                e -> nether.decrease());
        setItem(23, plugin.utils.ib(Material.PAPER, player)
                        .name("<yellow>" + formatBorder(netherBorder))
                        .lore("[lang]common.custom_value[/lang]").build(),
                e -> plugin.uhc.openIntInputSign(player, plugin.uhc::getNetherBorder, plugin.uhc::setNetherBorder, BorderWorldGUI::new));
        setItem(24, plugin.utils.ib(nether.isMaximum() ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE, player)
                        .name(nether.isMaximum() ? "<dark_gray>+" : "<green>+")
                        .lore("[lang]bandwgui.current_border[/lang] <green>" + formatBorder(netherBorder))
                        .addLoreIf(!nether.isMaximum(), "[lang]bandwgui.increase_border[/lang] <green>" + formatBorder(nether.closestHighestValue()) + "</green>.").build(),
                e -> nether.increase());

        if (plugin.uhc.isTpBorder()) {
            // Time between TP Borders
            int timer = plugin.uhc.getBorderTimer();
            setItem(30, plugin.utils.ib(Material.CLOCK, player).name("<yellow>[lang]bandwgui.time[/lang]")
                    .lore("[lang]bandwgui.time_desc[/lang]").build());
            setItem(31, plugin.utils.ib(timer <= 1 ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE, player)
                    .name(timer <= 1 ? "<dark_gray>-" : "<red>-")
                    .lore("[lang]bandwgui.current_timer[/lang] <red>" + timer + (timer == 1 ? " [lang]terms.minute[/lang]" : " [lang]terms.minutes[/lang]"))
                    .addLoreIf(timer > 1, "[lang]common.decrease_timer_1m[/lang]",
                            "[lang]common.decrease_timer_5m[/lang]").build(),
                    e -> plugin.uhc.decrease(e, plugin.uhc::getBorderTimer, plugin.uhc::setBorderTimer, BorderWorldGUI::new));
            setItem(32, plugin.utils.ib(Material.PAPER, player)
                            .name("<yellow>" + timer + (timer == 1 ? " [lang]terms.minute[/lang]" : " [lang]terms.minutes[/lang]"))
                            .lore("[lang]common.custom_value[/lang]").build(),
                    e -> plugin.uhc.openIntInputSign(player, plugin.uhc::getBorderTimer, plugin.uhc::setBorderTimer, BorderWorldGUI::new));
            setItem(33, plugin.utils.ib(Material.LIME_STAINED_GLASS_PANE, player)
                    .name("<green>+").lore("[lang]bandwgui.current_timer[/lang] <green>" + timer + (timer == 1 ? " [lang]terms.minute[/lang]" : " [lang]terms.minutes[/lang]"),
                            "[lang]common.increase_timer_1m[/lang]",
                            "[lang]common.increase_timer_5m[/lang]").build(),
                    e -> plugin.uhc.increase(e, plugin.uhc::getBorderTimer, plugin.uhc::setBorderTimer, BorderWorldGUI::new));
        } else {
            // Border Speed
            double speed = plugin.uhc.getBorderSpeed();
        }

        //TODO - AÑADIR SONIDOS DE ERROR(?) cuando tratas de interactuar con un cristal gris

        setItem(49, plugin.uhc.goBack(player), e -> new UHCGUI(player, plugin));

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }

    public String formatBorder(int value) {
        return value / 2 + "x" + value / 2;
    }
}
