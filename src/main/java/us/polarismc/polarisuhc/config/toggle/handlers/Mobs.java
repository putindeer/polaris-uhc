package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.GameRules;
import org.bukkit.Material;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "mobs", icon = Material.ZOMBIE_SPAWN_EGG, displayName = "Mobs",
        listener = false)
public class Mobs extends ToggleHandler {
    @Override
    protected void onEnable() {
        super.onEnable();
        plugin.uhc.world.applyGameruleToPlayingWorlds(GameRules.SPAWN_MOBS, true);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        plugin.uhc.world.applyGameruleToPlayingWorlds(GameRules.SPAWN_MOBS, false);
    }
}