package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Objects;

public class Debug implements CommandExecutor {
    private final Main plugin;

    public Debug(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("debug")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        plugin.utils.log("=== UHC STATUS ===",
                "Started: " + plugin.uhc.isStarted(),
                "Starting: " + plugin.uhc.isStarting(),
                "Finalized: " + plugin.uhc.isFinalized(),
                "Host: " + (plugin.uhc.getHost() != null ? plugin.uhc.getHost().getName() : "None"),
                "Host Number: " + plugin.uhc.getHostNumber(),
                "UHC Number: " + plugin.uhc.getNumber(),
                "",
                "=== WORLDS ===",
                "UHC World: " + plugin.uhc.getUhcWorldString() + " (Loaded: " + (plugin.uhc.getUhcWorld() != null) + ")",
                "Nether World: " + plugin.uhc.getNetherWorldString() + " (Loaded: " + (plugin.uhc.getNetherWorld() != null) + ")",
                "End World: " + plugin.uhc.getEndWorldString() + " (Loaded: " + (plugin.uhc.getEndWorld() != null) + ")",
                "Arena World: " + plugin.uhc.getArenaWorldString() + " (Loaded: " + (plugin.uhc.getArenaWorld() != null) + ")",
                "Lobby World: " + plugin.uhc.getLobbyWorldString() + " (Loaded: " + (plugin.uhc.getLobbyWorld() != null) + ")",
                "Seed: " + (plugin.uhc.getSeed() != null ? plugin.uhc.getSeed() : "Random"),
                "Nether Seed: " + (plugin.uhc.getNetherSeed() != null ? plugin.uhc.getNetherSeed() : "Random"),
                "End Seed: " + (plugin.uhc.getEndSeed() != null ? plugin.uhc.getEndSeed() : "Random"),
                "Amplified: " + plugin.uhc.isAmplified(),
                "",
                "=== TOGGLE ===",
                "Advancements: " + plugin.uhc.isAdvancements(),
                "Anti Burn: " + plugin.uhc.isAntiBurn(),
                "Auto LS: " + plugin.uhc.isAutoLS(),
                "Bookshelves: " + plugin.uhc.isBookshelves(),
                "End: " + plugin.uhc.isEnd(),
                "Explosives: " + plugin.uhc.isExplosives(),
                "Fire: " + plugin.uhc.isFireAspect(),
                "Flame: " + plugin.uhc.isFlame(),
                "Horses: " + plugin.uhc.isHorses(),
                "Mobs: " + plugin.uhc.isMobs(),
                "Nether: " + plugin.uhc.isNether(),
                "Notch: " + plugin.uhc.isNotch(),
                "Starter Books: " + plugin.uhc.isStarterBooks(),
                "Stats: " + plugin.uhc.isStats(),
                "Strength Nerf: " + plugin.uhc.isNerfedStrength(),
                "Trades: " + plugin.uhc.isTrades(),
                "",
                "=== DURATION ===",
                "PvP Time: " + plugin.uhc.getPvpTime() + " minutes",
                "Meetup Time: " + plugin.uhc.getMeetupTime() + " minutes",
                "Final Heal Time: " + plugin.uhc.getFinalHealTime() + " minutes",
                "",
                "=== BORDER ===",
                "Overworld Border: " + plugin.uhc.getBorder() + " blocks",
                "Nether Border: " + plugin.uhc.getNetherBorder() + " blocks",
                "Meetup Border: " + plugin.uhc.getMeetupBorder() + " blocks",
                "Nether Meetup Border: " + plugin.uhc.getNetherMeetupBorder() + " blocks",
                "Border Timer: " + plugin.uhc.getBorderTimer() + " minutes",
                "TP Border: " + plugin.uhc.isTpBorder(),
                "Border Speed: " + plugin.uhc.getBorderSpeed() + " blocks/second",
                "Border List: " + plugin.uhc.getBorderList().toString(),
                "Nether Border List: " + plugin.uhc.getNetherBorderList().toString(),
                "",
                "=== RATES ===",
                "XP Kill Rate: " + plugin.uhc.getXpKillRate() + "x",
                "Flint Rate: " + plugin.uhc.getFlintRate() + "%",
                "Apple Rate: " + plugin.uhc.getAppleRate() + "%",
                "Glass Rate: " + plugin.uhc.getGlassRate() + "%");
        return true;
    }
}