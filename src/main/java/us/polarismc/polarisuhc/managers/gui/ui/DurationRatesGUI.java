package us.polarismc.polarisuhc.managers.gui.ui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DurationRatesGUI extends FastInv {
    private final Main plugin;
    private final Player player;

    public DurationRatesGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<gold>Duration and Rates Settings")));
        this.plugin = plugin;
        this.player = player;

        int[] border = {0, 2, 3, 4, 5, 6, 8, 12, 14, 18, 20, 24, 26, 30, 31, 32, 36, 38, 39, 40, 41, 42, 44, 48, 50};
        setItems(border, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        boolean isFinalHeal = plugin.uhc.duration.getFinalHealTime() > 0;

        placeControl(1, plugin.uhc.rates::getAppleRate, plugin.uhc.rates::setAppleRate, Material.APPLE,
                "<red>Apple Rate", "Here you can adjust the Apple drop-rate.","Current drop percentage", "%", 0, 100);
        placeControl(19, plugin.uhc.rates::getGlassRate, plugin.uhc.rates::setGlassRate, Material.GLASS,
                "<white>Glass Rate", "Here you can adjust the Glass drop-rate (auto-smelted from sand).","Current drop percentage", "%", 0, 100);
        placeControl(37, plugin.uhc.rates::getFlintRate, plugin.uhc.rates::setFlintRate, Material.FLINT,
                "<gray>Flint Rate", "Here you can adjust the Flint drop-rate.","Current drop percentage", "%", 0, 100);

        placeControl(13, plugin.uhc.rates::getXpKillRate, plugin.uhc.rates::setXpKillRate, Material.EXPERIENCE_BOTTLE,
                "<aqua>XP Levels per Kill", "Here you can adjust the XP levels that a player will receive per kill.","Current XP levels per kill", " levels", 1, 100);

        placeControl(7, plugin.uhc.duration::getPvpTime, plugin.uhc.duration::setPvpTime, Material.IRON_SWORD,
                "<red>PvP Time", "Here you can adjust the time until enabled PvP.","Current minutes until enabled", " minutes", 1, 180);
        placeControl(25, plugin.uhc.duration::getMeetupTime, plugin.uhc.duration::setMeetupTime, Material.COMPASS,
                "<yellow>Meetup Time", "Here you can adjust the time between enabled PvP and Meetup.","Current minutes until enabled", " minutes", 1, 300);

        placeControl(43, plugin.uhc.duration::getFinalHealTime, plugin.uhc.duration::setFinalHealTime, isFinalHeal ? Material.ENCHANTED_GOLDEN_APPLE : Material.GOLDEN_APPLE,
                "<gold>Final Heal Time", "Here you can adjust the time until Final Heal. <dark_gray>(set it to 0 to disable it)</dark_gray>","Current minutes until enabled", " minutes", 0, 120);

        setItem(49, plugin.utils.goBack(), e -> new UHCGUI(player, plugin));
        addClickHandler(e -> e.setCancelled(true));

        open(player);
    }

    /**
     * Places a control set (decrease button, display item, increase button) in the inventory
     *
     * @param slot The center slot where the display item will be placed
     * @param getter     Method to get the current value
     * @param setter     Method to set the new value
     * @param icon       Material to use for the display item
     * @param name       Name for the display item
     * @param desc       Description text
     * @param controlDesc Description for control buttons
     * @param suffix     Suffix to show after the value
     * @param minimum    Minimum allowed value
     * @param maximum    Maximum allowed value
     */
    private void placeControl(int slot, Supplier<Integer> getter, Consumer<Integer> setter, Material icon, String name, String desc, String controlDesc, String suffix, int minimum, int maximum) {
        int value = getter.get();
        setItem(slot, plugin.utils.ib(icon).name(name).lore(desc).build(),
                e -> plugin.uhc.ui.openIntInputSign(player, getter, setter, minimum, maximum, DurationRatesGUI::new)
        );
        boolean isMinimum = getter.get() <= minimum;
        boolean isMaximum = getter.get() >= maximum;

        int belowSlot = slot + 9;
        setItem(belowSlot - 1, plugin.utils.ib(isMinimum ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).name("<red>-")
                        .lore(controlDesc + ": <red>" + getter.get() + suffix,
                                "Left-click to <red>decrease</red> 1 minute.",
                                "Right-click to <red>decrease</red> 5 minutes.").build(),
                e -> plugin.uhc.ui.decrease(e, getter, setter, minimum, DurationRatesGUI::new)
        );

        setItem(belowSlot, plugin.utils.ib(Material.PAPER).name("<yellow>" + value + suffix).lore("Click this to set a custom value.").build(),
                e -> plugin.uhc.ui.openIntInputSign(player, getter, setter, minimum, maximum, DurationRatesGUI::new)
        );

        setItem(belowSlot + 1, plugin.utils.ib(isMaximum ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).name("<green>+")
                        .lore(controlDesc + ": <green>" + getter.get() + suffix,
                                "Left-click to <green>increase</green> 1 minute.",
                                "Right-click to <green>increase</green> 5 minutes.").build(),
                e -> plugin.uhc.ui.increase(e, getter, setter, maximum, DurationRatesGUI::new));
    }
}