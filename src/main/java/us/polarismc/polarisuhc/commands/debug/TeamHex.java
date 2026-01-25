package us.polarismc.polarisuhc.commands.debug;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.team.TeamColor;

import java.util.Objects;

public class TeamHex implements CommandExecutor {
    private final Main plugin;

    public TeamHex(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("teamhex")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (TeamColor color : TeamColor.values()) {
            String line = color.getMiniMessageHex()
                    + " " + color.name()
                    + " <gray>(" + color.getHex() + ")";
            plugin.utils.message(sender, line);
        }
        return true;
    }
}

