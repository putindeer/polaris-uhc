package us.polarismc.polarisuhc.commands.host;

import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Objects;

public class CreateWorld implements CommandExecutor {
    private final Main plugin;

    public CreateWorld(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("createworld")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        try {
            switch (args.length) {
                case 1 -> {
                    if (args[0].equalsIgnoreCase("-a")) {
                        plugin.uhc.setAmplified(true);
                    } else {
                        plugin.uhc.setGlobalSeed(Long.parseLong(args[0]));
                    }
                }
                case 2 -> {
                    if (args[1].equalsIgnoreCase("-a")) {
                        plugin.uhc.setGlobalSeed(Long.parseLong(args[0]));
                        plugin.uhc.setAmplified(true);
                    } else {
                        plugin.uhc.setSeed(Long.parseLong(args[0]));
                        plugin.uhc.setNetherSeed(Long.parseLong(args[1]));
                        plugin.uhc.setEndSeed(Long.parseLong(args[0]));
                    }
                }
                case 3 -> {
                    if (args[1].equalsIgnoreCase("-a")) {
                        plugin.uhc.setAmplified(true);
                        plugin.uhc.setSeed(Long.parseLong(args[0]));
                        plugin.uhc.setNetherSeed(Long.parseLong(args[1]));
                        plugin.uhc.setEndSeed(Long.parseLong(args[0]));
                    } else {
                        sendUsage(sender);
                        return false;
                    }
                }
                default -> {
                    sendUsage(sender);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            plugin.utils.message(sender, "<red>[lang]worlds.invalid_seed_format[/lang]");
            return false;
        }
        plugin.uhc.createWorlds(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        plugin.utils.message(sender, "<red>[lang]common.usage[/lang]: /createworld <[lang]terms.seed[/lang]> [-a] <[lang]terms.netherseed[/lang]>");
        plugin.utils.message(sender, "<red>[lang]common.example[/lang]: /createworld");
        plugin.utils.message(sender, "<red>[lang]common.example[/lang]: /createworld 12345");
        plugin.utils.message(sender, "<red>[lang]common.example[/lang]: /createworld 12345 -a");
        plugin.utils.message(sender, "<red>[lang]common.example[/lang]: /createworld 12345 67890");
    }
}
