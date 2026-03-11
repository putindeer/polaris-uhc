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
        initializeBorder();
    }

    public int getCurrentBorder() {
        if (plugin.uhc.hasMeetupStarted()) {
            if (tpBorder && !borderList.isEmpty()) {
                return borderList.get(currentBorderIndex);
            }
            return meetupBorder - netherScatterInset;
        }
        return border;
    }

    public int getCurrentNetherBorder() {
        if (plugin.uhc.hasMeetupStarted()) {
            if (netherTPBorder && !netherBorderList.isEmpty()) {
                return netherBorderList.get(currentBorderIndex);
            }
            return netherMeetupBorder - netherScatterInset;
        }
        return netherBorder;
    }

    private int currentBorderIndex = 0;
    public void advanceBorderIndex() {
        if (currentBorderIndex < borderList.size() - 1) currentBorderIndex++;
    }

    private int border;
    private int meetupBorder;
    private int borderTimer;
    private boolean tpBorder;
    private double borderSpeed;
    private List<Integer> borderList;

    private int netherBorder;
    private int netherMeetupBorder;
    private int netherBorderTimer;
    private boolean netherTPBorder;
    private double netherBorderSpeed;
    private List<Integer> netherBorderList;
    private int netherScatterInset;

    private void initializeBorder() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("border");
        assert config != null;
        border = config.getInt("overworld");
        meetupBorder = config.getInt("meetup");
        borderTimer = config.getInt("timer");
        tpBorder = config.getBoolean("tp-border");
        borderSpeed = config.getDouble("speed");
        borderList = config.getIntegerList("border-list");

        netherBorder = config.getInt("nether");
        netherMeetupBorder = config.getInt("nether-meetup");
        netherBorderTimer = config.getInt("nether-timer");
        netherTPBorder = config.getBoolean("nether-tp-border");
        netherBorderSpeed = config.getDouble("nether-speed");
        netherBorderList = config.getIntegerList("nether-border-list");
        netherScatterInset = config.getInt("nether-scatter-inset");
    }
}
