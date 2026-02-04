package us.polarismc.polarisuhc.managers.arena;

import lombok.Getter;
import lombok.Setter;
import us.polarismc.polarisuhc.Main;

@SuppressWarnings("FieldCanBeLocal")
@Getter
@Setter
public class ArenaManager {
    private final Main plugin;
    private boolean enabled = false;
    public ArenaManager(Main plugin) {
        this.plugin = plugin;
    }

    public void disable() {
        enabled = false;
    }
}
