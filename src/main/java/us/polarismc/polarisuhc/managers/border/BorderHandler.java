package us.polarismc.polarisuhc.managers.border;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class BorderHandler {
    private final List<Integer> values;
    private final Main plugin;
    private final Player player;
    private final BiFunction<Player, Main, FastInv> guiCreator;
    private final Supplier<Integer> getter;
    private final Consumer<Integer> setter;

    public BorderHandler(Main plugin, String path, Player player, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        List<Integer> loadedValues = plugin.getConfig().getIntegerList(path).stream()
                .filter(v -> v > 0)
                .sorted()
                .toList();
        this.values = loadedValues.isEmpty() ? List.of(2000) : loadedValues;
        this.getter = getter;
        this.plugin = plugin;
        this.player = player;
        this.guiCreator = guiCreator;
        this.setter = setter;
    }

    public void increase() {
        if (isMaximum()) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = getter.get();
        int newValue = values.stream()
                .filter(v -> v > value)
                .findFirst()
                .orElse(value);
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
        setter.accept(newValue);
        guiCreator.apply(player, plugin);
    }

    public void decrease() {
        if (isMinimum()) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = getter.get();
        List<Integer> minimumValues = values.stream()
                .filter(v -> v < value)
                .toList();
        int newValue = minimumValues.isEmpty() ? value : minimumValues.getLast();
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        setter.accept(newValue);
        guiCreator.apply(player, plugin);
    }

    public int closestLowestValue() {
        int value = getter.get();
        List<Integer> minimumValues = values.stream()
                .filter(v -> v < value)
                .toList();
        return minimumValues.isEmpty() ? value : minimumValues.getLast();
    }

    public int closestHighestValue() {
        int value = getter.get();
        return values.stream()
                .filter(v -> v > value)
                .findFirst()
                .orElse(value);
    }

    public boolean isMinimum() {
        return getter.get() <= values.getFirst();
    }

    public boolean isMaximum() {
        return getter.get() >= values.getLast();
    }
}

