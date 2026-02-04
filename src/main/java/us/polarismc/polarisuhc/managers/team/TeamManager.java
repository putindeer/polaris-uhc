package us.polarismc.polarisuhc.managers.team;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Data
public class TeamManager {
    //TODO - port auction/captains/incaptains/drafters
    private final Main plugin;
    private TeamSize teamSize = TeamSize.FFA;
    private int teamLimit = 0;
    private final List<UHCTeam> teams = new ArrayList<>();

    private final List<String> prefixes = Arrays.stream(TeamPrefix.values()).map(TeamPrefix::getSymbol).toList();
    private final List<TeamColor> availableColors = Arrays.asList(TeamColor.values());
    private int lastTeamNumber = 0;
    private boolean randomized = false;

    private final Random random = ThreadLocalRandom.current();

    public TeamManager(Main plugin) {
        this.plugin = plugin;
    }

    public @Nullable UHCTeam getTeam(UUID uuid) {
        return teams.stream().filter(team -> team.getUniqueId() == uuid).findFirst().orElse(null);
    }

    public int getNewTeamNumber() {
        return ++lastTeamNumber;
    }

    public void deleteTeam(UHCTeam team) {
        team.getTeam().unregister();
        team.getMembers().forEach(uhcPlayer -> {
            uhcPlayer.setTeam(null);
            Player player = uhcPlayer.getPlayer();
            if (player != null) {
                plugin.info.nametag.ensureDisplay(player);
            }
        });
        teams.remove(team);
    }

    public List<UHCTeam> getAliveTeams() {
        return teams.stream().filter(UHCTeam::hasAlivePlayers).toList();
    }

    public String getAvailableEmoji(TeamColor color) {
        Set<String> used = new HashSet<>();
        teams.forEach(team -> used.add(team.getEmoji()));

        List<String> available = new ArrayList<>();
        prefixes.stream().filter(emoji -> !used.contains(emoji)).forEach(available::add);

        if (available.isEmpty()) {
            return getAvailableNotRepeatedEmoji(color);
        }

        return available.get(random.nextInt(available.size()));
    }

    public String getAvailableNotRepeatedEmoji(TeamColor color) {
        Set<String> used = new HashSet<>();
        teams.stream().filter(team -> team.getColor().equals(color)).forEach(team -> used.add(team.getEmoji()));

        List<String> available = new ArrayList<>();
        prefixes.stream().filter(emoji -> !used.contains(emoji)).forEach(available::add);

        if (available.isEmpty()) {
            return prefixes.get(random.nextInt(prefixes.size()));
        }

        return available.get(random.nextInt(available.size()));
    }

    public TeamColor getRandomAvailableColor() {
        Set<TeamColor> used = new HashSet<>();
        teams.forEach(team -> used.add(team.getColor()));

        List<TeamColor> available = new ArrayList<>();
        Arrays.stream(TeamColor.values()).filter(color -> !used.contains(color)).forEach(available::add);

        if (available.isEmpty()) {
            TeamColor[] allColors = TeamColor.values();
            return allColors[random.nextInt(allColors.length)];
        }

        return available.get(random.nextInt(available.size()));
    }

