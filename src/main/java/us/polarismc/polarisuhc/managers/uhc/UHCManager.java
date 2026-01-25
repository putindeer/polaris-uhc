package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.border.BorderManager;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraftManager;
import us.polarismc.polarisuhc.config.duration.DurationManager;
import us.polarismc.polarisuhc.managers.info.GUIManager;
import us.polarismc.polarisuhc.config.potion.PotionManager;
import us.polarismc.polarisuhc.config.rates.RatesManager;
import us.polarismc.polarisuhc.config.toggle.ToggleManager;
import us.polarismc.polarisuhc.config.world.WorldManager;

import java.util.List;

@Getter
@Setter
public class UHCManager {
    private final Main plugin;
    public final BorderManager border;
    public final CustomCraftManager crafts;
    public final DurationManager duration;
    public final PotionManager potion;
    public final ToggleManager toggle;
    public final RatesManager rates;
    public final WorldManager world;
    public final GUIManager ui;
    private boolean started = false;
    private boolean starting = false;
    private boolean finalized = false;
    private Player host = null;
    private int hostNumber;
    private int number;
    private List<OfflinePlayer> alivePlayers;
    private List<OfflinePlayer> deadPlayers;

    public UHCManager(Main plugin) {
        this.plugin = plugin;
        this.border = new BorderManager(plugin);
        this.crafts = new CustomCraftManager(plugin);
        this.duration = new DurationManager(plugin);
        this.potion = new PotionManager(plugin);
        this.toggle = new ToggleManager(plugin);
        this.rates = new RatesManager(plugin);
        this.world = new WorldManager(plugin);
        this.ui = new GUIManager(plugin);
    }

    public List<Player> getPlayingPlayers() {
        return alivePlayers.stream().filter(OfflinePlayer::isOnline).map(OfflinePlayer::getPlayer).toList();
    }
}