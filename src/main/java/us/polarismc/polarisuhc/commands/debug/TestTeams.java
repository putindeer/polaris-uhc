package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestTeams implements CommandExecutor {
    private final Main plugin;

    public TestTeams(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("testteams")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) {
            plugin.utils.message(sender, "<red>Uso: /testteams <cantidad>");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            plugin.utils.message(sender, "<red>Debes especificar un número válido");
            return true;
        }

        if (amount < 1 || amount > 2000) {
            plugin.utils.message(sender, "<red>Especifica un número entre 1 y 2000");
            return true;
        }

        List<String> messages = generateMessages(amount, player);

        plugin.utils.message(sender, messages.toArray(new String[0]));

        return true;
    }

    private List<String> generateMessages(int amount, Player player) {
        List<String> messages = new ArrayList<>();
        messages.add("<green>Generando " + amount + " equipos de prueba...");
        messages.add("");

        for (int i = 0; i < amount; i++) {
            UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
            UHCTeam team = new UHCTeam(uhcPlayer);
            String displayText = team.getPrefix() + player.getName();
            messages.add(displayText);
        }

        messages.add("");
        messages.add("<green>✓ Se generaron " + amount + " equipos exitosamente");
        return messages;
    }
}