    public void randomForce() {
        List<UHCPlayer> players = plugin.player.getOnlinePlayers().stream()
                .filter(player -> player.getPlayer() != null)
                .filter(player -> player.getPlayer().getGameMode() != GameMode.SPECTATOR)
                .toList();

        for (UHCTeam team : new ArrayList<>(teams)) {
            for (UHCPlayer member : new ArrayList<>(team.getMembers())) {
                team.removeMember(member);
            }
        }

        List<UHCPlayer> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);
        createTeamsFromList(shuffled, teamLimit);
    }

    public void randomAvailable() {
        List<UHCPlayer> available = plugin.player.getOnlinePlayers().stream()
                .filter(player -> player.getPlayer() != null)
                .filter(player -> player.getPlayer().getGameMode() != GameMode.SPECTATOR)
                .filter(player -> player.getTeam() == null)
                .toList();

        if (available.isEmpty()) return;

        List<UHCPlayer> shuffled = new ArrayList<>(available);
        Collections.shuffle(shuffled);
        createTeamsFromList(shuffled, teamLimit);
    }

    public void randomFill() {
        List<UHCPlayer> freeAgents = plugin.player.getOnlinePlayers().stream()
                .filter(player -> player.getPlayer() != null)
                .filter(player -> player.getPlayer().getGameMode() != GameMode.SPECTATOR)
                .filter(player -> player.getTeam() == null)
                .collect(Collectors.toCollection(ArrayList::new));

        List<UHCTeam> manageable = new ArrayList<>(teams).stream()
                .filter(team -> team.getMembersCount() <= teamLimit)
                .sorted(Comparator.comparingInt(UHCTeam::getMembersCount))
                .collect(Collectors.toCollection(ArrayList::new));

        for (UHCTeam source : new ArrayList<>(manageable)) {
            if (source.getMembersCount() == 0 || source.getMembersCount() >= teamLimit) continue;

            List<UHCPlayer> toMove = new ArrayList<>(source.getMembers());
            if (distributeMembers(toMove, manageable, source, teamLimit)) {
                while (source.getMembersCount() > 0) {
                    source.removeMember(source.getMembers().getFirst());
                }
                manageable.remove(source);
            }
        }

        Collections.shuffle(freeAgents);

        while (!freeAgents.isEmpty()) {
            manageable.sort(Comparator.comparingInt(UHCTeam::getMembersCount));

            UHCPlayer player = freeAgents.removeFirst();

            UHCTeam target = manageable.stream()
                    .filter(t -> t.getMembersCount() < teamLimit)
                    .findFirst()
                    .orElse(null);

            if (target == null) {
                manageable.add(new UHCTeam(player));
                continue;
            }

            target.addMember(player);
        }
    }

    private void createTeamsFromList(List<UHCPlayer> players, int teamLimit) {
        for (int i = 0; i < players.size(); i += teamLimit) {
            List<UHCPlayer> chunk = players.subList(i, Math.min(i + teamLimit, players.size()));
            if (chunk.isEmpty()) continue;

            UHCTeam team = new UHCTeam(chunk.getFirst());
            for (int j = 1; j < chunk.size(); j++) {
                team.addMember(chunk.get(j));
            }
        }
    }

    private boolean distributeMembers(List<UHCPlayer> members, List<UHCTeam> teams, UHCTeam source, int teamLimit) {
        List<UHCTeam> targets = teams.stream()
                .filter(team -> team != source)
                .filter(team -> team.getMembersCount() < teamLimit)
                .sorted(Comparator.comparingInt(UHCTeam::getMembersCount).reversed())
                .collect(Collectors.toCollection(ArrayList::new));

        for (UHCPlayer member : members) {
            UHCTeam target = targets.stream()
                    .filter(team -> team.getMembersCount() < teamLimit)
                    .findFirst()
                    .orElse(null);

            if (target == null) return false;

            source.removeMember(member);
            target.addMember(member);
        }

        return true;
    }

    public String getTeamSizeDisplayName() {
        TeamSize size = getTeamSize();
        int limit = getTeamLimit();

        return switch (size) {
            case FFA -> "FFA";
            case INFINITE_ALLIES -> "Infinite Allies";
            case CHOSEN -> "Chosen To" + limit;
            case LAFS -> "Love at First Sight To" + limit;
            case RANDOM -> randomized ? "Random To" + limit : "Random ToX";
            case TIMED_RANDOM -> randomized ? "Timed Random To" + limit : "Timed Random ToX";
            case CAPTAINS -> formatMultiplierName("Captains", limit);
            case AUCTION -> formatMultiplierName("Auction", limit);
            case INCAPTAINS -> formatMultiplierName("Incaptains", limit);
            case DRAFTERS -> "Drafters";
            case PICKED -> "Picked ToX";
        };
    }

    private String formatMultiplierName(String base, int n) {
        return switch (n) {
            case 1 -> base;
            case 2 -> "Double " + base;
            case 3 -> "Triple " + base;
            case 4 -> "Quad " + base;
            case 5 -> "Penta " + base;
            case 6 -> "Hexa " + base;
            case 7 -> "Hepta " + base;
            case 8 -> "Octa " + base;
            case 9 -> "Nona " + base;
            case 10 -> "Deca " + base;
            default -> n + "x " + base;
        };
    }

    public void disableAndDeleteTeams() {
        Bukkit.getScoreboardManager().getMainScoreboard().getTeams().forEach(Team::unregister);
    }
}
