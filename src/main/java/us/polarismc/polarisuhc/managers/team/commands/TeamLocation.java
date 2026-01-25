package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Objects;

public class TeamLocation implements CommandExecutor, Listener {
    private final Main plugin;
    public TeamLocation(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("teamlocation")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String @NotNull [] strings) {
        if (sender instanceof Player player) {
            UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
            UHCTeam team = uhcPlayer.getTeam();
            if (team == null) {
                plugin.utils.message(sender, "<red>You are not in a team!");
                return true;
            }

            if (!uhcPlayer.isPlaying()) {
                plugin.utils.message(sender, "<red>You can only use this command while playing!");
                return true;
            }
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            String color = team.getColorTag();
            String world = getWorldString(player);

            //TODO - add primary color compatibility?
            team.sendMessage(player, uhcPlayer.getDisplayName() + " <gray>is in</gray> " +
                    "<dark_aqua>X:</dark_aqua> " + color + x + " " +
                    "<dark_aqua>Y:</dark_aqua> " + color + y + " " +
                    "<dark_aqua>Z:</dark_aqua> " + color + z + " <dark_gray>|</dark_gray> " + world);
        } else {
            plugin.utils.message(sender, "<red>Only players can use this command!");
        }
        return false;
    }

    public String getWorldString(Player player) {
        if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            return "<green>Overworld";
        } else if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            return "<red>Nether";
        } else if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
            return "<dark_purple>The End";
        } else {
            return "<gray>Other";
        }
    }
}
