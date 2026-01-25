package us.polarismc.polarisuhc.managers.gui;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIResult;
import de.rapha149.signgui.exception.SignGUIVersionException;
import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.polarismc.polarisuhc.Main;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUIManager {
    private final Main plugin;
    public GUIManager(Main plugin) {
        this.plugin = plugin;
    }

    public void increase(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, int maximum, BiFunction<Player, Main, FastInv> guiCreator) {
        Player player = (Player) event.getWhoClicked();
        if (getter.get() >= maximum) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = event.getClick().isRightClick() ? 5 : 1;
        int newValue = Math.min(getter.get() + value, maximum);
        float pitch = event.getClick().isRightClick() ? 1.5f : 1;
        setter.accept(newValue);
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, pitch));
        guiCreator.apply(player, plugin);
    }

    public void decrease(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, BiFunction<Player, Main, FastInv> guiCreator) {
        Player player = (Player) event.getWhoClicked();
        if (getter.get() <= minimum) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = event.getClick().isRightClick() ? 5 : 1;
        int newValue = Math.max(getter.get() - value, minimum);
        float pitch = event.getClick().isRightClick() ? 1.5f : 1;
        setter.accept(newValue);
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, pitch));
        guiCreator.apply(player, plugin);
    }

    public void openIntInputSign(Player player, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum, BiFunction<Player, Main, FastInv> guiCreator) {
        try {
            Object[] lines = new Object[] {
                    plugin.utils.chat(""),
                    plugin.utils.chat("Enter a new value"),
                    plugin.utils.chat("Current value:"),
                    plugin.utils.chat(String.valueOf(getter.get()))
            };

            SignGUI gui = SignGUI.builder().setAdventureLines(lines).setType(Material.PALE_OAK_SIGN).setHandler((p, result) -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        int value = extractFirstNumber(result);
                        value = Math.max(minimum, Math.min(value, maximum));
                        setter.accept(value);
                        p.playSound(Sound.sound(SoundEventKeys.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 10, 2));
                        guiCreator.apply(p, plugin);
                    } catch (NumberFormatException e) {
                        plugin.utils.message(p, "Invalid input. Please enter a valid number.");
                        guiCreator.apply(p, plugin);
                    }
                });
                return Collections.emptyList();
            }).build();

            gui.open(player);
        } catch (SignGUIVersionException e) {
            plugin.utils.message(player, "An exception has occurred. Please contact with an administrator to solve this issue.");
            plugin.utils.severe(e.getMessage());
        }
    }

    public void increase(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        increase(event, getter, setter, Integer.MAX_VALUE, guiCreator);
    }

    public void decrease(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        decrease(event, getter, setter, 1, guiCreator);
    }

    public void openIntInputSign(Player player, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        openIntInputSign(player, getter, setter, 1, Integer.MAX_VALUE, guiCreator);
    }

    private int extractFirstNumber(SignGUIResult result) throws NumberFormatException {
        String line = result.getLineWithoutColor(0).trim();
        String numeric = line.replaceAll("[^0-9]", "");

        if (!numeric.isEmpty()) {
            int value = Integer.parseInt(numeric);
            if (value > 0) return value;
        }

        throw new NumberFormatException("The first line does not contain a valid positive integer.");
    }
}
