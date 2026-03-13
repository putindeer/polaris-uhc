package us.polarismc.polarisuhc.events;

import lombok.Getter;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.List;

@Getter
public class MeetupStartEvent extends UHCEvent {
    private final List<UHCPlayer> alivePlayers;
    private final List<UHCPlayer> teleportedPlayers;
    public MeetupStartEvent(List<UHCPlayer> alivePlayers, List<UHCPlayer> teleportedPlayers) {
        this.alivePlayers = alivePlayers;
        this.teleportedPlayers = teleportedPlayers;
    }
}
