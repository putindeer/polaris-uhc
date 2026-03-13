package us.polarismc.polarisuhc.managers.game.services;

import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import us.polarismc.polarisuhc.Main;

import java.util.List;

import net.kyori.adventure.sound.Sound;
import us.polarismc.polarisuhc.config.toggle.ToggleSetting;
import us.polarismc.polarisuhc.events.UHCPlayerStartEvent;
import us.polarismc.polarisuhc.events.UHCStartEvent;
import us.polarismc.polarisuhc.managers.game.timer.EventAnchor;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

public class StartService {
    private final Main plugin;

    private BukkitTask countdownTask;
    private BukkitTask pvpCountdownTask;

    public StartService(Main plugin) {
        this.plugin = plugin;
    }

    public void start(Player host) {
        countdown(host);
    }

    private void handleStart(Player host) {
        plugin.uhc.setState(UHCState.STARTED);
        plugin.utils.broadcast("<green>The UHC has started!");

        plugin.info.handleInfoStart();

        prepareWorlds(host);

        List<UHCPlayer> uhcPlayers = plugin.player.getPlayingOnlinePlayers();
        List<Player> players = plugin.player.getPlayingOnlinePlayersAsPlayers();
        players.forEach(player -> {
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            plugin.game.resetPrestartAttributes(player);
            player.setInvulnerable(false);
            //TODO - add immunity for 5 seconds
        });

        plugin.utils.broadcastTitle("<gold>The UHC has started", "<green>GL!",
                Sound.sound(SoundEventKeys.BLOCK_ANCIENT_DEBRIS_BREAK, Sound.Source.MASTER, 10, 1),
                plugin.utils.timesFromTicks(10, 40, 10));
        UHCStartEvent startEvent = new UHCStartEvent(plugin.player.getPlayingPlayers(), uhcPlayers);
        //TODO - add autols in hubmanager
        //TODO - add stats
        Bukkit.getPluginManager().callEvent(startEvent);
        uhcPlayers.forEach(player -> {
            UHCPlayerStartEvent playerStartEvent = new UHCPlayerStartEvent(player, plugin);
            Bukkit.getPluginManager().callEvent(playerStartEvent);
            playerStartEvent.give();
        });

        plugin.timer.addEvent(EventAnchor.PRE_PVP, 60,
                () -> plugin.utils.broadcast("Send /helpop if someone is stalking you. | Manda /helpop si te están stalkeando."));

        //TODO - add starter books implementation

        cancelCountdown();

        plugin.game.finalizeStep(host);
    }

    private void countdown(Player host) {
        cancelCountdown();

        countdownTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int time = 6;

            @Override
            public void run() {
                time--;

                if (time >= 1 && time <= 5) {
                    String color = switch (time) {
                        case 5 -> "<dark_red>";
                        case 4 -> "<red>";
                        case 3 -> "<yellow>";
                        case 2 -> "<dark_green>";
                        default -> "<green>";
                    };

                    plugin.utils.broadcast(SoundEventKeys.BLOCK_NOTE_BLOCK_HARP,
                            color + time + "<dark_gray> seconds until the UHC starts...");
                    plugin.utils.broadcastTitle(color + time);
                    return;
                }

                if (time > 0) return;

                handleStart(host);
            }
        }, 0L, 20L);
    }

    private void prepareWorlds(Player host) {
        plugin.uhc.world.getPlayingWorlds().forEach(world -> {
            world.setGameRule(GameRules.PVP, false);
            world.setGameRule(GameRules.SHOW_ADVANCEMENT_MESSAGES, plugin.uhc.toggle.isAdvancements());
            world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, false);
            world.setGameRule(GameRules.SPAWN_PHANTOMS, false);
            world.setGameRule(GameRules.SPAWN_MOBS, plugin.uhc.toggle.isMobs());
            world.setHardcore(true);
            world.setDifficulty(Difficulty.HARD);
            world.setTime(1000);
        });

        World world = plugin.uhc.world.getUhcWorld();
        world.getWorldBorder().setSize(plugin.uhc.border.getBorder());

        if (plugin.uhc.toggle.isNether()) {
            World netherWorld = plugin.uhc.world.getNetherWorld();
            if (netherWorld == null) {
                plugin.uhc.toggle.toggleSetting(ToggleSetting.NETHER);
                plugin.utils.message(host, "Nether <red>disabled</red> because the nether world was <red>not created</red>.");
            } else {
                netherWorld.getWorldBorder().setSize(plugin.uhc.border.getNetherBorder());
            }
        }
    }

    public void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
    }
}
