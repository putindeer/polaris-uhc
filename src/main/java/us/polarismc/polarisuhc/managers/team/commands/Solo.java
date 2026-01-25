package us.polarismc.polarisuhc.managers.team.commands;

import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Objects;

public class Solo implements CommandExecutor {
    private final Main plugin;

    public Solo(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("solo")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            makeEveryoneSolo();
            plugin.utils.broadcast(Sound.sound(SoundEventKeys.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 1f),
                    "<yellow>All non-team players were added to one.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.utils.message(sender, "<red>This player isn't online.");
            return true;
        }

        UHCPlayer uhcTarget = plugin.player.getUHCPlayer(target);

        if (uhcTarget.getTeam() != null) {
            plugin.utils.message(sender, "<red>This player already has a team.");
            return true;
        }

        new UHCTeam(uhcTarget);
        plugin.utils.message(sender, "<aqua>" + target.getName() + " <green>was added to a solo team.");
        return true;
    }

    private void makeEveryoneSolo() {
        plugin.player.getOnlinePlayers().stream()
                .filter(p -> Objects.requireNonNull(p.getPlayer()).getGameMode() == GameMode.SURVIVAL)
                .filter(p -> p.getTeam() == null)
                .forEach(UHCTeam::new);
    }
}
