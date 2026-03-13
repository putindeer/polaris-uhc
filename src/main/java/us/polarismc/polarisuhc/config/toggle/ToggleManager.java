package us.polarismc.polarisuhc.config.toggle;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

@Getter
@Setter
public class ToggleManager {
    private final Main plugin;
    private final Map<ToggleSetting, ToggleHandler> handlers = new EnumMap<>(ToggleSetting.class);

    public ToggleManager(Main plugin) {
        this.plugin = plugin;

        for (ToggleSetting setting : ToggleSetting.values()) {
            ToggleHandler handler = setting.create();
            handlers.put(setting, handler);
        }
    }

    public ToggleHandler getHandler(ToggleSetting setting) {
        return handlers.get(setting);
    }

    public void toggleSetting(ToggleSetting setting) {
        ToggleHandler handler = handlers.get(setting);
        handler.toggle();
    }

    public void toggleSettingFromUI(Player player, ToggleSetting setting, BiFunction<Player, Main, FastInv> guiCreator) {
        toggleSetting(setting);
        boolean nowEnabled = isEnabled(setting);
        player.playSound(Sound.sound(
                nowEnabled ? SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON : SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF,
                Sound.Source.MASTER, 10, 1));
        guiCreator.apply(player, plugin);
    }

    public boolean isEnabled(ToggleSetting setting) {
        return handlers.get(setting).isEnabled();
    }

    // Helpers

    public boolean isAdvancements() { return isEnabled(ToggleSetting.ADVANCEMENTS); }
    public boolean isAntiItemDestruction() { return isEnabled(ToggleSetting.ANTI_ITEM_DESTRUCTION); }
    public boolean isAutoLS() { return isEnabled(ToggleSetting.AUTOLS); }
    public boolean isAutoMiningWarn() { return isEnabled(ToggleSetting.AUTO_MINING_WARN); }
    public boolean isBookshelves() { return isEnabled(ToggleSetting.BOOKSHELVES); }
    public boolean isEnd() { return isEnabled(ToggleSetting.END); }
    public boolean isExplosives() { return isEnabled(ToggleSetting.EXPLOSIVES); }
    public boolean isFireAspect() { return isEnabled(ToggleSetting.FIRE_ASPECT); }
    public boolean isFlame() { return isEnabled(ToggleSetting.FLAME); }
    public boolean isHorses() { return isEnabled(ToggleSetting.HORSES); }
    public boolean isMobs() { return isEnabled(ToggleSetting.MOBS); }
    public boolean isNerfedStrength() { return isEnabled(ToggleSetting.NERFED_STRENGTH); }
    public boolean isNether() { return isEnabled(ToggleSetting.NETHER); }
    public boolean isNotch() { return isEnabled(ToggleSetting.NOTCH); }
    public boolean isStarterBooks() { return isEnabled(ToggleSetting.STARTER_BOOKS); }
    public boolean isStats() { return isEnabled(ToggleSetting.STATS); }
    public boolean isTrades() { return isEnabled(ToggleSetting.TRADES); }
}
