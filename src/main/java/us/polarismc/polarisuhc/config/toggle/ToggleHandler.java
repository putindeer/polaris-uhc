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
        if (enabled) return;
        enabled = true;
        if (info.listener()) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        onEnable();
    }

    public final void disable() {
        if (!enabled) return;
        enabled = false;
        if (info.listener()) {
            HandlerList.unregisterAll(this);
        }
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
    }

    protected void onEnable() {}
    protected void onDisable() {}
}
