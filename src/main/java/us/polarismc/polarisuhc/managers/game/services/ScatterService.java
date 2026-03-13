package us.polarismc.polarisuhc.managers.game.services;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ScatterService implements Listener {
    private final Main plugin;
    public ScatterService(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void handleJoinDuringScatter(Player player) {
        if (!plugin.uhc.isScattering()) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;

        UUID uuid = player.getUniqueId();

        if (processed.add(uuid)) {
            queue.addLast(uuid);
            totalDynamic.incrementAndGet();
            plugin.utils.message(player, "<gray>You will be scattered soon...");
        }
        plugin.game.advancementService.enqueueReset(player);
    }

    private final Deque<UUID> queue = new ArrayDeque<>();
    private final Set<UUID> processed = new HashSet<>();
    private final AtomicInteger pending = new AtomicInteger(0);
    private final AtomicInteger started = new AtomicInteger(0);
    private final AtomicInteger totalDynamic = new AtomicInteger(0);
    private final AtomicBoolean waitingPrinted = new AtomicBoolean(false);

    public void scatter(Player host) {
        plugin.uhc.setState(UHCState.SCATTERING);
        clearData();

        Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() == GameMode.SURVIVAL).map(Player::getUniqueId).forEach(uuid -> {
            queue.addLast(uuid);
            processed.add(uuid);
            totalDynamic.incrementAndGet();
        });

        String line = "<dark_gray><strikethrough>--------------------------</strikethrough></dark_gray>";
        String plural = totalDynamic.get() == 1 ? "" : "s";
        plugin.utils.broadcast(line, "<gray>Starting the scatter of <blue>" + totalDynamic.get() + "</blue> player" + plural + ".", line);

        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (queue.isEmpty()) {
                if (!waitingPrinted.getAndSet(true)) {
                    plugin.utils.broadcast("<gray>Waiting for players to load chunks...");
                }
                if (pending.get() == 0) {
                    task.cancel();
                    plugin.uhc.setState(UHCState.SCATTERED);
                    plugin.utils.broadcast(line, "<gray>Scatter concluded.", line);

                    plugin.game.finalizeStep(host);
                }
                return;
            }

            UUID uuid = queue.pollFirst();

            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline() || player.getGameMode() != GameMode.SURVIVAL) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                String name = offlinePlayer.getName();

                int remaining = totalDynamic.decrementAndGet();
                plugin.utils.broadcast("<dark_gray>[<red><strikethrough>X</strikethrough></red>/<gray>" + remaining + "</gray>] "
                        + "<red><strikethrough>" + name + "</strikethrough></red> <gray>(offline)</gray>");
                return;
            }

            Location targetLocation = plugin.game.locationService.getScatterLocationFor(player);
            if (targetLocation == null) {
                int remaining = totalDynamic.decrementAndGet();
                plugin.utils.broadcast("<dark_gray>[<red>X</red>/<gray>" + remaining + "</gray>] <red>" + player.getName() + "</red> <gray>(no location)</gray>");
                return;
            }

            pending.incrementAndGet();
            int now = started.incrementAndGet();

            plugin.utils.broadcast("<dark_gray>[<gray>" + now + "</gray>/<gray>" + totalDynamic.get() + "</gray>] <aqua>" + player.getName());

            player.teleportAsync(targetLocation).whenComplete((ok, ex) -> {
                pending.decrementAndGet();

                if (ex == null && Boolean.TRUE.equals(ok)) {
                    Bukkit.getScheduler().runTask(plugin, () -> plugin.player.getUHCPlayer(player).setAlive());
                }

                // The code should never reach this part but it's here in case something goes wrong.

                else if (ex != null) {
                    Bukkit.getScheduler().runTask(plugin, () ->
                            plugin.utils.broadcast("<dark_gray>[<red><strikethrough>" + started.decrementAndGet() + "</strikethrough></red>/<gray>" + totalDynamic.decrementAndGet() + "</gray>] "
                                    + "<red><strikethrough>" + player.getName() + "</strikethrough></red> <gray>(invalid scatter)</gray>"));
                    plugin.utils.warning("Teleport failed for " + player.getName() + ": " + ex.getMessage());
                } else {
                    Bukkit.getScheduler().runTask(plugin, () ->
                            plugin.utils.broadcast("<dark_gray>[<red><strikethrough>" + started.decrementAndGet() + "</strikethrough></red>/<gray>" + totalDynamic.decrementAndGet() + "</gray>] "
                                    + "<red><strikethrough>" + player.getName() + "</strikethrough></red> <gray>(invalid scatter)</gray>"));
                    plugin.utils.warning("Teleport failed for " + player.getName() + ": returned false");
                }
            });
        }, 0L, 5L);
    }

    private void clearData() {
        queue.clear();
        processed.clear();
        pending.set(0);
        started.set(0);
        totalDynamic.set(0);
        waitingPrinted.set(false);
    }
}
