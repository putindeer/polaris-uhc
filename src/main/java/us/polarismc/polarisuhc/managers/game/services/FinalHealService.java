package us.polarismc.polarisuhc.managers.game.services;

import us.polarismc.polarisuhc.Main;

public class FinalHealService {
    private final Main plugin;
    public FinalHealService(Main plugin) {
        this.plugin = plugin;
    }

    public void giveFinalHeal() {
        plugin.uhc.getPlayingPlayers().forEach(player -> plugin.utils.setMaxHealth(player));
        plugin.utils.broadcast("<green>All players has been healed.");
    }
}
