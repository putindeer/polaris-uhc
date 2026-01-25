package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Objects;

public class Hex implements CommandExecutor {
    private final Main plugin;

    public Hex(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("hex")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) {
            plugin.utils.message(sender, "<#FF4757>Usage: /hex <message>");
            return true;
        }

        String message = String.join(" ", args);
        plugin.utils.message(sender, false, message);

        return true;
    }
}
