package us.polarismc.polarisuhc.events;

import lombok.Getter;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

@Getter
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class UHCDeathEvent extends UHCEvent {
    private final PlayerDeathEvent deathEvent;
    private final UHCPlayer player;
    public UHCDeathEvent(PlayerDeathEvent deathEvent, UHCPlayer player) {
        this.deathEvent = deathEvent;
        this.player = player;
    }
}
