package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.MinedResource;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Objects;

public class Ores implements CommandExecutor, Listener {
    private final Main plugin;
    public Ores(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("ores")).setExecutor(this);
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

            MinedResource[] resources = MinedResource.values();
            StringBuilder message = new StringBuilder();
            message.append(uhcPlayer.getDisplayName()).append(" <gray>has in their inventory: ");

            for (int i = 0; i < resources.length; i++) {
                MinedResource resource = resources[i];

                int amount = plugin.utils.getMaterialAmount(player, resource.getMaterial());
                message.append(resource.getFormattedDisplayName()).append(": ").append(amount);

                if (i < resources.length - 1) {
                    message.append(" <dark_gray>| ");
                }
            }

            team.sendMessage(player, message.toString());
        } else {
            plugin.utils.message(sender, "<red>Only players can use this command!");
        }
        return false;
    }
}
