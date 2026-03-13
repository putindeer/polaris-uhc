package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Debug implements CommandExecutor {
    private final Main plugin;

    public Debug(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("debug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendDebug();
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "scenario" -> listScenarios(sender);
            case "actionbar" -> actionBar(sender);
            case "start" -> start();
        }
        return true;
    }

    private void listScenarios(CommandSender sender) {
        plugin.utils.message(sender, "<red>Scenarios:");

        plugin.scen.getAll().values().forEach(scenario -> {
            String status = scenario.isEnabled() ? "<green>✓ Enabled</green>" : "<red>✗ Disabled</red>";
            String devTag = scenario.isInDevelopment() ? " <yellow>[DEV]</yellow>" : "";

            plugin.utils.message(sender, "<gray>• <white>" + scenario.getName() + "</white> " + devTag
                    + " (display: " + scenario.getDisplayName() + ") "
                    + status + " <gray>(by " + scenario.getAuthorString() + ") (priority: " + scenario.getPriority() + ")");

            // Mostrar descripción
            plugin.utils.message(sender, scenario.getDescription());

            // Mostrar incompatibilidades si existen
            if (scenario.getIncompatibleScenarios().length > 0) {
                String incompatible = Arrays.stream(scenario.getIncompatibleScenarios())
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));
                plugin.utils.message(sender, "  <red>⚠ Incompatible with: " + incompatible);
            }
        });
    }

    private void actionBar(CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        plugin.info.bar.setDefault(player, p -> plugin.timer::getFormatted);
        plugin.utils.delay(40, () -> plugin.info.bar.sendTemporary(player, "Este es un mensaje temporal de 3 segundos", 3));
        plugin.utils.delay(200, () -> plugin.info.bar.sendTemporary(player, "Este es un mensaje temporal de 2 segundos con variable" + player.getName(), 2));
        plugin.utils.delay(2000, () -> plugin.info.bar.clear(player));
    }

    private void start() {
        plugin.timer.start();
    }

    private void sendDebug() {
        plugin.utils.log("=== UHC STATUS ===",
                "Started: " + plugin.uhc.hasStarted(),
                "Starting: " + plugin.uhc.isStarting(),
                "Finalized: " + plugin.uhc.isFinalized(),
                "Host: " + (plugin.uhc.getHost() != null ? plugin.uhc.getHost() : "None"),
                "Host Number: " + plugin.uhc.getHostNumber(),
                "UHC Number: " + plugin.uhc.getNumber(),
                "",
                "=== WORLDS ===",
                "UHC World: " + plugin.uhc.world.getUhcWorldString() + " (Loaded: " + (plugin.uhc.world.getUhcWorld() != null) + ")",
                "Nether World: " + plugin.uhc.world.getNetherWorldString() + " (Loaded: " + (plugin.uhc.world.getNetherWorld() != null) + ")",
                "End World: " + plugin.uhc.world.getEndWorldString() + " (Loaded: " + (plugin.uhc.world.getEndWorld() != null) + ")",
                "Arena World: " + plugin.uhc.world.getArenaWorldString() + " (Loaded: " + (plugin.uhc.world.getArenaWorld() != null) + ")",
                "Lobby World: " + plugin.uhc.world.getLobbyWorldString() + " (Loaded: " + (plugin.uhc.world.getLobbyWorld() != null) + ")",
                "Seed: " + (plugin.uhc.world.getSeed() != null ? plugin.uhc.world.getSeed() : "Random"),
                "Nether Seed: " + (plugin.uhc.world.getNetherSeed() != null ? plugin.uhc.world.getNetherSeed() : "Random"),
                "End Seed: " + (plugin.uhc.world.getEndSeed() != null ? plugin.uhc.world.getEndSeed() : "Random"),
                "Amplified: " + plugin.uhc.world.isAmplified(),
                "",
                "=== TOGGLE ===",
                "Advancements: " + plugin.uhc.toggle.isAdvancements(),
                "Anti Item Destruction: " + plugin.uhc.toggle.isAntiItemDestruction(),
                "Auto LS: " + plugin.uhc.toggle.isAutoLS(),
                "Bookshelves: " + plugin.uhc.toggle.isBookshelves(),
                "End: " + plugin.uhc.toggle.isEnd(),
                "Explosives: " + plugin.uhc.toggle.isExplosives(),
                "Fire: " + plugin.uhc.toggle.isFireAspect(),
                "Flame: " + plugin.uhc.toggle.isFlame(),
                "Horses: " + plugin.uhc.toggle.isHorses(),
                "Mobs: " + plugin.uhc.toggle.isMobs(),
                "Nether: " + plugin.uhc.toggle.isNether(),
                "Notch: " + plugin.uhc.toggle.isNotch(),
                "Starter Books: " + plugin.uhc.toggle.isStarterBooks(),
                "Stats: " + plugin.uhc.toggle.isStats(),
                "Strength Nerf: " + plugin.uhc.toggle.isNerfedStrength(),
                "Trades: " + plugin.uhc.toggle.isTrades(),
                "",
                "=== DURATION ===",
                "PvP Time: " + plugin.uhc.duration.getPvpTime() + " minutes",
                "Meetup Time: " + plugin.uhc.duration.getMeetupTime() + " minutes",
                "Final Heal Time: " + plugin.uhc.duration.getFinalHealTime() + " minutes",
                "",
                "=== BORDER ===",
                "Overworld Border: " + plugin.uhc.border.getBorder() + " blocks",
                "Nether Border: " + plugin.uhc.border.getNetherBorder() + " blocks",
                "Meetup Border: " + plugin.uhc.border.getMeetupBorder() + " blocks",
                "Nether Meetup Border: " + plugin.uhc.border.getNetherMeetupBorder() + " blocks",
                "Border Timer: " + plugin.uhc.border.getBorderTimer() + " minutes",
                "TP Border: " + plugin.uhc.border.isTpBorder(),
                "Border Speed: " + plugin.uhc.border.getBorderSpeed() + " blocks/second",
                "Border List: " + plugin.uhc.border.getBorderList().toString(),
                "Nether Border List: " + plugin.uhc.border.getNetherBorderList().toString(),
                "",
                "=== RATES ===",
                "XP Kill Rate: " + plugin.uhc.rates.getXpKillRate() + "x",
                "Flint Rate: " + plugin.uhc.rates.getFlintRate() + "%",
                "Apple Rate: " + plugin.uhc.rates.getAppleRate() + "%",
                "Glass Rate: " + plugin.uhc.rates.getGlassRate() + "%");
    }
}