package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;

@Scenario(name = "CutClean", author = "putindeer", icon = Material.IRON_INGOT,
        description = "Ores are auto-smelted and animals drop cooked meat.")
public class CutClean extends BaseScenario implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<ItemStack> drops = new ArrayList<>();
        event.getDrops().forEach(drop -> {
            switch (drop.getType()) {
                case PORKCHOP -> drops.add(new ItemStack(Material.COOKED_PORKCHOP, drop.getAmount()));
                case BEEF -> drops.add(new ItemStack(Material.COOKED_BEEF, drop.getAmount()));
                case CHICKEN -> drops.add(new ItemStack(Material.COOKED_CHICKEN, drop.getAmount()));
                case MUTTON -> drops.add(new ItemStack(Material.COOKED_MUTTON, drop.getAmount()));
                case RABBIT -> drops.add(new ItemStack(Material.COOKED_RABBIT, drop.getAmount()));
                case SALMON -> drops.add(new ItemStack(Material.COOKED_SALMON, drop.getAmount()));
                case COD -> drops.add(new ItemStack(Material.COOKED_COD, drop.getAmount()));
                default -> drops.add(drop);
            }
        });

        event.getDrops().clear();
        event.getDrops().addAll(drops);
    }
}