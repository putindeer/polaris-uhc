package us.polarismc.polarisuhc.util;

import me.putindeer.api.util.PluginUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.info.actionbar.ActionBarManager;

public class Utils extends PluginUtils {
    public final ActionBarManager bar;
    public Utils(Main plugin, String prefix) {
        super(plugin, prefix);
        bar = new ActionBarManager(plugin);
    }

    public ItemStack goBack() {
        return ib(Material.BARRIER).name("Go Back").lore("Click this to go back to the previous menu.").build();
    }

    //todo - (add primary color compatibility)
    public String teamPrefix() {
        return "<blue>Team <dark_gray>» </dark_gray>";
    }

    public String teamPrefix(Player player) {
        return "<blue>Team <dark_gray>» </dark_gray>";
    }

    public boolean hasHostPermission(CommandSender sender) {
        return sender.hasPermission("uhc.host");
    }

    public ItemStack head() {
        //TODO - add ghead
        return new ItemStack(Material.PLAYER_HEAD);
    }
}