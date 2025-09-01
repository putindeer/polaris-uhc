package us.polarismc.polarisuhc.commands.debug;

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.gui.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUI implements TabExecutor {
    private final Main plugin;

    public GUI(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("gui")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            plugin.utils.message(player, "<red>[lang]terms.usage[/lang]: /gui <gui name>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "createworld" -> new BorderWorldGUI(player, plugin);
            case "toggle" -> new ToggleGUI(player, plugin);
            case "customcrafts" -> new CustomCraftsGUI(player, plugin);
            case "potions" -> new PotionsGUI(player, plugin);
            case "duration" -> new DurationRatesGUI(player, plugin);
            case "givehead" -> player.give(plugin.utils.ib(Material.PLAYER_HEAD).owner(player).glint().name("<gold>Golden Head")
                    .edible(ConsumeEffect.applyStatusEffects
                            (List.of(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), new PotionEffect(PotionEffectType.ABSORPTION, 2000, 2)), 1)).build());
            default -> plugin.utils.message(player, "&cUnknown GUI.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> guis = Arrays.asList("createworld", "toggle", "customcrafts", "potions", "givehead", "duration");
            return guis.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
