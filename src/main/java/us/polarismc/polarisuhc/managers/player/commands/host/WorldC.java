package us.polarismc.polarisuhc.managers.player.commands.host;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldC implements TabExecutor {
    private final Main plugin;

    public WorldC(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("world")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, "<red>Only players can use this command.");
            return true;
        }
        if (args.length != 1) {
            plugin.utils.message(sender, "<red>Usage: /world <name>");
            return true;
        }

        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            plugin.utils.message(sender, "<red>That world does not exist.");
            return true;
        }

        plugin.player.teleport(player, world.getSpawnLocation());
        plugin.utils.message(sender, "<green>Teleported to " + args[0] + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getWorlds().stream().map(World::getName).forEach(completions::add);
        }
        completions.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}

