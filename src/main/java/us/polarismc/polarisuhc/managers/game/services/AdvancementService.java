package us.polarismc.polarisuhc.managers.game.services;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.Keyed;
import org.bukkit.scheduler.BukkitTask;
import us.polarismc.polarisuhc.Main;

import java.util.*;

public class AdvancementService {
    private final Main plugin;

    private List<Advancement> cachedAdvancements;
    private List<NamespacedKey> cachedRecipeKeys;

    private BukkitTask resetTask;
    private final Queue<UUID> resetQueue = new ArrayDeque<>();
    private final Set<UUID> resetDone = new HashSet<>();
    private final Set<UUID> resetQueued = new HashSet<>();

    public AdvancementService(Main plugin) {
        this.plugin = plugin;
    }

    public List<Advancement> getAllAdvancementsNoRecipes() {
        if (cachedAdvancements == null) {
            cachedAdvancements = new ArrayList<>();
            Bukkit.getServer().advancementIterator().forEachRemaining(adv -> {
                if (!adv.getKey().getKey().startsWith("recipes/")) {
                    cachedAdvancements.add(adv);
                }
            });
        }
        return cachedAdvancements;
    }

    public List<NamespacedKey> getAllRecipeKeys() {
        if (cachedRecipeKeys == null) {
            cachedRecipeKeys = new ArrayList<>();
            Bukkit.recipeIterator().forEachRemaining(recipe -> {
                if (recipe instanceof Keyed keyed) {
                    cachedRecipeKeys.add(keyed.getKey());
                }
            });
        }
        return cachedRecipeKeys;
    }

    public void resetAdvancementsAndKeepRecipes(Player player) {
        for (Advancement advancement : getAllAdvancementsNoRecipes()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancement);

            Collection<String> awarded = progress.getAwardedCriteria();
            if (awarded.isEmpty()) continue;

            for (String criteria : new ArrayList<>(awarded)) {
                progress.revokeCriteria(criteria);
            }
        }

        player.discoverRecipes(getAllRecipeKeys());
    }

    public void startResetTask(long periodTicks) {
        stopResetTask();

        resetTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            UUID uuid;
            Player player = null;

            while ((uuid = resetQueue.poll()) != null) {
                resetQueued.remove(uuid);

                if (resetDone.contains(uuid)) continue;

                player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) break;

                player = null;
            }

            if (player == null) {
                if (resetQueue.isEmpty()) stopResetTask();
                return;
            }

            resetAdvancementsAndKeepRecipes(player);
            resetDone.add(player.getUniqueId());

            if (resetQueue.isEmpty()) stopResetTask();
        }, 0L, periodTicks);
    }

    public void enqueueReset(Player player) {
        UUID uuid = player.getUniqueId();
        if (resetDone.contains(uuid)) return;
        if (!resetQueued.add(uuid)) return;
        resetQueue.add(uuid);
    }

    public void stopResetTask() {
        if (resetTask != null) {
            resetTask.cancel();
            resetTask = null;
        }
        resetDone.clear();
        resetQueued.clear();
        resetQueue.clear();
    }
}