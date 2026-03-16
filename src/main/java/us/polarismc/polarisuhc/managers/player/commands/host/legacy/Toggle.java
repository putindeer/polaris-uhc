package us.polarismc.polarisuhc.managers.player.commands.host.legacy;

import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.toggle.ToggleSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Toggle implements TabExecutor {
    private final Main plugin;

    public Toggle(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("toggle")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            plugin.utils.message(sender, "<red>Usage: /toggle <setting>");
            return true;
        }

        ToggleSetting setting = Arrays.stream(ToggleSetting.values())
                .filter(s -> s.getInfo().id().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);

        if (setting == null) {
            plugin.utils.message(sender, "<red>Setting not found!");
            return true;
        }

        plugin.uhc.toggle.toggleSetting(setting);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.addAll(Arrays.stream(ToggleSetting.values()).map(s -> s.getInfo().id()).toList());
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(list);
        return list;
    }
}