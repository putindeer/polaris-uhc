package us.polarismc.polarisuhc.managers.game.services;

import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.PvPStartEvent;


public class PvPStartService {
    private final Main plugin;
    public PvPStartService(Main plugin) {
        this.plugin = plugin;
    }

    public void startPvP() {
        plugin.utils.broadcast(SoundEventKeys.BLOCK_BEACON_POWER_SELECT,
                "<aqua>PvP<gray> has been enabled, good luck!");
        plugin.uhc.world.getUhcWorld().setGameRule(GameRules.PVP, true);
        if (plugin.uhc.toggle.isNether()) {
            plugin.uhc.world.getNetherWorld().setGameRule(GameRules.PVP, true);
        }
        Bukkit.getPluginManager().callEvent(new PvPStartEvent());
        //TODO - add whitelist implementation
    }
}
