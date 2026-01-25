package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "Unbreakable", author = "putindeer", icon = Material.BEDROCK,
        description = "Every tool, weapon or armor piece is unbreakable.")
public class Unbreakable extends BaseScenario {
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }
}