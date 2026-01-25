package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.TeamPrefix;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.*;

public class EmojiTeam implements TabExecutor {
    private final Main plugin;

    public EmojiTeam(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("emojiteam")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            plugin.utils.message(sender, "<red>Usage: <white>/emojiteam <player> <emoji></white>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.utils.message(sender, "<red>Player not online.");
            return true;
        }

        String emojiArg = args[1];
        String emoji = parseEmoji(emojiArg);
        if (emoji == null) {
            plugin.utils.message(sender, "<red>Invalid emoji: <white>" + emojiArg + "</white>",
                    "<gray>Use tab-complete to see valid options.</gray>");
            return true;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);

        UHCTeam team = targetUhc.getTeam();
        if (team == null) {
            plugin.utils.message(sender, "<red>This player is not in a team.");
            return true;
        }

        team.setEmoji(emoji);
        plugin.utils.message(sender, "<green>Team emoji updated for <white>" + target.getName() + "</white>.");
        return true;
    }

    private String parseEmoji(String input) {
        if (input == null || input.isBlank()) return null;

        for (TeamPrefix prefix : TeamPrefix.values()) {
            if (prefix.getSymbol().equals(input)) {
                return prefix.getSymbol();
            }
        }

        String key = input.toUpperCase(Locale.ROOT).replace('-', '_');
        try {
            return TeamPrefix.valueOf(key).getSymbol();
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player player)) return list;
        if (!plugin.utils.hasHostPermission(player)) return list;

        if (args.length == 1) {
            plugin.team.getTeams().stream().map(UHCTeam::getOnlineMembers).flatMap(Collection::stream).map(Player::getName).forEach(list::add);
        }

        if (args.length == 2) {
            for (TeamPrefix prefix : TeamPrefix.values()) {
                list.add(prefix.getSymbol());
            }
        }

        list.removeIf(s -> s == null || !s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT)));
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}