package us.polarismc.polarisuhc.events;

import lombok.Getter;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.List;

@Getter
public class MeetupStartEvent extends UHCEvent {
    List<UHCPlayer> alivePlayers;
    public MeetupStartEvent(List<UHCPlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }
}
