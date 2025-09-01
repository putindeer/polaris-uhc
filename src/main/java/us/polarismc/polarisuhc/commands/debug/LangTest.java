package us.polarismc.polarisuhc.commands.debug;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;

import java.util.Objects;

/**
 * Comando para probar y asignar traducciones usando LangManager.
 */
public class LangTest implements CommandExecutor {

    private final Main plugin;

    public LangTest(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("langtest")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            plugin.utils.message(sender,
                    "&e[LangTest]",
                    "[lang]welcome.message[/lang]",
                     "[lang]error.message[/lang]",
                     "[lang]command.language.changed[/lang]",
                     "[lang]non.existing.key[/lang]",
                    plugin.utils.lang.translate(sender, "[lang]welcome.message[/lang]"),
                    plugin.utils.lang.translate(sender, "[lang]error.message[/lang]"),
                    plugin.utils.lang.translate(sender, "[lang]command.language.changed[/lang]").formatted("es_ES"),
                    plugin.utils.lang.translate(sender, "[lang]non.existing.key[/lang]"));
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            String code = args[2];

            if (target == null) {
                plugin.utils.message(sender, "&cJugador no encontrado: " + args[1]);
                return true;
            }

            if (!plugin.utils.lang.setPlayerLanguage(target, code)) {
                plugin.utils.message(sender, "&cCódigo de idioma inválido: " + code);
                return true;
            }

            plugin.utils.message(sender, "&aIdioma de &e" + target.getName() + "&a actualizado a &e" + code);
            plugin.utils.message(target, plugin.utils.lang.translate(target, "[lang]command.language.changed[/lang]").formatted(code));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.utils.lang.reload();
            plugin.utils.message(sender, "plugin recargado");
        }

        plugin.utils.message(sender, "&cUso: /langtest [set <jugador> <codigoIdioma>]");
        return true;
    }
}
