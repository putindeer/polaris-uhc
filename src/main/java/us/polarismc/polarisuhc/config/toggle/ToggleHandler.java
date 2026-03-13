package us.polarismc.polarisuhc.config.toggle;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import us.polarismc.polarisuhc.Main;

public abstract class ToggleHandler implements Listener {
    protected final Main plugin = Main.getInstance();
    private final ToggleInfo info;
    @Getter private boolean enabled = false;

    protected ToggleHandler() {
        this.info = getClass().getAnnotation(ToggleInfo.class);
        if (info == null) {
            throw new IllegalStateException("Missing @ToggleInfo on " + getClass().getSimpleName());
        }
        setDefault(info);
    }

    public final void enable() {
        enabled = true;
        if (info.listener() && !info.listenerWhenDisabled()) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else if (info.listener()) {
            HandlerList.unregisterAll(this);
        }
        plugin.utils.broadcast(info.displayName() + " has been <blue>enabled</blue>.");
        onEnable();
    }

    public final void disable() {
        enabled = false;
        if (info.listener() && info.listenerWhenDisabled()) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else if (info.listener()) {
            HandlerList.unregisterAll(this);
        }
        plugin.utils.broadcast(info.displayName() + " has been <blue>disabled</blue>.");
        onDisable();
    }

    public final void toggle() {
        if (enabled) disable();
        else enable();
    }

    private void setDefault(ToggleInfo info) {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle");
        if (config == null) return;
        enabled = config.getBoolean(info.id());
        if (enabled && !info.listenerWhenDisabled()) {
            enable();
        } else if (info.listenerWhenDisabled() && !enabled) {
            disable();
        }
    }

    protected void onEnable() {}
    protected void onDisable() {}
}
