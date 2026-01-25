package us.polarismc.polarisuhc.managers.team.commands;

import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.team.TeamSize;

import java.util.Objects;

public class MakeTeams implements CommandExecutor {
    private final Sound sound = Sound.sound(SoundEventKeys.BLOCK_NOTE_BLOCK_BIT, Sound.Source.MASTER, 1f, 1f);

    private final Main plugin;

    public MakeTeams(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("maketeams")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (plugin.uhc.isStarted()) {
            plugin.utils.message(sender, sound, "<red>You can't use this after the UHC has started.");
            return true;
        }

        int teamLimit = plugin.team.getTeamLimit();
        if (teamLimit <= 1) {
            plugin.utils.message(sender, sound, "<red>You need to set a valid team limit before using <white>/maketeams</white>.",
                    "<gray>Use <white>/team limit</white> to change it.</gray>");
            return true;
        }

        if (plugin.team.getTeamSize() != TeamSize.CHOSEN) {
            plugin.utils.message(sender, sound, "<red>/maketeams can only be used when TeamSize is <white>Chosen</white>.",
                    "<gray>Current team size: <white>" + plugin.team.getTeamSizeDisplayName() + "</white>");
            return true;
        }

        String invite = "<click:suggest_command:'/team invite '>" +
                        "<hover:show_text:'Suggest /team invite'>" +
                        "<yellow>/team invite <nick></yellow></hover></click>";

        String message = "<dark_aqua><bold>Vayan haciendo teams con <dark_gray>[" + invite + "]</dark_gray>, es <red>" + plugin.team.getTeamSizeDisplayName() +
                        "<gray> | <dark_aqua>Make teams with <dark_gray>[" + invite + "]</dark_gray>, it's <red>" + plugin.team.getTeamSizeDisplayName();

        plugin.utils.broadcast(sound, message);
        return true;
    }
}
