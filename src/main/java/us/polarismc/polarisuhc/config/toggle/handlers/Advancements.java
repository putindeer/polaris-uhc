package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.GameRules;
import org.bukkit.Material;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "advancements", icon = Material.WRITABLE_BOOK, displayName = "Advancements",
        listener = false)
public class Advancements extends ToggleHandler {
    @Override
    protected void onEnable() {
        super.onEnable();
        plugin.uhc.world.applyGameruleToPlayingWorlds(GameRules.SHOW_ADVANCEMENT_MESSAGES, true);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        plugin.uhc.world.applyGameruleToPlayingWorlds(GameRules.SHOW_ADVANCEMENT_MESSAGES, false);
    }
}