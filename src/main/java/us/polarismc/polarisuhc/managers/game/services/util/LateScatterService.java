package us.polarismc.polarisuhc.managers.game.services.util;

import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.LateScatterPlayerEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class LateScatterService {
    private final Main plugin;
    public LateScatterService(Main plugin) {
        this.plugin = plugin;
    }

    public void lateScatter(Player player) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        preparePlayer(player);
        lateScatterTeleport(player);

        //TODO - add immunity for 5 seconds
        LateScatterPlayerEvent lsEvent = new LateScatterPlayerEvent(uhcPlayer, plugin);
        Bukkit.getPluginManager().callEvent(lsEvent);
        lsEvent.give();

        uhcPlayer.setAlive();

        plugin.utils.broadcast(SoundEventKeys.BLOCK_NOTE_BLOCK_HARP,
                "<aqua>" + player.getName() + " <gray>has late scattered!");
        //TODO - add stats
    }

    private void preparePlayer(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0f);
        player.getInventory().clear();
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        plugin.player.removeAllPassengers(player);
        plugin.game.advancementService.resetAdvancementsAndKeepRecipes(player);
    }

    private void lateScatterTeleport(Player player) {
        Location target = plugin.game.locationService.getScatterLocationFor(player);
        if (target == null) return;
        player.teleport(target);
    }
}

