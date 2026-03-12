package us.polarismc.polarisuhc.events;

import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.List;

public class MeetupStartEvent extends UHCEvent {
    List<UHCPlayer> alivePlayers;
    public MeetupStartEvent(List<UHCPlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }
}
