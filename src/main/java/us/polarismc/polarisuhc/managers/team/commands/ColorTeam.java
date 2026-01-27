package us.polarismc.polarisuhc.managers.team.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.TeamColor;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.*;

public class ColorTeam implements TabExecutor {
    private final String hostPermission = "uhc.host";
    private final String changeColorPermission = "uhc.changecolor";

    private final Main plugin;

    public ColorTeam(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("colorteam")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player executor)) {
            plugin.utils.message(sender, "<red>Only players can use this command.");
            return true;
        }

        boolean isHost = executor.hasPermission(hostPermission);
        boolean canSelf = executor.hasPermission(changeColorPermission);

        if (!isHost && !canSelf) {
            plugin.utils.message(executor, "<red>No permission. If you want to change your team color, you need VIP rank.");
            return true;
        }

        if (plugin.uhc.hasStarted() && !isHost) {
            plugin.utils.message(executor, "<red>You can't change team colors after the UHC has started.");
            return true;
        }

        Player target;
        String colorArg;

        if (isHost) {
            if (args.length == 1) {
                target = executor;
                colorArg = args[0];
            } else if (args.length >= 2) {
                target = Bukkit.getPlayerExact(args[0]);
                colorArg = args[1];
            } else {
                sendUsage(executor, true);
                return true;
            }
        } else {
            if (args.length < 1) {
                sendUsage(executor, false);
                return true;
            }
            target = executor;
            colorArg = args[0];
        }

        if (target == null) {
            plugin.utils.message(executor, "<red>That player is not online.");
            return true;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);
        UHCTeam team = targetUhc.getTeam();
        if (team == null) {
            plugin.utils.message(executor, isHost && args.length >= 2 ? "<red>This player is not in a team." : "<red>You are not in a team.");
            return true;
        }

        TeamColor color = TeamColor.fromInput(colorArg);
        if (color == null) {
            plugin.utils.message(executor, "<red>Invalid color: <white>" + colorArg + "</white>",
                    "<gray>Valid colors: <white>" + getValidColorsString() + "</white>");
            return true;
        }

        if (isColorBlocked(color)) {
            plugin.utils.message(executor, "<red>This color is already used by another alive team.</red>",
                    "<gray>Pick another color (tab-complete shows available ones).</gray>");
            return true;
        }

        team.setColor(color);
        plugin.utils.message(executor, "<green>Team color changed to <white>" + color.name().toLowerCase(Locale.ROOT) + "</white>.");
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player executor)) return list;

        boolean isHost = executor.hasPermission(hostPermission);
        boolean canSelf = executor.hasPermission(changeColorPermission);

        if (!isHost && !canSelf) return list;
        if (plugin.uhc.hasStarted() && !isHost) return list;

        if (args.length == 1) {
            if (isHost) {
                plugin.team.getTeams().stream().map(UHCTeam::getOnlineMembers).flatMap(Collection::stream).map(Player::getName).forEach(list::add);
            } else {
                UHCTeam team = Optional.ofNullable(plugin.player.getUHCPlayer(executor))
                        .map(UHCPlayer::getTeam)
                        .orElse(null);

                for (TeamColor color : getSelectableColors()) {
                    list.add(color.name().toLowerCase(Locale.ROOT));
                }
            }
        } else if (args.length == 2 && isHost) {
            for (TeamColor color : getSelectableColors()) {
                list.add(color.name().toLowerCase(Locale.ROOT));
            }
        }

        list.removeIf(s -> s == null || !s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT)));
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    private void sendUsage(Player player, boolean isHost) {
        if (isHost) {
            plugin.utils.message(player, "<red>Usage: <white>/colorteam <player> <color></white> <gray>or</gray> <white>/colorteam <color></white>");
        } else {
            plugin.utils.message(player, "<red>Usage: <white>/colorteam <color></white>");
        }
    }

    private Set<TeamColor> getUsedColorsExcept() {
        Set<TeamColor> used = EnumSet.noneOf(TeamColor.class);
        for (UHCTeam team : plugin.team.getTeams()) {
            if (team == null) continue;
            if (team.getPlayingMemberCount() <= 0) continue;

            TeamColor color = team.getColor();
            if (color != null) used.add(color);
        }
        return used;
    }

    private List<TeamColor> getSelectableColors() {
        Set<TeamColor> used = getUsedColorsExcept();

        List<TeamColor> free = new ArrayList<>();
        for (TeamColor color : TeamColor.values()) {
            if (!used.contains(color)) free.add(color);
        }

        return free.isEmpty() ? Arrays.asList(TeamColor.values()) : free;
    }

    private boolean isColorBlocked(TeamColor desired) {
        Set<TeamColor> used = getUsedColorsExcept();
        if (used.size() >= TeamColor.values().length) return false;
        return used.contains(desired);
    }

    private String getValidColorsString() {
        return String.join(", ",
                getSelectableColors().stream()
                        .map(c -> c.name().toLowerCase(Locale.ROOT))
                        .toList()
        );
    }
}