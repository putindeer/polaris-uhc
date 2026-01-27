package us.polarismc.polarisuhc;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.polarismc.polarisuhc.managers.channel.ChannelManager;
import us.polarismc.polarisuhc.managers.info.InfoManager;
import us.polarismc.polarisuhc.managers.player.PlayerManager;
import us.polarismc.polarisuhc.managers.team.TeamManager;
import us.polarismc.polarisuhc.managers.uhc.GameTimer;
import us.polarismc.polarisuhc.managers.scenario.ScenarioManager;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;
import us.polarismc.polarisuhc.util.StartThings;
import us.polarismc.polarisuhc.util.Utils;

public class Main extends JavaPlugin {
    @Getter
    public static Main instance;
    public Utils utils;
    public StartThings start;
    public UHCManager uhc;
    public ScenarioManager scen;
    public PlayerManager player;
    public TeamManager team;
    public GameTimer timer;
    public ChannelManager channel;
    public InfoManager info;
    @Override
    public void onEnable() {
        instance = this;
        utils = new Utils(this, "<blue><bold>UHC</bold></blue> <dark_gray>»</dark_gray> <reset>");
        start = new StartThings(this);
    }

    @Override
    public void onDisable() {
        start.disable();
    }
}
