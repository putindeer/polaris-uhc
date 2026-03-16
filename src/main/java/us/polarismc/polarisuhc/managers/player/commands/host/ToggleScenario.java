package us.polarismc.polarisuhc.managers.player.commands.host;

import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ToggleScenario implements TabExecutor {
    private final Main plugin;

    public ToggleScenario(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("togglescenario")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            plugin.utils.message(sender, "<red>Usage: /togglescenario <scenario>");
            return true;
        }
        String scenarioName = args[0];

        plugin.scen.toggle(scenarioName, sender);

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.addAll(plugin.scen.getAll().values().stream().map(BaseScenario::getName).toList());
        }

        list.removeIf(s -> s == null || !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(list);
        return list;
    }
}