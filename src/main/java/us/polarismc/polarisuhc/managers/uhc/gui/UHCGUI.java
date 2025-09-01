package us.polarismc.polarisuhc.managers.uhc.gui;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;
import us.polarismc.polarisuhc.Main;

public class UHCGUI extends FastInv {
    public UHCGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<black>uhc gui test placehlder", player)));
        int[] glass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        setItems(glass, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());

        setItem(0, plugin.utils.ib(Material.GRASS_BLOCK, player).name("world").build(), e -> new BorderWorldGUI(player, plugin));
        setItem(1, plugin.utils.ib(Material.CLOCK, player).name("duration").build(), e -> new DurationRatesGUI(player, plugin));
        setItem(2, plugin.utils.ib(Material.APPLE, player).name("rates").build(), e -> new DurationRatesGUI(player, plugin));
        setItem(3, plugin.utils.ib(Material.POTION, player).potionType(PotionType.SWIFTNESS).customName("potions").build(), e -> new PotionsGUI(player, plugin));
        setItem(4, plugin.utils.ib(Material.ELYTRA, player).name("customcrafts").build(), e -> new CustomCraftsGUI(player, plugin));
        setItem(5, plugin.utils.ib(Material.LEVER, player).name("toggles").build(), e -> new ToggleGUI(player, plugin));

        addClickHandler(e -> e.setCancelled(true));
        open(player);
    }
}
