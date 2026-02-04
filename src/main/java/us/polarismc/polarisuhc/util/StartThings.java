package us.polarismc.polarisuhc.util;

import fr.mrmicky.fastinv.FastInvManager;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.commands.debug.*;
import us.polarismc.polarisuhc.commands.host.legacy.CreateWorld;
import us.polarismc.polarisuhc.commands.host.legacy.ToggleScenario;
import us.polarismc.polarisuhc.managers.arena.ArenaManager;
import us.polarismc.polarisuhc.managers.channel.ChannelManager;
import us.polarismc.polarisuhc.managers.hub.HubManager;
import us.polarismc.polarisuhc.managers.info.InfoManager;
import us.polarismc.polarisuhc.managers.player.PlayerManager;
import us.polarismc.polarisuhc.managers.game.GameFlowManager;
import us.polarismc.polarisuhc.managers.team.TeamManager;
import us.polarismc.polarisuhc.managers.team.commands.*;
import us.polarismc.polarisuhc.managers.game.GameTimer;
import us.polarismc.polarisuhc.managers.scenario.ScenarioManager;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

public class StartThings {
    private final Main plugin;
    public StartThings(Main plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        enable();
    }

    public void enable() {
        registerCommands();
        registerListeners();
        registerManagers();
        registerLanguage();
    }

    public void disable() {
        plugin.team.disableAndDeleteTeams();
        plugin.uhc.crafts.disableAll();
        plugin.player.removeAllDisplays();
    }

    public void registerCommands() {
        // Dev commands (uhc.dev)
        new Debug(plugin);
        new GUI(plugin);
        new Hex(plugin);
        new TeamHex(plugin);
        new TestScatter(plugin);
        new TestTeams(plugin);
        // Host commands (uhc.host)
        new CreateWorld(plugin);
        new ToggleScenario(plugin);
        // Team commands (uhc.managers.team)
        new BookStuff(plugin);
        new ColorTeam(plugin);
        new EmojiTeam(plugin);
        new MakeTeams(plugin);
        new MinedOres(plugin);
        new Ores(plugin);
        new Solo(plugin);
        new TeamChat(plugin);
        new TeamCommand(plugin);
        new TeamLocation(plugin);
    }

    public void registerListeners() {

    }

    public void registerManagers() {
        FastInvManager.register(plugin);
        plugin.uhc = new UHCManager(plugin);
        plugin.scen = new ScenarioManager(plugin);
        plugin.player = new PlayerManager(plugin);
        plugin.team = new TeamManager(plugin);
        plugin.timer = new GameTimer(plugin);
        plugin.channel = new ChannelManager(plugin);
        plugin.info = new InfoManager(plugin);
        plugin.game = new GameFlowManager(plugin);
        plugin.arena = new ArenaManager(plugin);
        plugin.hub = new HubManager(plugin);
    }

    public void registerLanguage() {
        //TODO - rehacer esto pls
    }
}
