package us.polarismc.polarisuhc.managers.team.commands;

import net.kyori.adventure.sound.Sound;
import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.TeamSize;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.*;
import java.util.stream.Collectors;

public class TeamCommand implements TabExecutor {
    private final Main plugin;

    private final Sound SOUND_OK = Sound.sound(SoundEventKeys.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 1f);
    private final Sound SOUND_NO = Sound.sound(SoundEventKeys.ENTITY_VILLAGER_NO, Sound.Source.MASTER, 1f, 1f);

    public TeamCommand(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("team")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, SOUND_NO, "<red>Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        switch (subCommand) {
            case "help" -> sendHelp(player);
            case "invite" -> handleInvite(player, args);
            case "accept" -> handleAccept(player, args);
            case "check" -> handleCheck(player, args);
            case "leave" -> handleLeave(player);
            case "size" -> handleSize(player, args);
            case "limit" -> handleLimit(player, args);
            case "add" -> handleAdd(player, args);
            case "remove" -> handleRemove(player, args);
            case "random" -> handleRandom(player, args);
            default -> plugin.utils.message(player, SOUND_NO, "<red>Unknown subcommand. Use <white>/team help</white>.");
        }

        return true;
    }

    private void sendHelp(Player player) {
        List<String> lines = new ArrayList<>(List.of(
                "<gray>---- <white>/team</white> <gray>----</gray>",
                "<yellow>/team invite <player> <gray>- Invite a player</gray>",
                "<yellow>/team accept <player> <gray>- Accept an invite</gray>",
                "<yellow>/team check <gray>- Show your team</gray>",
                "<yellow>/team leave <gray>- Leave your team <dark_red>(disabled after start)</dark_red></gray>"
        ));

        if (plugin.utils.hasHostPermission(player)) {
            lines.addAll(List.of(
                    "<gray>Host sub-commands:</gray>",
                    "<yellow>/team size <number>",
                    "<yellow>/team enable <gray>|</gray> /team disable",
                    "<yellow>/team add <player> <leader>",
                    "<yellow>/team remove <player>",
                    "<yellow>/team random <gray>[size]"
            ));
        }

        plugin.utils.message(player, lines.toArray(String[]::new));
    }

    private void handleInvite(Player inviter, String[] args) {
        if (isSpectating(inviter)) return;
        if (cantCreateNewTeams(inviter)) return;

        if (args.length < 2) {
            plugin.utils.message(inviter, SOUND_NO, "<red>Usage: <white>/team invite <player>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            plugin.utils.message(inviter, SOUND_NO, "<red><aqua>" + args[1] + "</aqua> isn't online.");
            return;
        }

        if (target.getUniqueId().equals(inviter.getUniqueId())) {
            plugin.utils.message(inviter, SOUND_NO, "<red>You can't invite yourself.");
            return;
        }

        UHCPlayer inviterUhc = plugin.player.getUHCPlayer(inviter);
        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);

        UHCTeam inviterTeam = inviterUhc.getTeam();

        if (inviterTeam != null && isTeamFull(inviterTeam)) {
            plugin.utils.message(inviter, SOUND_NO, "<red>The team is full!");
            return;
        }

        if (targetUhc.getTeamInvites().contains(inviterUhc)) {
            plugin.utils.message(inviter, SOUND_NO, "<red>You already invited this player to your team.");
            return;
        }

        targetUhc.addTeamInvite(inviterUhc);
        plugin.utils.message(inviter, SOUND_OK, "<green>You invited <aqua>" + target.getName() + "</aqua> to your team.");
        plugin.utils.message(target, SOUND_OK,
                "<aqua>" + inviter.getName() + "</aqua> has invited you to their team.",
                "<gray>Use <white>/team accept " + inviter.getName() + "</white> to join.</gray>",
                "<gray><click:run_command:'/team accept " + inviter.getName() + "'>" +
                        "<hover:show_text:'Click to join " + inviter.getName() + "'s team.'>" +
                        "<aqua>[Click to accept]</aqua></hover></click></gray>"
        );

        plugin.utils.delay(600, () -> {
            if (!targetUhc.removeTeamInvite(inviterUhc)) return;
            plugin.utils.message(inviter, "<gray>The invitation of <aqua>" + target.getName() + "</aqua> has expired.");
        });
    }


    private void handleAccept(Player target, String[] args) {
        if (isSpectating(target)) return;
        if (cantCreateNewTeams(target)) return;

        if (args.length < 2) {
            plugin.utils.message(target, SOUND_NO, "<red>Usage: <white>/team accept <player>");
            return;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);

        Player inviter = Bukkit.getPlayerExact(args[1]);
        if (inviter == null) {
            plugin.utils.message(target, SOUND_NO, "<red>That player isn't online.");
            return;
        }

        UHCPlayer inviterUhc = plugin.player.getUHCPlayer(inviter);

        if (!targetUhc.hasTeamInvite(inviterUhc)) {
            plugin.utils.message(target, SOUND_NO, "<red>You don't have an invite from that player.");
            return;
        }

        UHCTeam inviterTeam = inviterUhc.getTeam();
        targetUhc.removeTeamInvite(inviterUhc);

        if (inviterTeam != null && isTeamFull(inviterTeam)) {
            plugin.utils.message(target, SOUND_NO, "<red>That team is full.");
            return;
        }

        UHCTeam currentTeam = targetUhc.getTeam();
        boolean started = plugin.uhc.isNotWaiting();

        if (currentTeam != null && started && currentTeam.getScatteredUHCMembers().size() > 1) {
            plugin.utils.message(target, SOUND_NO, "<red>You can't join a team if you already have been scattered with one after the UHC has started.");
            return;
        }

        if (currentTeam != null) {
            currentTeam.removeMember(targetUhc);
        }

        if (inviterTeam == null) {
            inviterTeam = new UHCTeam(inviterUhc);
        }

        String color = inviterTeam.getColorTag();

        inviterTeam.sendMessage(inviter, color + target.getName() + "<green> joined your team!");

        inviterTeam.addMember(targetUhc);

        plugin.utils.message(target, SOUND_OK, "<green>You joined " + color + inviter.getName() + "</c>'s team.");
    }

    private void handleLeave(Player player) {
        if (plugin.uhc.isNotWaiting()) {
            plugin.utils.message(player, SOUND_NO, "<red>You can't leave a team after the UHC has started.");
            return;
        }

        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        UHCTeam team = uhcPlayer.getTeam();
        if (team == null) {
            plugin.utils.message(player, SOUND_NO, "<red>You are not in a team.");
            return;
        }

        team.removeMember(uhcPlayer);
        plugin.utils.message(player, SOUND_OK, "<green>You left your team.");
        String color = team.getColorTag();
        team.sendMessage(player, color + player.getName() + "<red> left your team.");
    }

    private void handleSize(Player host, String[] args) {
        if (doesntHasHostPermission(host)) return;

        if (plugin.uhc.hasStarted()) {
            plugin.utils.message(host, SOUND_NO, "<red>You can't change team size after start.");
            return;
        }

        if (args.length < 2) {
            sendSizeHelp(host);
            return;
        }

        TeamSize teamSize = parseTeamSize(args[1]);
        if (teamSize == null) {
            plugin.utils.message(host, SOUND_NO, "<red>Unknown team size.");
            sendSizeHelp(host);
            return;
        }

        plugin.team.setTeamSize(teamSize);

        plugin.utils.broadcast("Team size set to <blue>" + plugin.team.getTeamSizeDisplayName() + "</blue>.");
        switch (teamSize) {
            case RANDOM,TIMED_RANDOM -> plugin.utils.message(host, "Before you randomize, be sure to use <blue>/team limit</blue> to randomize teams with your desired team size.");

            case CAPTAINS -> plugin.utils.message(host, "Use <blue>/team limit</blue> to set Double/Triple Captains.");

            case AUCTION -> plugin.utils.message(host, "Use <blue>/team limit</blue> to set Double/Triple Auction.");

            case INCAPTAINS -> plugin.utils.message(host, "Use <blue>/team limit</blue> to set Double/Triple Incaptains.");
        }
    }

    private void handleLimit(Player host, String[] args) {
        if (doesntHasHostPermission(host)) return;

        if (args.length < 2) {
            plugin.utils.message(host, SOUND_NO, "<red>Usage: <white>/team limit <number></white>",
                    "<red>Remember that team limit works differently between Chosen-like teamsizes and Captains-like teamsizes.",
                    "<gray>In Chosen-like teamsizes, it sets the maximum limit of players in each team.",
                    "<gray>In Captains-like teamsizes, it sets the amount of players that are picked in each round.");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            plugin.utils.message(host, SOUND_NO, "<red>Invalid number.",
                    "<red>Usage: <white>/team limit <number></white>",
                    "<red>Remember that team limit works differently between Chosen-like teamsizes and Captains-like teamsizes.",
                    "In Chosen-like teamsizes, it sets the maximum limit of players in each team.",
                    "In Captains-like teamsizes, it sets the amount of players that are picked in each round.");
            return;
        }

        plugin.team.setTeamLimit(size);
        plugin.utils.broadcast("Team size set to <blue>" + plugin.team.getTeamSizeDisplayName() + "</blue>.");
    }

    private void handleAdd(Player host, String[] args) {
        if (doesntHasHostPermission(host)) return;
        if (args.length < 3) {
            plugin.utils.message(host, SOUND_NO, "<red>Usage: <white>/team add <player> <leader></white>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        Player leader = Bukkit.getPlayerExact(args[2]);
        if (target == null || leader == null) {
            plugin.utils.message(host, SOUND_NO, "<red>Both players must be online.");
            return;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);
        UHCPlayer leaderUhc = plugin.player.getUHCPlayer(leader);

        UHCTeam team = leaderUhc.getTeam();
        if (team == null) team = new UHCTeam(leaderUhc);

        if (isTeamFull(team)) {
            plugin.utils.message(host, SOUND_NO, "<red>That team is full.");
            return;
        }

        team.addMember(targetUhc);
        plugin.utils.message(host, SOUND_OK, "<green>Added <blue>" + target.getName() + "</blue > to <white>" + leader.getName() + "</white>'s team.");
    }

    private void handleRemove(Player host, String[] args) {
        if (doesntHasHostPermission(host)) return;
        if (args.length < 2) {
            plugin.utils.message(host, SOUND_NO, "<red>Usage: <white>/team remove <player></white>");
            return;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(args[1]);
        if (targetUhc == null) {
            plugin.utils.message(host, SOUND_NO, "<red>That player hasn't logged during this UHC.");
            return;
        }

        UHCTeam team = targetUhc.getTeam();
        if (team == null) {
            plugin.utils.message(host, SOUND_NO, "<red>That player has no team.");
            return;
        }

        team.removeMember(targetUhc);
        plugin.utils.message(host, SOUND_OK, "<green>Removed <white>" + targetUhc.getName() + "</white> from their team.");
    }

    private void handleCheck(Player host, String[] args) {
        if (!doesntHasHostPermission(host)) return;

        if (args.length < 2) {
            plugin.utils.message(host, SOUND_NO, "<red>Usage: <white>/team check <player></white>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            plugin.utils.message(host, SOUND_NO, "<red><aqua>" + args[1] + "</aqua> isn't online.");
            return;
        }

        UHCPlayer targetUhc = plugin.player.getUHCPlayer(target);

        UHCTeam team = targetUhc.getTeam();
        if (team == null) {
            plugin.utils.message(host, SOUND_NO, "<red>This player has no team.");
            return;
        }

        String membersText = team.getMembers().stream()
                .map(UHCPlayer::getName)
                .collect(Collectors.joining(", "));
        String color = team.getColorTag();

        plugin.utils.message(host, "<gray>Team info of " + color + target.getName() + "<gray>:</gray>",
                "<gray>Team:</gray> " + color + team.getTeamNumber(),
                "<gray>Members:</gray> " + color + membersText,
                "<gray>Team Kills:</gray> " + color + team.getKills(),
                "<gray>Members Alive:</gray> " + color + team.getPlayingMemberCount()
        );
    }

    private void handleRandom(Player host, String[] args) {
        if (!doesntHasHostPermission(host)) return;

        if (args.length < 2) {
            sendRandomHelp(host);
            return;
        }

        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "force" -> plugin.team.randomForce();
            case "available" -> plugin.team.randomAvailable();
            case "fill" -> plugin.team.randomFill();
            default -> sendRandomHelp(host);
        }
    }

    private void sendRandomHelp(Player player) {
        plugin.utils.message(player, SOUND_NO,
                "<red>Usage: <white>/team random <force|available|fill></white>",
                "<gray>Force: Clears all teams and randomizes everyone</gray>",
                "<gray>Available: Randomizes only players without a team</gray>",
                "<gray>Fill: Balances existing teams to be as even as possible</gray>"
        );
    }

    private boolean isSpectating(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            plugin.utils.message(player, SOUND_NO, "<red>You can't use team commands while spectating.");
            return true;
        }
        return false;
    }

    private boolean doesntHasHostPermission(Player player) {
        if (!plugin.utils.hasHostPermission(player)) {
            plugin.utils.message(player, SOUND_NO, "<red>No permission.");
            return true;
        }
        return false;
    }

    private boolean cantCreateNewTeams(Player player) {
        if (plugin.team.getTeamSize() != TeamSize.CHOSEN) {
            plugin.utils.message(player, SOUND_NO, "<red>The team-size is not Chosen, so team management is disabled.");
            return true;
        }
        return false;
    }

    private boolean isTeamFull(UHCTeam team) {
        return (team.getMembers().size() >= plugin.team.getTeamLimit()) && plugin.team.getTeamSize() == TeamSize.CHOSEN;
    }

    private void sendSizeHelp(Player player) {
        plugin.utils.message(player, SOUND_NO,
                "<red>Usage: <white>/team size <type></white>",
                "<gray>Types:</gray> <white>ffa</white><gray>,</gray> <white>infinite_allies</white><gray>,</gray> <white>chosen</white><gray>,</gray> <white>random</white><gray>,</gray> <white>timed_random</white><gray>,</gray> <white>lafs</white><gray>,</gray> <white>captains</white><gray>,</gray> <white>incaptains</white><gray>,</gray> <white>auction</white><gray>,</gray> <white>drafters</white><gray>,</gray> <white>picked</white>",
                "<gray>Team limit / pick quantity is configured with <white>/team limit</white>.</gray>"
        );
    }

    private TeamSize parseTeamSize(String input) {
        String key = input.toUpperCase(Locale.ROOT).replace('-', '_');
        try {
            return TeamSize.valueOf(key);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }


    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!(sender instanceof Player player)) return list;

        boolean host = plugin.utils.hasHostPermission(player);
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
        String sub = args[0].toLowerCase(Locale.ROOT);

        switch (args.length) {
            case 1 -> {
                list.addAll(List.of("help", "invite", "accept", "leave"));
                if (host) {
                    list.addAll(List.of("size", "add", "remove", "random", "check", "limit"));
                }
            }
            case 2 -> {
                switch (sub) {
                    case "invite" -> list.addAll(plugin.player.getOnlinePlayers().stream().map(UHCPlayer::getName).toList());
                    case "accept" -> list.addAll(uhcPlayer.getTeamInvites().stream().filter(UHCPlayer::isOnline).map(UHCPlayer::getName).toList());
                    case "add" -> {
                        if (host) {
                            list.addAll(plugin.player.getOnlinePlayers().stream().map(UHCPlayer::getName).toList());
                        }
                    }
                    case "remove" -> {
                        if (host) {
                            list.addAll(plugin.player.getOnlinePlayers().stream().filter(UHCPlayer::hasTeam).map(UHCPlayer::getName).toList());
                        }
                    }
                    case "random" -> {
                        if (host) {
                            list.addAll(List.of("force", "available", "fill"));
                        }
                    }
                    case "size" -> {
                        if (host) {
                            list.addAll(Arrays.stream(TeamSize.values()).map(size -> size.name().toLowerCase()).toList());
                        }
                    }
                }
            }
            case 3 -> {
                if (host && sub.equals("add")) {
                    list.addAll(plugin.player.getOnlinePlayers().stream().map(UHCPlayer::getName).toList());
                }
            }
        }

        list.removeIf(s -> s == null || !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(list);
        return list;
    }
}
