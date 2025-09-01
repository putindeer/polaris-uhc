package us.polarismc.polarisuhc.managers.uhc.gui;

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
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<gold>[lang]dandrgui.gui_title[/lang]")));
        this.plugin = plugin;
        this.player = player;

        int[] border = {0, 2, 3, 4, 5, 6, 8, 12, 14, 18, 20, 24, 26, 30, 31, 32, 36, 38, 39, 40, 41, 42, 44, 48, 50};
        setItems(border, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        boolean isFinalHeal = plugin.uhc.getFinalHealTime() > 0;

        placeControl(1, plugin.uhc::getAppleRate, plugin.uhc::setAppleRate, Material.APPLE,
                "<red>[lang]dandrgui.apple[/lang]", "[lang]dandrgui.apple_desc[/lang]","[lang]dandrgui.percentage[/lang]", "%", 0, 100);
        placeControl(19, plugin.uhc::getGlassRate, plugin.uhc::setGlassRate, Material.GLASS,
                "<white>[lang]dandrgui.glass[/lang]", "[lang]dandrgui.glass_desc[/lang]","[lang]dandrgui.percentage[/lang]", "%", 0, 100);
        placeControl(37, plugin.uhc::getFlintRate, plugin.uhc::setFlintRate, Material.FLINT,
                "<gray>[lang]dandrgui.flint[/lang]", "[lang]dandrgui.flint_desc[/lang]","[lang]dandrgui.percentage[/lang]", "%", 0, 100);

        placeControl(13, plugin.uhc::getXpKillRate, plugin.uhc::setXpKillRate, Material.EXPERIENCE_BOTTLE,
                "<aqua>[lang]dandrgui.xpkill[/lang]", "[lang]dandrgui.xpkill_desc[/lang]","[lang]dandrgui.xp[/lang]", " [lang]terms.levels[/lang]", 1, 100);

        placeControl(7, plugin.uhc::getPvpTime, plugin.uhc::setPvpTime, Material.IRON_SWORD,
                "<red>[lang]dandrgui.pvp[/lang]", "[lang]dandrgui.pvp_desc[/lang]","[lang]dandrgui.time[/lang]", " [lang]terms.minutes[/lang]", 1, 180);
        placeControl(25, plugin.uhc::getMeetupTime, plugin.uhc::setMeetupTime, Material.COMPASS,
                "<yellow>[lang]dandrgui.meetup[/lang]", "[lang]dandrgui.meetup_desc[/lang]","[lang]dandrgui.time[/lang]", " [lang]terms.minutes[/lang]", 1, 300);

        placeControl(43, plugin.uhc::getFinalHealTime, plugin.uhc::setFinalHealTime, isFinalHeal ? Material.ENCHANTED_GOLDEN_APPLE : Material.GOLDEN_APPLE,
                "<gold>[lang]dandrgui.finalheal[/lang]", "[lang]dandrgui.finalheal_desc[/lang]","[lang]dandrgui.time[/lang]", " [lang]terms.minutes[/lang]", 0, 120);

        setItem(49, plugin.uhc.goBack(player), e -> new UHCGUI(player, plugin));
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
                e -> plugin.uhc.openIntInputSign(player, getter, setter, minimum, maximum, DurationRatesGUI::new)
        );
        boolean isMinimum = getter.get() <= minimum;
        boolean isMaximum = getter.get() >= maximum;

        int belowSlot = slot + 9;
        setItem(belowSlot - 1, plugin.utils.ib(isMinimum ? Material.BLACK_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).name("<red>-")
                        .lore(controlDesc + ": <red>" + getter.get() + suffix,
                        "[lang]common.decrease_timer_1m[/lang]",
                        "[lang]common.decrease_timer_5m[/lang]").build(),
                e -> plugin.uhc.decrease(e, getter, setter, minimum, DurationRatesGUI::new)
        );

        setItem(belowSlot, plugin.utils.ib(Material.PAPER).name("<yellow>" + value + suffix).lore("[lang]common.custom_value[/lang]").build(),
                e -> plugin.uhc.openIntInputSign(player, getter, setter, minimum, maximum, DurationRatesGUI::new)
        );

        setItem(belowSlot + 1, plugin.utils.ib(isMaximum ? Material.BLACK_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).name("<green>+")
                        .lore(controlDesc + ": <green>" + getter.get() + suffix,
                        "[lang]common.increase_timer_1m[/lang]",
                        "[lang]common.increase_timer_5m[/lang]").build(),
                e -> plugin.uhc.increase(e, getter, setter, maximum, DurationRatesGUI::new));
    }
}