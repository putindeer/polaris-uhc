package us.polarismc.polarisuhc.managers.scenario;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import us.polarismc.polarisuhc.Main;

import java.util.*;

/**
 * Manager unificado que escanea, registra y controla scenarios
 */
@Slf4j
public class ScenarioManager {
    private final Map<String, BaseScenario> scenarios = new HashMap<>();
    private final Main plugin;

    public ScenarioManager(Main plugin) {
        this.plugin = plugin;
        loadScenarios();
    }

    private void loadScenarios() {
        Reflections reflections = new Reflections("us.polarismc.polarisuhc.scenarios");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Scenario.class);

        for (Class<?> scenarioClass : annotated) {
            try {
                if (BaseScenario.class.isAssignableFrom(scenarioClass)) {
                    BaseScenario scenario = (BaseScenario) scenarioClass.getDeclaredConstructor().newInstance();
                    registerScenario(scenario);
                }
            } catch (Exception e) {
                plugin.utils.severe("Scenario must be annotated with @Scenario: " + this.getClass().getSimpleName());
            }
        }
    }

    public void registerScenario(BaseScenario scenario) {
        scenarios.put(scenario.getName().toLowerCase(), scenario);
    }

    public BaseScenario getScenario(String name) {
        return scenarios.get(name.toLowerCase());
    }

    public Map<String, BaseScenario> getAllScenarios() {
        return Map.copyOf(scenarios);
    }

    public boolean enable(String name) {
        BaseScenario scenario = scenarios.get(name.toLowerCase());
        if (scenario != null) {
            scenario.enable();
            return true;
        }
        return false;
    }

    public boolean disable(String name) {
        BaseScenario scenario = scenarios.get(name.toLowerCase());
        if (scenario != null) {
            scenario.disable();
            return true;
        }
        return false;
    }

    public List<BaseScenario> getEnabledScenarios() {
        return scenarios.values().stream()
                .filter(BaseScenario::isEnabled)
                .toList();
    }
}
