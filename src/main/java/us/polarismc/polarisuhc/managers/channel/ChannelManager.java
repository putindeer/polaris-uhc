package us.polarismc.polarisuhc.managers.channel;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.UHCDeathEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.*;

public class ChannelManager implements Listener {
    private final Main plugin;

    public ChannelManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();
        UHCPlayer uhc = plugin.player.getUHCPlayer(sender);
        if (uhc == null) return;

        ChannelKey sendChannel = uhc.getSendChannel();
        if (!sendChannel.sendable()) return;

        event.setCancelled(true);

        String input = PlainTextComponentSerializer.plainText().serialize(event.message());
        String message = format(sendChannel, sender, input);

        List<Player> receivers = getOnlineReceivers(sendChannel);

        plugin.utils.message(receivers, false, message);
        plugin.utils.message(Bukkit.getConsoleSender(), false, message);
    }

    @EventHandler
    public void onUHCDeath(UHCDeathEvent event) {
        event.getPlayer().resetSendChannel();
    }

    public List<Player> getOnlineReceivers(ChannelKey channel) {
        List<Player> receivers = new ArrayList<>();

        switch (channel.type()) {
            case GLOBAL -> getReceivingPlayers(channel).forEach(uhcPlayer -> receivers.add(uhcPlayer.getPlayer()));
            case STAFF -> getReceivingPlayers(channel).stream().map(UHCPlayer::getPlayer).filter(Objects::nonNull)
                    .filter(player -> player.hasPermission("polaris.chat.staff")).forEach(receivers::add);
            case SPEC -> getReceivingPlayers(channel).stream().filter(UHCPlayer::isPlaying).forEach(uhcPlayer -> receivers.add(uhcPlayer.getPlayer()));
            case TEAM -> getReceivingPlayers(channel).forEach(uhcPlayer -> {
                TeamChannel teamChannel = (TeamChannel) channel;
                UHCTeam team = teamChannel.team();
                if (team == null) return;
                receivers.addAll(team.getOnlineMembers());
            });
        }

        return receivers;
    }

    private String format(ChannelKey channel, Player sender, String message) {
        return switch (channel) {
            case StaffChannel staffChannel -> "<dark_gray>[<red><bold>StaffChat</bold></red>]</dark_gray> <blue>" + sender.getName() + ": <aqua>" + message;
            case SpecChannel specChannel -> "<aqua>[Spectator Chat] " + sender.getName() + ": " + message;
            //TODO - add primary color compatibility
            case TeamChannel teamChannel -> {
                UHCTeam team = teamChannel.team();
                yield plugin.utils.teamPrefix(sender) + team.getColorTag()
                        + sender.getName() + ": <white>" + message;
            }
            default -> message;
        };
    }


    public List<UHCPlayer> getReceivingPlayers(ChannelKey channel) {
        return plugin.player.getOnlinePlayers().stream().filter(uhcPlayer -> uhcPlayer.isReceivingFromChannel(channel)).toList();
    }
}
