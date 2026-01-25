package us.polarismc.polarisuhc.managers.team;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.TeamJoinEvent;
import us.polarismc.polarisuhc.events.TeamLeaveEvent;
import us.polarismc.polarisuhc.managers.channel.ChannelKey;
import us.polarismc.polarisuhc.managers.channel.TeamChannel;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class UHCTeam {
    private final Main plugin;
    private final Team team;

    private final List<UHCPlayer> members = new ArrayList<>();

    private final int teamNumber;
    private final String teamName;

    private TeamColor color;
    private String colorTag;
    private NamedTextColor fallbackColor;
    private String emoji;
    private final ChannelKey channel;

    @Setter private Inventory teamInventory;

    @Setter private int kills = 0;

    public UHCTeam(UHCPlayer leader) {
        this.plugin = Main.getInstance();

        members.add(leader);
        leader.setTeam(this);

        this.teamNumber = plugin.team.getNewTeamNumber();
        this.teamName = String.valueOf(teamNumber);

        this.color = plugin.team.getRandomAvailableColor();
        this.colorTag = color.getMiniMessageHex();
        this.fallbackColor = color.getFallbackColor();
        this.emoji = plugin.team.getAvailableEmoji(color);

        this.channel = new TeamChannel(this);
        leader.addChannel(channel);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.team = scoreboard.getTeam(teamName) != null ? scoreboard.getTeam(teamName) : scoreboard.registerNewTeam(teamName);

        applyScoreboardStyle();

        assert team != null;
        team.addEntry(leader.getName());
        updatePlayerDisplay(leader.getPlayer());

        plugin.team.getTeams().add(this);
    }

    //TODO - tratar de hacer que se pueda ver el color en el nametag del player

    private void applyScoreboardStyle() {
        team.prefix(getPrefixComponent());
        team.color(fallbackColor);
    }

    private void updatePlayerDisplay(Player player) {
        if (player == null || !player.isOnline()) return;

        Component tag = plugin.utils.chat(getPrefix() + player.getName());

        player.playerListName(tag);
        player.displayName(tag);
    }

    private void updateAllPlayersDisplay() {
        getOnlineMembers().forEach(this::updatePlayerDisplay);
    }

    public String getPrefix() {
        return colorTag + emoji + " ";
    }

    public Component getPrefixComponent() {
        return plugin.utils.chat(getPrefix());
    }

    public void addMember(UHCPlayer uhcPlayer) {
        members.add(uhcPlayer);
        team.addEntry(uhcPlayer.getName());
        uhcPlayer.setTeam(this);

        updatePlayerDisplay(uhcPlayer.getPlayer());
        uhcPlayer.addChannel(channel);
        Bukkit.getPluginManager().callEvent(new TeamJoinEvent(uhcPlayer, this));
    }

    public void removeMember(UHCPlayer uhcPlayer) {
        members.remove(uhcPlayer);
        team.removeEntry(uhcPlayer.getName());
        uhcPlayer.setTeam(null);
        uhcPlayer.removeChannel(channel);

        if (uhcPlayer.isOnline()) {
            Player player = uhcPlayer.getPlayer();
            if (player != null) {
                Component base = Component.text(player.getName());
                player.playerListName(base);
                player.displayName(base);
            }
        }

        if (members.isEmpty()) {
            plugin.team.deleteTeam(this);
        }

        Bukkit.getPluginManager().callEvent(new TeamLeaveEvent(uhcPlayer, this));
    }

    public void setColor(TeamColor color) {
        this.color = color;
        this.colorTag = color.getMiniMessageHex();
        this.fallbackColor = color.getFallbackColor();
        applyScoreboardStyle();
        updateAllPlayersDisplay();
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
        applyScoreboardStyle();
        updateAllPlayersDisplay();
    }

    public void addKill() {
        kills++;
    }

    public boolean contains(UHCPlayer player) {
        return members.contains(player);
    }

    public boolean contains(Player player) {
        return members.stream().anyMatch(member -> Objects.equals(member.getPlayer(), player));
    }

    public int getMembersCount() {
        return members.size();
    }

    public int getPlayingMemberCount() {
        return (int) members.stream().filter(UHCPlayer::isPlaying).count();
    }

    public boolean hasOnlinePlayers() {
        return members.stream().anyMatch(UHCPlayer::isOnline);
    }

    public boolean hasAlivePlayers() {
        return members.stream().anyMatch(UHCPlayer::isPlaying);
    }

    public List<Player> getOnlineMembers() {
        return members.stream().filter(UHCPlayer::isOnline).map(UHCPlayer::getPlayer).toList();
    }

    public List<UHCPlayer> getAliveMembers() {
        return members.stream().filter(UHCPlayer::isPlaying).toList();
    }

    public List<UHCPlayer> getOnlineUHCMembers() {
        return members.stream().filter(UHCPlayer::isOnline).toList();
    }

    //TODO - find a way of implementing PREFIX.TEAM, PREFIX.GLOBAL to mcdev-utils
    public void sendMessage(String message) {
        plugin.utils.message(getOnlineMembers(), false, plugin.utils.teamPrefix() + message);
    }

    public void sendMessage(Player player, String message) {
        plugin.utils.message(getOnlineMembers(), false, plugin.utils.teamPrefix(player) + message);
    }
}
