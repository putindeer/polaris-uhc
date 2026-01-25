package us.polarismc.polarisuhc.managers.info.actionbar;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ActionBarManager {
    private final Main plugin;
    private final Map<UUID, ActionBarData> playerData;

    public ActionBarManager(Main plugin) {
        this.plugin = plugin;
        this.playerData = new ConcurrentHashMap<>();
        startUpdateTask();
    }

    public void setDefault(Player player, Function<Player, String> message) {
        ActionBarData data = getOrCreateData(player);
        data.defaultMessage = message;
    }

    public void setDefault(Player player, String message) {
        setDefault(player, p -> message);
    }

    public void setGlobalDefault(Function<Player, String> message) {
        Bukkit.getOnlinePlayers().forEach(player -> setDefault(player, message));
    }

    public void setGlobalDefault(String message) {
        setGlobalDefault(p -> message);
    }

    public void sendTemporary(Player player, Function<Player, String> message, float seconds) {
        ActionBarData data = getOrCreateData(player);
        data.temporaryMessage = message;

        long ticks = (long) seconds * 20;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ActionBarData currentData = playerData.get(player.getUniqueId());
            if (currentData != null && message.equals(currentData.temporaryMessage)) {
                currentData.temporaryMessage = null;
            }
        }, ticks);
    }

    public void sendTemporary(Player player, String message, float seconds) {
        sendTemporary(player, p -> message, seconds);
    }

    public void sendGlobalTemporary(Function<Player, String> message, float seconds) {
        Bukkit.getOnlinePlayers().forEach(player -> sendTemporary(player, message, seconds));
    }

    public void sendGlobalTemporary(String message, float seconds) {
        sendGlobalTemporary(p -> message, seconds);
    }

    public void clear(Player player) {
        ActionBarData data = playerData.get(player.getUniqueId());
        if (data != null) {
            data.defaultMessage = null;
            data.temporaryMessage = null;
        }
    }

    public void clearAll() {
        Bukkit.getOnlinePlayers().forEach(this::clear);
    }

    private void startUpdateTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Map.Entry<UUID, ActionBarData> entry : playerData.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player == null || !player.isOnline()) {
                    continue;
                }

                ActionBarData data = entry.getValue();
                String messageToShow = null;

                if (data.temporaryMessage != null) {
                    messageToShow = data.temporaryMessage.apply(player);
                } else if (data.defaultMessage != null) {
                    messageToShow = data.defaultMessage.apply(player);
                }

                if (messageToShow != null) {
                    String finalMessage = messageToShow;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (player.isOnline()) {
                            Component component = plugin.utils.chat(finalMessage);
                            player.sendActionBar(component);
                        }
                    });
                }
            }
        }, 0L, 2L);
    }

    private static class ActionBarData {
        Function<Player, String> defaultMessage;
        Function<Player, String> temporaryMessage;
    }

    private ActionBarData getOrCreateData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), k -> new ActionBarData());
    }
}