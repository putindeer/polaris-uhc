package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import us.polarismc.api.util.builder.WorldBuilder;
import us.polarismc.polarisuhc.Main;

import java.util.List;

@Getter
@Setter
public class UHCManager {
    private final Main plugin;
    private boolean started = false;
    private boolean starting = false;
    private boolean finalized = false;
    private Player host = null;
    private int hostNumber;
    private int number;

    public UHCManager(Main plugin) {
        this.plugin = plugin;
        initializeWorlds();
        initializeToggle();
        initializeDuration();
        initializeBorder();
        initializeRates();
    }

    //region [Worlds]
    private String arenaWorldString;
    private String lobbyWorldString;
    private World arenaWorld;
    private World lobbyWorld;

    private String uhcWorldString;
    private String netherWorldString;
    private String endWorldString;
    private World uhcWorld;
    private World netherWorld;
    private World endWorld;
    private Long seed = null;
    private Long netherSeed = null;
    private Long endSeed = null;
    private boolean amplified = false;

    private void initializeWorlds() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("worlds");
        assert config != null;
        uhcWorldString = config.getString("overworld");
        netherWorldString = config.getString("nether");
        endWorldString = config.getString("end");
        arenaWorldString = config.getString("arena");
        lobbyWorldString = config.getString("lobby");
    }

    public void setGlobalSeed(long l) {
        seed = netherSeed = endSeed = l;
    }

    public void createWorlds(CommandSender sender) {
        if (uhcWorld != null && (!nether || netherWorld != null) && (!end || endWorld != null)) {
            plugin.utils.message(sender, "All worlds have already been created.");
            return;
        }

        if (uhcWorld == null) {
            plugin.utils.message(sender, "Creating <green>" + (amplified ? "amplified " : "") + "overworld <gray>with " + (seed == null ? "random seed." : "seed: <green>" + seed));
            uhcWorld = createWorld(uhcWorldString, seed, World.Environment.NORMAL, amplified);
            plugin.utils.message(sender, "Overworld created succesfully.");
        } else {
            plugin.utils.message(sender, "Overworld has already been created, skipping it...");
        }

        if (nether) {
            if (netherWorld == null) {
                plugin.utils.message(sender, "Creating <red>nether <gray>with " + (seed == null ? "random seed." : netherSeed.equals(seed) ? "the same seed as overworld: <red>" + netherSeed : "seed: <red>" + netherSeed));
                netherWorld = createWorld(netherWorldString, netherSeed, World.Environment.NETHER, false);
                plugin.utils.message(sender, "Nether created succesfully.");
            } else {
                plugin.utils.message(sender, "Nether has already been created, skipping it...");
            }
        }

        if (end) {
            if (endWorld == null) {
                plugin.utils.message(sender, "Creating <purple>end <gray>with " + (seed == null ? "random seed." : endSeed.equals(seed) ? "the same seed as overworld: <purple>" + endSeed : "seed: <purple>" + endSeed));
                endWorld = createWorld(endWorldString, endSeed, World.Environment.THE_END, false);
                plugin.utils.message(sender, "End created succesfully.");
            } else {
                plugin.utils.message(sender, "End has already been created, skipping it...");
            }
        }
    }

    private World createWorld(String name, long seed, World.Environment env, boolean amplified) {
        WorldType type = amplified ? WorldType.AMPLIFIED : WorldType.NORMAL;
        return new WorldBuilder(name, plugin)
                .seed(seed)
                .type(type)
                .environment(env)
                .gamerule(GameRule.NATURAL_REGENERATION, false)
                .gamerule(GameRule.DO_MOB_SPAWNING, false)
                .gamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                .gamerule(GameRule.DO_IMMEDIATE_RESPAWN, true)
                .build();
    }

    //endregion

    //region [Toggle]
    private boolean advancements;
    private boolean antiburn;
    private boolean autols;
    private boolean bookshelves;
    private boolean customcrafts;
    private boolean end;
    private boolean explosives;
    private boolean horses;
    private boolean mobs;
    private boolean nether;
    private boolean notch;
    private boolean pots;
    private boolean starterbooks;
    private boolean stats;
    private boolean strengthnerf;
    private boolean trades;

    private void initializeToggle() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle");
        assert config != null;
        advancements = config.getBoolean("advancements");
        antiburn = config.getBoolean("antiburn");
        autols = config.getBoolean("autols");
        bookshelves = config.getBoolean("bookshelves");
        customcrafts = config.getBoolean("customcrafts");
        end = config.getBoolean("end");
        explosives = config.getBoolean("explosives");
        horses = config.getBoolean("horses");
        mobs = config.getBoolean("mobs");
        nether = config.getBoolean("nether");
        notch = config.getBoolean("notch");
        pots = config.getBoolean("pots");
        starterbooks = config.getBoolean("starterbooks");
        stats = config.getBoolean("stats");
        strengthnerf = config.getBoolean("strengthnerf");
        trades = config.getBoolean("trades");
    }
    //endregion

    //region [Duración]
    private int pvpTime;
    private int meetupTime;
    private int finalhealTime;

    private void initializeDuration() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("rates");
        assert config != null;
        pvpTime = config.getInt("pvp");
        meetupTime = config.getInt("meetup");
        finalhealTime = config.getInt("finalheal");
    }
    //endregion

    //region [Borde]
    private int border;
    private int netherBorder;
    private int meetupBorder;
    private int netherMeetupBorder;
    private int borderTimer;
    private boolean tpBorder;
    private int borderSpeed;
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
        borderSpeed = config.getInt("speed");
        borderList = config.getIntegerList("borderlist");
        netherBorderList = config.getIntegerList("netherborderlist");
    }
    //endregion

    //region [Rates]
    private int xpkillRate;
    private int flintRate;
    private int appleRate;
    private int glassRate;

    private void initializeRates() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("rates");
        assert config != null;
        xpkillRate = config.getInt("xpkill");
        flintRate = config.getInt("flint");
        appleRate = config.getInt("apple");
        glassRate = config.getInt("glass");
    }
    //endregion
}
