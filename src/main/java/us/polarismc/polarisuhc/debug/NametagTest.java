package us.polarismc.polarisuhc.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.Objects;

public class NametagTest implements CommandExecutor {
    private final Main plugin;

    public NametagTest(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("nametag")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, "<#FF4757>Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            plugin.utils.message(sender, "<#FF4757>Usage: /nametag <add|remove>");
            return true;
        }

        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        switch (args[0].toLowerCase()) {
            case "add":
                plugin.info.nametag.ensureDisplay(player);
                plugin.utils.message(sender, "<#00FF00>Display added.");
                break;

            case "remove":
                TextDisplay display = uhcPlayer.getNametag();
                if (display != null) {
                    display.remove();
                    uhcPlayer.setNametag(null);
                    plugin.utils.message(sender, "<#00FF00>Display removed.");
                } else {
                    plugin.utils.message(sender, "<#FF4757>No display found.");
                }
                break;

            default:
                plugin.utils.message(sender, "<#FF4757>Usage: /hex <add|remove>");
                break;
        }

        return true;
    }
}