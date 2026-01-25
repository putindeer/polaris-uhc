package us.polarismc.polarisuhc.events;

import lombok.Getter;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

@Getter
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class TeamLeaveEvent extends UHCEvent {
    private final UHCPlayer player;
    private final UHCTeam team;
    public TeamLeaveEvent(UHCPlayer player, UHCTeam team) {
        this.player = player;
        this.team = team;
    }
}
