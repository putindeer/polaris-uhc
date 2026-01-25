package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;
import us.polarismc.polarisuhc.managers.channel.GlobalChannel;
import us.polarismc.polarisuhc.managers.channel.TeamChannel;
import us.polarismc.polarisuhc.managers.channel.ChannelKey;

import java.util.Objects;

public class TeamChat implements CommandExecutor {
    private final Main plugin;

    public TeamChat(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("teamchat")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, "<red>Only players can use this command!");
            return true;
        }

        if (player.getGameMode() == GameMode.SPECTATOR) {
            plugin.utils.message(player, "<red>You can't use this command while spectating!");
            return true;
        }

        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        UHCTeam team = uhcPlayer.getTeam();
        if (team == null) {
            plugin.utils.message(player, "<red>You are not in a team!");
            return true;
        }

        if (uhcPlayer.isDead()) {
            plugin.utils.message(player, "<red>You can only use this command while playing!");
            return true;
        }

        ChannelKey current = uhcPlayer.getSendChannel();

        boolean isTeam = current instanceof TeamChannel(UHCTeam team1) && team1.equals(team);
        if (isTeam) {
            uhcPlayer.setSendChannel(new GlobalChannel());
            plugin.utils.message(player, false, plugin.utils.teamPrefix(player) + "<white>TeamChat <red>Off</red>");
        } else {
            uhcPlayer.setSendChannel(new TeamChannel(team));
            plugin.utils.message(player, false, plugin.utils.teamPrefix(player) + "<white>TeamChat <green>On</green>");
        }

        return true;
    }
}

