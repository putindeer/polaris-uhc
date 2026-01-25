package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.events.UHCDeathEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Scenario(name = "TeamInventory", author = "putindeer", icon = Material.ENDER_CHEST, command = "teaminventory",
        description = "Each team has a shared inventory. Use /ti to open it.")
public class TeamInventory extends BaseScenario implements TabExecutor {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(UHCDeathEvent uhcEvent) {
        PlayerDeathEvent event = uhcEvent.getDeathEvent();

        UHCPlayer uhcPlayer = uhcEvent.getPlayer();

        UHCTeam team = uhcPlayer.getTeam();
        if (team == null) return;

        if (team.getPlayingMemberCount() - 1 != 0) return;

        Inventory teamInventory = team.getTeamInventory();
        if (teamInventory == null) return;

        for (ItemStack item : teamInventory.getContents()) {
            if (item == null || item.getType().isAir()) continue;
            event.getDrops().add(item);
        }

        team.setTeamInventory(null);
    }

    private void openTeamInventory(Player viewer, Player owner) {
        UHCPlayer ownerUhcPlayer = plugin.player.getUHCPlayer(owner);

        if (viewer.getUniqueId().equals(owner.getUniqueId()) && !ownerUhcPlayer.isPlaying()) {
            plugin.utils.message(viewer, "<red>You must be playing to use TeamInventory.");
            return;
        }

        UHCTeam team = ownerUhcPlayer.getTeam();
        if (team == null) {
            plugin.utils.message(viewer, "<red>That player is not in a team.");
            return;
        }

        if (!team.hasAlivePlayers()) {
            plugin.utils.message(viewer, "<red>This team doesn't have any players left, so they don't have an inventory.");
            return;
        }

        Inventory teamInventory = team.getTeamInventory();
        if (teamInventory == null) {
            teamInventory = Bukkit.createInventory(null, 4 * 9, plugin.utils.chat(team.getColorTag() + "Team Inventory - " + team.getEmoji()));
            team.setTeamInventory(teamInventory);
        }

        viewer.openInventory(teamInventory);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player viewer)) {
            plugin.utils.message(sender, "<red>Only players can use this command.");
            return true;
        }

        if (!plugin.uhc.isStarted()) {
            plugin.utils.message(viewer, "<red>The UHC hasn't started yet.");
            return true;
        }

        UHCPlayer viewerPlayer = plugin.player.getUHCPlayer(viewer);

        if (args.length == 0 || !plugin.utils.hasHostPermission(viewer) || viewerPlayer.isPlaying()) {
            openTeamInventory(viewer, viewer);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.utils.message(viewer, "<red>That player isn't online.");
            return true;
        }

        openTeamInventory(viewer, target);
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        if (!(sender instanceof Player player)) return list;
        if (!plugin.utils.hasHostPermission(player)) return list;

        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }

        list.removeIf(s -> s == null || !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(list);
        return list;
    }
}