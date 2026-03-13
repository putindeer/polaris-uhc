package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "Hades", author = "putindeer", icon = Material.GOLDEN_SWORD,
        description = {"<green>Overworld</green> is disabled. You spawn in the <red>Nether</red>.",
                "Quartz drops <white>iron nuggets</white>.",
                "Gilded Blackstone drops <aqua>diamonds</aqua>.",
                "Nether leaves (Wart Blocks) drop <red>apples</red>.",
                "Blackstone veins may contain <aqua>Gilded Blackstone</aqua>.",
                "The <red>Nether</red> has no lava.",
                "You start with a <white>stone pickaxe</white>."},
        enablesNetherInMeetup = true,
        disablesOverworld = true,
        inDevelopment = true)
public class Hades extends BaseScenario {
}