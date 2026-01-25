package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.events.PvPStartEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "Vulture", author = "putindeer", icon = Material.RECOVERY_COMPASS,
        description = "")
public class Vulture extends BaseScenario {
    //TODO - scen que muestre al jugador muerto más cercano + desc
    @EventHandler
    public void onPvP(PvPStartEvent event) {
        plugin.uhc.getPlayingPlayers().forEach(player -> plugin.utils.giveOrDrop(player, new ItemStack(Material.RECOVERY_COMPASS)));
    }
}
