package us.polarismc.polarisuhc.config.customcrafts;

import fr.mrmicky.fastinv.FastInv;
import me.putindeer.api.util.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCGUI;

import java.util.function.Consumer;
import java.util.stream.IntStream;

public class CustomCraftsGUI extends FastInv {

    private final Main plugin;
    private final Player player;

    public CustomCraftsGUI(Player player, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("<blue>Custom Crafts Settings</blue>")));
        this.plugin = plugin;
        this.player = player;

        setItems(0, 53, plugin.utils.ib(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name("").hideTooltip().build());
        setItem(49, plugin.utils.goBack(), e -> new UHCGUI(player, plugin));

        addToggle(2, CustomCraftSetting.TOTEM_CRAFT);
        addToggle(3, CustomCraftSetting.SPECTRAL_ARROW_CRAFT);
        addToggle(4, CustomCraftSetting.GLISTERING_MELON_CRAFT);
        addToggle(5, CustomCraftSetting.TNT_MINECART_CRAFT);
        addToggle(6, CustomCraftSetting.LECTERN_CRAFT);
        addToggle(29, CustomCraftSetting.GOLDEN_HEAD_CRAFT);
        addToggle(30, CustomCraftSetting.TRIDENT_CRAFT);
        addToggle(31, CustomCraftSetting.ELYTRA_CRAFT);
        addToggle(32, CustomCraftSetting.MACE_CRAFT);
        addToggle(33, CustomCraftSetting.BREEZE_ROD_CRAFT);

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
    
    private void addToggle(int slot, CustomCraftSetting setting) {
        addToggle(slot, setting, ib -> {});
    }
}
