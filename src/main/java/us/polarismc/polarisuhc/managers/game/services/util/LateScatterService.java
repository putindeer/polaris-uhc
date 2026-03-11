package us.polarismc.polarisuhc.managers.game.services.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

public class LateScatterService {
    private final Main plugin;
    public LateScatterService(Main plugin) {
        this.plugin = plugin;
    }

    public void lateScatterTeleport(Player player) {
        //TODO - port this
        Location target = plugin.game.locationService.getScatterLocationFor(player);
        if (target == null) return;
        player.teleport(target);
        plugin.player.getUHCPlayer(player).setAlive();
    }
}

