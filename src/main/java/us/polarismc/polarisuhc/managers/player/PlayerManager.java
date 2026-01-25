package us.polarismc.polarisuhc.managers.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager implements Listener {
    private final Main plugin;
    @Getter private final List<UHCPlayer> playerList = new ArrayList<>();

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getUHCPlayer(player) == null) {
            playerList.add(new UHCPlayer(event.getPlayer()));
        }
    }

    public UHCPlayer getUHCPlayer(@NotNull Player player) {
        return getUHCPlayer(player.getUniqueId());
    }

    public UHCPlayer getUHCPlayer(String name) {
        return playerList.stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    public @Nullable UHCPlayer getUHCPlayer(UUID uniqueId) {
        return playerList.stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public List<UHCPlayer> getOnlinePlayers() {
        return playerList.stream().filter(UHCPlayer::isOnline).toList();
    }

    public List<UHCPlayer> getPlayingPlayers() {
        return playerList.stream().filter(UHCPlayer::isPlaying).toList();
    }

    public List<UHCPlayer> getPlayingOnlinePlayers() {
        return playerList.stream().filter(UHCPlayer::isOnline).filter(UHCPlayer::isPlaying).toList();
    }
}
