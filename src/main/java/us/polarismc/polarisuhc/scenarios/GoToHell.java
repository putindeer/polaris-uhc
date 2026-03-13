package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "GoToHell", author = "putindeer", icon = Material.NETHERRACK,
        description = "If you aren't in the <red>Nether</red> during Meetup, you will receive damage.",
        enablesNetherInMeetup = true,
        inDevelopment = true)
public class GoToHell extends BaseScenario {
}
