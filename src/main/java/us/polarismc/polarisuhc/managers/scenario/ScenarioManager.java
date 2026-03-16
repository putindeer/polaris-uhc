package us.polarismc.polarisuhc.managers.scenario;

import org.bukkit.command.CommandSender;
import us.polarismc.polarisuhc.Main;

import java.util.*;

public class ScenarioManager {
    private final Map<ScenarioType, BaseScenario> scenarios = new HashMap<>();
    private final Main plugin;

    public ScenarioManager(Main plugin) {
        this.plugin = plugin;
        loadScenarios();
    }

    public boolean hasEnabledNetherInMeetup() {
        return scenarios.values().stream().filter(BaseScenario::isEnabled).anyMatch(BaseScenario::enablesNetherInMeetup);
    }

    public boolean hasDisabledOverworld() {
        return scenarios.values().stream().filter(BaseScenario::isEnabled).anyMatch(BaseScenario::disablesOverworld);
    }

    public boolean hasEnabledMiningInMeetup() {
        return scenarios.values().stream().filter(BaseScenario::isEnabled).anyMatch(BaseScenario::enablesMiningInMeetup);
    }

    private void loadScenarios() {
        for (ScenarioType type : ScenarioType.values()) {
            Class<?> scenarioClass = type.getScenarioClass();
            try {
                if (BaseScenario.class.isAssignableFrom(scenarioClass)) {
                    BaseScenario scenario = (BaseScenario) scenarioClass.getDeclaredConstructor().newInstance();
                    registerScenario(scenario, type);
                }
            } catch (Exception e) {
                plugin.utils.severe("Scenario must be annotated with @Scenario: " + this.getClass().getSimpleName());
            }
        }
    }

    private void registerScenario(BaseScenario scenario, ScenarioType type) {
        scenarios.put(type, scenario);
    }

    public BaseScenario get(ScenarioType type) {
        return scenarios.get(type);
    }

    public Map<ScenarioType, BaseScenario> getAll() {
        return Map.copyOf(scenarios);
    }

    private void enable(ScenarioType type) {
        BaseScenario scenario = scenarios.get(type);
        scenario.enable();
    }

    private void disable(ScenarioType type) {
        BaseScenario scenario = scenarios.get(type);
        scenario.disable();
    }

    public void toggle(ScenarioType type) {
        BaseScenario scenario = scenarios.get(type);
        if (scenario.isEnabled()) {
            disable(type);
        } else {
            enable(type);
        }
    }

    public void toggle(String name, CommandSender sender) {
        ScenarioType type = getType(name);
        if (type == null) {
            plugin.utils.message(sender, "<red>Scenario not found!");
            return;
        }
        toggle(type);
    }

    private ScenarioType getType(String name) {
        for (Map.Entry<ScenarioType, BaseScenario> entry : scenarios.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<BaseScenario> getEnabled() {
        return scenarios.values().stream()
                .filter(BaseScenario::isEnabled)
                .toList();
    }
}
