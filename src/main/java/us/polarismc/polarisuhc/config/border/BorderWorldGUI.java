package us.polarismc.polarisuhc.config.border;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCGUI;

public class BorderWorldGUI extends FastInv {
    private final Main plugin;
    private final Player player;
    public BorderWorldGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>World and Border Settings")));

        this.player = player;
        this.plugin = plugin;

        BorderGUIHandler overworld = new BorderGUIHandler(plugin, "border.gui-default-list.overworld", player, plugin.uhc.border::getBorder, plugin.uhc.border::setBorder, BorderWorldGUI::new);
        BorderGUIHandler nether = new BorderGUIHandler(plugin, "border.gui-default-list.nether", player, plugin.uhc.border::getNetherBorder, plugin.uhc.border::setNetherBorder, BorderWorldGUI::new);

        int[] glass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 16, 17, 18, 19, 20, 25, 26, 27, 29, 34, 35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 50, 51, 52};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());

        // Create Worlds & TP Border
        setItem(10, plugin.utils.ib(plugin.uhc.world.areWorldsCreated() ? Material.GRASS_BLOCK : Material.BEDROCK).name("<blue>Create Worlds")
                .lore(plugin.uhc.world.areWorldsCreated() ? "<green>All worlds are already created." : "Click this to create all worlds needed.").build(), e -> plugin.uhc.world.createWorlds(player));
        setItem(28, plugin.utils.ib(Material.CHORUS_FRUIT).name("<light_purple>TP Border")
                        .lore("Toggle tp-border between <red>OFF</red> and <green>ON</green>.").build(),
                e -> toggleTPBorder());
        setItem(37, plugin.utils.ib(plugin.uhc.border.isTpBorder() ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).name("").hideTooltip().build(),
                e -> toggleTPBorder());

        // Overworld Border
        int border = plugin.uhc.border.getBorder();
        setItem(12, plugin.utils.ib(Material.GRASS_BLOCK).name("<green>Overworld Border")
                .lore("Here you can adjust the overworld border.").build());
        setItem(13, plugin.utils.ib(overworld.isMinimum() ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                        .name(overworld.isMinimum() ? "<dark_gray>-" : "<red>-")
                        .lore("Current border: <red>" + formatBorder(border))
                        .addLoreIf(!overworld.isMinimum(), "Click this to decrease this border to <red>" + formatBorder(overworld.closestLowestValue()) + "</red>.").build(),
                e -> overworld.decrease());
        setItem(14, plugin.utils.ib(Material.PAPER)
                        .name("<yellow>" + formatBorder(border))
                        .lore("Click this to set a custom value.").build(),
                e -> plugin.uhc.ui.openIntInputSign(player, plugin.uhc.border::getBorder, plugin.uhc.border::setBorder, BorderWorldGUI::new));
        setItem(15, plugin.utils.ib(overworld.isMaximum() ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE)
                        .name(overworld.isMaximum() ? "<dark_gray>+" : "<green>+")
                        .lore("Current border: <green>" + formatBorder(border))
                        .addLoreIf(!overworld.isMaximum(), "Click this to increase this border to <green>" + formatBorder(overworld.closestHighestValue()) + "</green>.").build(),
                e -> overworld.increase());

        // Nether Border
        int netherBorder = plugin.uhc.border.getNetherBorder();
        setItem(21, plugin.utils.ib(Material.CRIMSON_NYLIUM).name("<red>Nether Border")
                .lore("Here you can adjust the nether border.").build());
        setItem(22, plugin.utils.ib(nether.isMinimum() ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                        .name(nether.isMinimum() ? "<dark_gray>-" : "<red>-")
                        .lore("Current border: <red>" + formatBorder(netherBorder))
                        .addLoreIf(!nether.isMinimum(), "Click this to decrease this border to <red>" + formatBorder(nether.closestLowestValue()) + "</red>.").build(),
                e -> nether.decrease());
        setItem(23, plugin.utils.ib(Material.PAPER)
                        .name("<yellow>" + formatBorder(netherBorder))
                        .lore("Click this to set a custom value.").build(),
                e -> plugin.uhc.ui.openIntInputSign(player, plugin.uhc.border::getNetherBorder, plugin.uhc.border::setNetherBorder, BorderWorldGUI::new));
        setItem(24, plugin.utils.ib(nether.isMaximum() ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE)
                        .name(nether.isMaximum() ? "<dark_gray>+" : "<green>+")
                        .lore("Current border: <green>" + formatBorder(netherBorder))
                        .addLoreIf(!nether.isMaximum(), "Click this to increase this border to <green>" + formatBorder(nether.closestHighestValue()) + "</green>.").build(),
                e -> nether.increase());

        if (plugin.uhc.border.isTpBorder()) {
            // Time between TP Borders
            int timer = plugin.uhc.border.getBorderTimer();
            setItem(30, plugin.utils.ib(Material.CLOCK).name("<yellow>Border Time")
                    .lore("Here you can adjust the time between tp-borders.").build());
            setItem(31, plugin.utils.ib(timer <= 1 ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                            .name(timer <= 1 ? "<dark_gray>-" : "<red>-")
                            .lore("Current time between TP-Borders: <red>" + timer + (timer == 1 ? " minute" : " minutes"))
                            .addLoreIf(timer > 1, "Left-click to <red>decrease</red> 1 minute.",
                                    "Right-click to <red>decrease</red> 5 minutes.").build(),
                    e -> plugin.uhc.ui.decrease(e, plugin.uhc.border::getBorderTimer, plugin.uhc.border::setBorderTimer, BorderWorldGUI::new));
            setItem(32, plugin.utils.ib(Material.PAPER)
                            .name("<yellow>" + timer + (timer == 1 ? " minute" : " minutes"))
                            .lore("Click this to set a custom value.").build(),
                    e -> plugin.uhc.ui.openIntInputSign(player, plugin.uhc.border::getBorderTimer, plugin.uhc.border::setBorderTimer, BorderWorldGUI::new));
            setItem(33, plugin.utils.ib(Material.LIME_STAINED_GLASS_PANE)
                            .name("<green>+").lore("Current time between TP-Borders: <green>" + timer + (timer == 1 ? " minute" : " minutes"),
                                    "Left-click to <green>increase</green> 1 minute.",
                                    "Right-click to <green>increase</green> 5 minutes.").build(),
                    e -> plugin.uhc.ui.increase(e, plugin.uhc.border::getBorderTimer, plugin.uhc.border::setBorderTimer, BorderWorldGUI::new));
        } else {
            // Border Speed
            double speed = plugin.uhc.border.getBorderSpeed();
        }

        //TODO - AÑADIR SONIDOS DE ERROR(?) cuando tratas de interactuar con un cristal gris

        setItem(49, plugin.utils.goBack(), e -> new UHCGUI(player, plugin));

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }

    public String formatBorder(int value) {
        return value / 2 + "x" + value / 2;
    }

    private void toggleTPBorder() {
        boolean bool = !plugin.uhc.border.isTpBorder();
        plugin.uhc.border.setTpBorder(bool);

        if (bool) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
        } else {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        }

        new BorderWorldGUI(player, plugin);
    }
}