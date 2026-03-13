package us.polarismc.polarisuhc.commands.host;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Objects;

public class QuickStart implements CommandExecutor {
    private final Main plugin;
    public QuickStart(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("quickstart")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] strings) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, "You can't use this command in the console!");
            return false;
        }
        plugin.game.quickStart(player);
        return true;
    }
}
