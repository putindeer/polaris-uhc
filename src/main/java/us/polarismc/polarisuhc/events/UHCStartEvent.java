package us.polarismc.polarisuhc.events;

import lombok.Getter;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.List;

@Getter
public class UHCStartEvent extends UHCEvent {
    private final List<UHCPlayer> players;
    private final List<UHCPlayer> onlinePlayers;
    public UHCStartEvent(List<UHCPlayer> players, List<UHCPlayer> onlinePlayers) {
        this.players = players;
        this.onlinePlayers = onlinePlayers;
    }
}
