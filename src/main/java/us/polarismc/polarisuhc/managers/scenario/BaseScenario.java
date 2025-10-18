package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class BaseScenario {
    @Getter
    private boolean enabled = false;
    protected final Main plugin = Main.getInstance();

    private final Scenario annotation;

    protected BaseScenario() {
        this.annotation = this.getClass().getAnnotation(Scenario.class);
        if (annotation == null) {
            throw new RuntimeException("Scenario must be annotated with @Scenario: " + getClass().getSimpleName());
        }

        log.debug("Scenario '{}' initialized", getName());
    }

    public final String getName() {
        return annotation.name();
    }

    public final String getAuthor() {
        return annotation.author();
    }

    public final List<Component> getDescription() {
        return Arrays.stream(getDescriptionLines())
                .map(plugin.utils::chat)
                .toList();
    }

    public final ItemStack getIcon() {
        return new ItemStack(annotation.icon());
    }

    /**
     * Override para descripciones dinámicas
     */
    protected String[] getDescriptionLines() {
        return annotation.description();
    }

    public final void enable() {
        if (enabled) return;

        if (this instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) this, plugin);
            log.debug("Auto-registered events for: {}", getName());
        }

        enabled = true;
        onEnable();
        log.info("Enabled scenario: {}", getName());
    }

    public final void disable() {
        if (!enabled) return;

        if (this instanceof Listener) {
            HandlerList.unregisterAll((Listener) this);
            log.debug("Auto-unregistered events for: {}", getName());
        }

        enabled = false;
        onDisable();
        log.info("Disabled scenario: {}", getName());
    }

    // ===== MÉTODOS OPCIONALES =====
    protected void onEnable() { /* Override if needed */ }
    protected void onDisable() { /* Override if needed */ }
}