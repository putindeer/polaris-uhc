package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import us.polarismc.polarisuhc.scenarios.*;

@Getter
public enum ScenarioType {
    CUT_CLEAN(CutClean.class),
    FORTUNE_BABIES(FortuneBabies.class),
    FORTUNE_BOYS(FortuneBoys.class),
    FORTUNE_BOYS_PLUS(FortuneBoysPlus.class),
    HASTEY_BABIES(HasteyBabies.class),
    HASTEY_BOYS(HasteyBoys.class),
    HASTEY_BOYS_PLUS(HasteyBoysPlus.class),
    TEAM_INVENTORY(TeamInventory.class),
    UNBREAKABLE(Unbreakable.class);

    private final Class<? extends BaseScenario> scenarioClass;

    ScenarioType(Class<? extends BaseScenario> scenarioClass) {
        this.scenarioClass = scenarioClass;
    }
}