package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Objects;

public class BookStuff implements CommandExecutor {
    private final Main plugin;
    public BookStuff(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("bookstuff")).setExecutor(this);
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

            Material[] materials = {Material.SUGAR_CANE, Material.PAPER, Material.LEATHER, Material.BOOK};
            String[] names = {"<#7CFC98>Sugar Cane", "<#E6E6E6>Paper", "<#A97142>Leather", "<#9FC5E8>Books"};

            StringBuilder message = new StringBuilder();
            message.append(uhcPlayer.getDisplayName()).append(" <gray>has in their inventory: ");

            for (int i = 0; i < materials.length; i++) {
                int amount = plugin.utils.getMaterialAmount(player, materials[i]);

                message.append(names[i]).append(": ").append(amount);

                if (i < materials.length - 1) {
                    message.append(" <dark_gray>| ");
                } else {
                    message.append("<dark_gray>.");
                }
            }

            team.sendMessage(player, message.toString());
        } else {
            plugin.utils.message(sender, "<red>Only players can use this command!");
        }
        return false;
    }
}
