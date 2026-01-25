package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraftsGUI;
import us.polarismc.polarisuhc.config.duration.DurationRatesGUI;
import us.polarismc.polarisuhc.config.potion.PotionsGUI;
import us.polarismc.polarisuhc.config.toggle.ToggleGUI;
import us.polarismc.polarisuhc.config.border.BorderWorldGUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUI implements TabExecutor {
    private final Main plugin;

    public GUI(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("gui")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            plugin.utils.message(player, "<red>Usage: /gui <gui name>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "createworld" -> new BorderWorldGUI(player, plugin);
            case "toggle" -> new ToggleGUI(player, plugin);
            case "customcrafts" -> new CustomCraftsGUI(player, plugin);
            case "potions" -> new PotionsGUI(player, plugin);
            case "duration" -> new DurationRatesGUI(player, plugin);
            default -> plugin.utils.message(player, "<red>Unknown GUI.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> guis = Arrays.asList("createworld", "toggle", "customcrafts", "potions", "duration");
            return guis.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
