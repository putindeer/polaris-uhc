package us.polarismc.polarisuhc.managers.player;

import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Data;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.channel.ChannelKey;
import us.polarismc.polarisuhc.managers.channel.GlobalChannel;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.*;

@Data
public class UHCPlayer {
    private final Main plugin;
    private UHCTeam team = null;
    private final UUID uniqueId;
    private final String name;
    private final Set<UHCPlayer> teamInvites = new HashSet<>();
    private final EnumMap<MinedResource, Integer> minedResources = new EnumMap<>(MinedResource.class);

    private String inventory;
    private int warnAmount = 0;

    private ChannelKey sendChannel = new GlobalChannel();
    private final Set<ChannelKey> receiveChannels = new HashSet<>();
    private TextDisplay nametag = null;

    private boolean arena = false;
    private boolean isEditingKit = false;

    public UHCPlayer(Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public UHCPlayer(UUID uniqueId, String name) {
        this.plugin = Main.getInstance();

        this.uniqueId = uniqueId;
        this.name = name;
        Arrays.stream(MinedResource.values()).forEach(resource -> minedResources.put(resource, 0));
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    public String getDisplayName() {
        OfflinePlayer player = getOfflinePlayer();
        if (team == null) {
            return player.getName();
        }

        return team.getPrefix() + player.getName();
    }

    public void updateDisplayName() {
        plugin.info.nametag.updateText(this);
    }

    public Component getDisplayNameComponent() {
        return plugin.utils.chat(getDisplayName());
    }

    public boolean isOnline() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uniqueId);
        return player.isOnline();
    }

    public boolean isPlaying() {
        return plugin.uhc.getAlivePlayers().stream().map(OfflinePlayer::getName).toList().contains(getName());
    }

    public boolean isDead() {
        return plugin.uhc.getDeadPlayers().stream().map(OfflinePlayer::getName).toList().contains(getName());
    }

    public boolean isInTeamWith(UHCPlayer player) {
        return team != null && team.contains(player);
    }

    public boolean isReceivingFromChannel(ChannelKey channel) {
        return receiveChannels.contains(channel);
    }

    public void addChannel(ChannelKey key) {
        receiveChannels.add(key);
    }

    public void removeChannel(ChannelKey key) {
        receiveChannels.remove(key);
    }

    public void resetSendChannel() {
        sendChannel = new GlobalChannel();
    }

    public void addTeamInvite(UHCPlayer inviter) {
        teamInvites.add(inviter);
    }

    public boolean removeTeamInvite(UHCPlayer inviter) {
        return teamInvites.remove(inviter);
    }

    public boolean hasTeamInvite(UHCPlayer inviter) {
        return teamInvites.contains(inviter);
    }

    public boolean hasTeam() {
        return team != null;
    }

    public void addMinedResource(MinedResource resource) {
        minedResources.merge(resource, 1, Integer::sum);
    }

    public void addMinedResource(MinedResource resource, int amount) {
        minedResources.merge(resource, amount, Integer::sum);
    }

    public int getMinedResource(MinedResource resource) {
        return minedResources.getOrDefault(resource, 0);
    }

    public void warn(String reason) {
        warnAmount++;
        if (isOnline()) {
            Player player = getPlayer();
            if (player == null) return;
            plugin.utils.message(player, Sound.sound(SoundEventKeys.ENTITY_ELDER_GUARDIAN_CURSE, Sound.Source.MASTER, 1, 1),
                    "<red>You were warned. <dark_gray>(<aqua>" + warnAmount + "</aqua> / 2)",
                    "<red>Reason: <blue>" + reason);
            plugin.utils.title(player, "<red>You were warned!", "<red>Reason: <blue>" + reason,
                    plugin.utils.timesFromTicks(10, 40, 10));
            plugin.utils.broadcast("<red>" + getName() + " <dark_gray>(<aqua>" + warnAmount + "</aqua> / 2)<red> was warned for <blue>" + reason + ".");
            if (warnAmount >= 2) {
                player.damage(10000);
            }
        }
    }

    //TODO - arena kits
}

