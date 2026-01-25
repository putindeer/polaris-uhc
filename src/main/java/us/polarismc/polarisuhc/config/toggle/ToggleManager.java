package us.polarismc.polarisuhc.config.toggle;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

import java.util.function.BiFunction;

@Getter
@Setter
public class ToggleManager {
    private final Main plugin;

    public ToggleManager(Main plugin) {
        this.plugin = plugin;
        initializeToggle();
    }

    private boolean advancements;
    private boolean antiBurn;
    private boolean autoLS;
    private boolean bookshelves;
    private boolean end;
    private boolean explosives;
    private boolean fireAspect;
    private boolean flame;
    private boolean horses;
    private boolean mobs;
    private boolean nerfedStrength;
    private boolean nether;
    private boolean notch;
    private boolean starterBooks;
    private boolean stats;
    private boolean trades;

    private void initializeToggle() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle");
        assert config != null;
        advancements = config.getBoolean("advancements");
        antiBurn = config.getBoolean("antiburn");
        autoLS = config.getBoolean("autols");
        bookshelves = config.getBoolean("bookshelves");
        end = config.getBoolean("end");
        explosives = config.getBoolean("explosives");
        fireAspect = config.getBoolean("fireaspect");
        flame = config.getBoolean("flame");
        horses = config.getBoolean("horses");
        mobs = config.getBoolean("mobs");
        nerfedStrength = config.getBoolean("nerfedstrength");
        nether = config.getBoolean("nether");
        notch = config.getBoolean("notch");
        starterBooks = config.getBoolean("starterbooks");
        stats = config.getBoolean("stats");
        trades = config.getBoolean("trades");
    }

    public void toggleSetting(Player p, ToggleSetting setting, BiFunction<Player, Main, FastInv> guiCreator) {
        setting.toggle(plugin);

        if (setting.get(plugin)) {
            p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
        } else {
            p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        }

        guiCreator.apply(p, plugin);
    }
}
