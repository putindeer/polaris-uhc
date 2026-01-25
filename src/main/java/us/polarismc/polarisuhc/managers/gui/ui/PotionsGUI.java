package us.polarismc.polarisuhc.managers.gui.ui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.potion.PotionSetting;

import java.util.function.Consumer;

public class PotionsGUI extends FastInv {
    private final Main plugin;
    private final Player player;

    public PotionsGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>Potion Settings</blue>")));
        this.plugin = plugin;
        this.player = player;

        int[] glass = new int[]{0, 1, 2, 6, 7, 8, 9, 10, 11, 15, 16, 17, 36, 40, 44, 45, 53};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        setItem(49, plugin.utils.goBack(), e -> new UHCGUI(player, plugin));

        addPotion(3, PotionSetting.POISON, ib -> {});
        addPotion(4, PotionSetting.SWIFTNESS, ib -> {});
        addPotion(5, PotionSetting.FIRE_RESISTANCE, ib -> {});
        addPotion(18, PotionSetting.TURTLE_MASTER, ib -> {});
        addPotion(19, PotionSetting.SLOWNESS, ib -> {});
        addPotion(20, PotionSetting.INVISIBILITY, ib -> {});
        addPotion(21, PotionSetting.REGENERATION, ib -> {});
        addPotion(22, PotionSetting.STRENGTH, ib -> {});
        addPotion(23, PotionSetting.HEALING, ib -> {});
        addPotion(24, PotionSetting.HARMING, ib -> {});
        addPotion(25, PotionSetting.SLOW_FALLING, ib -> {});
        addPotion(26, PotionSetting.WEAKNESS, ib -> {});
        addPotion(37, PotionSetting.LEAPING, ib -> {});
        addPotion(38, PotionSetting.WATER_BREATHING, ib -> {});
        addPotion(39, PotionSetting.INFESTATION, ib -> {});
        addPotion(41, PotionSetting.WIND_CHARGING, ib -> {});
        addPotion(42, PotionSetting.OOZING, ib -> {});
        addPotion(43, PotionSetting.WEAVING, ib -> {});

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }

    private void addPotion(int slot, PotionSetting setting, Consumer<ItemBuilder> iconConfig) {
        ItemBuilder ib = setting.buildIcon(plugin);
        iconConfig.accept(ib);
        setItem(slot, ib.build(), e -> plugin.uhc.potion.togglePotion(player, setting, PotionsGUI::new));
        setItem(slot + 9, setting.buildToggleGlass(plugin).build(), e -> plugin.uhc.potion.togglePotion(player, setting, PotionsGUI::new));
    }
}