package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;

import java.util.Arrays;

public abstract class BaseScenario implements Listener {
    @Getter private boolean enabled = false;
    protected final Main plugin = Main.getInstance();

    private final Scenario annotation;

    protected BaseScenario() {
        this.annotation = this.getClass().getAnnotation(Scenario.class);
        if (annotation == null) {
            throw new RuntimeException("Scenario must be annotated with @Scenario: " + getClass().getSimpleName());
        }
    }

    public final String getName() {
        return annotation.name();
    }

    public final String getDisplayName() {
        String display = annotation.displayName();
        return display.isEmpty() ? annotation.name() : display;
    }

    public final String[] getAuthors() {
        if (annotation.authors().length > 0) {
            return annotation.authors();
        }
        return new String[]{annotation.author()};
    }

    public final String getAuthorString() {
        String[] authors = getAuthors();

        if (authors.length == 0) {
            return "Unknown";
        }

        return String.join(", ", authors);
    }

    public final String[] getCommandNames() {
        if (annotation.commands().length > 0) {
            return annotation.commands();
        }

        return new String[]{annotation.command()};
    }

    public final ItemStack getIcon() {
        return getIconStack();
    }

    public final int getPriority() {
        return annotation.priority();
    }

    public final Component[] getDescription() {
        return Arrays.stream(getDescriptionLines())
                .map(plugin.utils::chat)
                .toArray(Component[]::new);
    }

    public final ScenarioType[] getIncompatibleScenarios() {
        return annotation.incompatibleWith();
    }

    public final boolean isInDevelopment() {
        return annotation.inDevelopment();
    }

    protected String[] getDescriptionLines() {
        return annotation.description();
    }

    protected ItemStack getIconStack() {
        return new ItemStack(annotation.icon());
    }

    public final void enable() {
        if (enabled) return;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        enabled = true;
        plugin.utils.broadcast(getDisplayName() + " has been <blue>enabled</blue>.");

        registerScenarioCommands();
        onEnable();
    }

    public final void disable() {
        if (!enabled) return;

        unregisterScenarioCommands();
        HandlerList.unregisterAll(this);

        enabled = false;
        plugin.utils.broadcast(getDisplayName() + " has been <blue>disabled</blue>.");
        onDisable();
    }

    private void registerScenarioCommands() {
        for (String name : getCommandNames()) {
            if (name.isBlank()) continue;

            PluginCommand command = plugin.getCommand(name);
            if (command == null) {
                plugin.getLogger().warning("Command '" + name + "' not found in plugin.yml (scenario " + getName() + ")");
                continue;
            }

            if (this instanceof CommandExecutor executor) {
                command.setExecutor(executor);
            }
            if (this instanceof TabCompleter completer) {
                command.setTabCompleter(completer);
            }
        }
    }

    private void unregisterScenarioCommands() {
        for (String name : getCommandNames()) {
            if (name.isBlank()) continue;

            PluginCommand command = plugin.getCommand(name);
            if (command == null) continue;

            command.setExecutor((sender, cmd, label, args) -> {
                plugin.utils.message(sender, "<red>" + getDisplayName() + " isn't enabled.");
                return true;
            });
            command.setTabCompleter((sender, cmd, alias, args) -> java.util.Collections.emptyList());
        }
    }


    protected void onEnable() {}
    protected void onDisable() {}
}