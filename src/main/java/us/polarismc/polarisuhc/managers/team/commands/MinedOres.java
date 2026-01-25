package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.MinedResource;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Objects;

public class MinedOres implements CommandExecutor, Listener {
    private final Main plugin;
    public MinedOres(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("minedores")).setExecutor(this);
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        UHCPlayer player = plugin.player.getUHCPlayer(event.getPlayer());
        if (player.isPlaying()) {
            switch (event.getBlock().getType()) {
                case ANCIENT_DEBRIS -> player.addMinedResource(MinedResource.NETHERITE_SCRAP);
                case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> player.addMinedResource(MinedResource.DIAMOND);
                case GOLD_ORE, DEEPSLATE_GOLD_ORE -> player.addMinedResource(MinedResource.GOLD);
                case IRON_ORE, DEEPSLATE_IRON_ORE -> player.addMinedResource(MinedResource.IRON);
            }
        }
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
            message.append(uhcPlayer.getDisplayName()).append(" <gray>has mined: ");

            for (int i = 0; i < resources.length; i++) {
                MinedResource resource = resources[i];

                int amount = uhcPlayer.getMinedResource(resource);
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
