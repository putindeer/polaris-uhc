package us.polarismc.polarisuhc.config.border;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import us.polarismc.polarisuhc.Main;

import java.util.List;

@Getter
@Setter
public class BorderManager {
    private final Main plugin;
    public BorderManager(Main plugin) {
        this.plugin = plugin;
    }

    private int border;
    private int netherBorder;
    private int meetupBorder;
    private int netherMeetupBorder;
    private int borderTimer;
    private boolean tpBorder;
    private double borderSpeed;
    private List<Integer> borderList;
    private List<Integer> netherBorderList;

    private void initializeBorder() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("border");
        assert config != null;
        border = config.getInt("overworld");
        netherBorder = config.getInt("nether");
        meetupBorder = config.getInt("meetup");
        netherMeetupBorder = config.getInt("nethermeetup");
        tpBorder = config.getBoolean("tpborder");
        borderTimer = config.getInt("timer");
        borderSpeed = config.getDouble("speed");
        borderList = config.getIntegerList("borderlist");
        netherBorderList = config.getIntegerList("netherborderlist");
    }
}
