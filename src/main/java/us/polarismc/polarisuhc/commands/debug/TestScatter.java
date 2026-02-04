package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.Objects;

public class TestScatter implements CommandExecutor {
    private final Main plugin;

    public TestScatter(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("testscatter")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.utils.message(sender, "<red>Only players can use this command.");
            return true;
        }
        plugin.uhc.setState(UHCState.IDLE);
        return true;
    }
}


