package us.polarismc.polarisuhc.config.world;

import lombok.Getter;
import lombok.Setter;
import me.putindeer.api.util.builder.WorldBuilder;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import us.polarismc.polarisuhc.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorldManager {
    private final Main plugin;
    public WorldManager(Main plugin) {
        this.plugin = plugin;
        initializeWorlds();
    }

    public List<World> getPlayingWorlds() {
        List<World> worlds = new ArrayList<>();

        if (uhcWorld != null) worlds.add(uhcWorld);

        if (plugin.uhc.toggle.isNether() && netherWorld != null) worlds.add(netherWorld);

        if (plugin.uhc.toggle.isEnd() && endWorld != null) worlds.add(endWorld);

        return worlds;
    }

    public <T> void applyGameruleToPlayingWorlds(GameRule<T> rule, T value) {
        getPlayingWorlds().forEach(world -> world.setGameRule(rule, value));
    }

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

        uhcWorld = getWorldIfExists(uhcWorldString);
        netherWorld = getWorldIfExists(netherWorldString);
        endWorld = getWorldIfExists(endWorldString);
        arenaWorld = getWorldIfExists(arenaWorldString);
        lobbyWorld = getWorldIfExists(lobbyWorldString);
    }

    private World getWorldIfExists(String name) {
        if (name == null || name.isBlank()) return null;

        World world = Bukkit.getWorld(name);
        if (world != null) return world;

        File worldFolder = new File(Bukkit.getWorldContainer(), name);
        if (worldFolder.exists() && worldFolder.isDirectory()) {
            return Bukkit.createWorld(new WorldCreator(name));
        }

        return null;
    }

    public void setGlobalSeed(long l) {
        seed = netherSeed = endSeed = l;
    }

    public void createWorlds(CommandSender sender) {
        if (areWorldsCreated()) {
            plugin.utils.message(sender, "All worlds have already been created.");
            return;
        }

        if (uhcWorld == null) {
            if (amplified) {
                if (seed == null) {
                    plugin.utils.message(sender, "Creating <green>amplified overworld</green>");
                } else {
                    plugin.utils.message(sender, "Creating <green>amplified overworld</green> with seed: <green>" + seed);
                }
            } else {
                if (seed == null) {
                    plugin.utils.message(sender, "Creating <green>overworld</green> with random seed.");
                } else {
                    plugin.utils.message(sender, "Creating <green>overworld</green> with seed: <green>" + seed);
                }
            }

            uhcWorld = createWorld(uhcWorldString, seed, World.Environment.NORMAL, amplified);
            plugin.utils.message(sender, "Overworld created successfully.");
        } else {
            plugin.utils.message(sender, "Overworld has already been created, skipping it...");
        }

        if (plugin.uhc.toggle.isNether()) {
            if (netherWorld == null) {
                if (seed == null) {
                    plugin.utils.message(sender, "Creating <red>nether</red> with random seed.");
                } else if (netherSeed.equals(seed)) {
                    plugin.utils.message(sender, "Creating <red>nether</red> with the same seed as overworld: <red>" + netherSeed);
                } else {
                    plugin.utils.message(sender, "Creating <red>nether</red> with seed: <red>" + netherSeed);
                }

                netherWorld = createWorld(netherWorldString, netherSeed, World.Environment.NETHER, false);
                plugin.utils.message(sender, "Nether created successfully.");
            } else {
                plugin.utils.message(sender, "Nether has already been created, skipping it...");
            }
        }

        if (plugin.uhc.toggle.isEnd()) {
            if (endWorld == null) {
                if (seed == null) {
                    plugin.utils.message(sender, "Creating <purple>end</purple> with random seed.");
                } else if (endSeed.equals(seed)) {
                    plugin.utils.message(sender, "Creating <purple>end</purple> with the same seed as overworld: <purple>" + endSeed);
                } else {
                    plugin.utils.message(sender, "Creating <purple>end</purple> with seed: <purple>" + endSeed);
                }

                endWorld = createWorld(endWorldString, endSeed, World.Environment.THE_END, false);
                plugin.utils.message(sender, "End created successfully.");
            } else {
                plugin.utils.message(sender, "End has already been created, skipping it...");
            }
        }
    }

    public boolean areWorldsCreated() {
        return uhcWorld != null && (!plugin.uhc.toggle.isNether() || netherWorld != null) && (!plugin.uhc.toggle.isEnd() || endWorld != null);
    }

    private World createWorld(String name, Long seed, World.Environment env, boolean amplified) {
        WorldType type = amplified ? WorldType.AMPLIFIED : WorldType.NORMAL;
        return new WorldBuilder(name)
                .seed(seed)
                .type(type)
                .environment(env)
                .gamerule(GameRules.NATURAL_HEALTH_REGENERATION, false)
                .gamerule(GameRules.SPAWN_MOBS, false)
                .gamerule(GameRules.SHOW_ADVANCEMENT_MESSAGES, false)
                .gamerule(GameRules.IMMEDIATE_RESPAWN, true)
                .build();
    }
}
