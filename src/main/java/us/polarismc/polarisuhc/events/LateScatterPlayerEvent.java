package us.polarismc.polarisuhc.events;

import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class LateScatterPlayerEvent extends UHCPlayerLoadoutEvent {
    public LateScatterPlayerEvent(UHCPlayer uhcPlayer, Main plugin) {
        super(uhcPlayer, plugin);
    }
}

