package us.polarismc.polarisuhc.managers.uhc;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIResult;
import de.rapha149.signgui.exception.SignGUIVersionException;
import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import me.putindeer.api.util.builder.WorldBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.enums.PotionBoolean;
import us.polarismc.polarisuhc.enums.PotionSetting;
import us.polarismc.polarisuhc.enums.ToggleSetting;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        if (areWorldsCreated()) {
            plugin.utils.message(sender, "[lang]worlds.already_created[/lang]");
            return;
        }

        if (uhcWorld == null) {
            if (amplified) {
                if (seed == null) {
                    plugin.utils.message(sender, "[lang]worlds.creating_overworld_amplified_random[/lang]");
                } else {
                    plugin.utils.message(sender, "[lang]worlds.creating_overworld_amplified_set[/lang] <green>" + seed);
                }
            } else {
                if (seed == null) {
                    plugin.utils.message(sender, "[lang]worlds.creating_overworld_random[/lang]");
                } else {
                    plugin.utils.message(sender, "[lang]worlds.creating_overworld_set[/lang] <green>" + seed);
                }
            }

            uhcWorld = createWorld(uhcWorldString, seed, World.Environment.NORMAL, amplified);
            plugin.utils.message(sender, "[lang]worlds.overworld_created[/lang]");
        } else {
            plugin.utils.message(sender, "[lang]worlds.overworld_exists[/lang]");
        }

        if (nether) {
            if (netherWorld == null) {
                if (seed == null) {
                    plugin.utils.message(sender, "[lang]worlds.creating_nether_random[/lang]");
                } else if (netherSeed.equals(seed)) {
                    plugin.utils.message(sender, "[lang]worlds.creating_nether_same[/lang] <red>" + netherSeed);
                } else {
                    plugin.utils.message(sender, "[lang]worlds.creating_nether_set[/lang] <red>" + netherSeed);
                }

                netherWorld = createWorld(netherWorldString, netherSeed, World.Environment.NETHER, false);
                plugin.utils.message(sender, "[lang]worlds.nether_created[/lang]");
            } else {
                plugin.utils.message(sender, "[lang]worlds.nether_exists[/lang]");
            }
        }

        if (end) {
            if (endWorld == null) {
                if (seed == null) {
                    plugin.utils.message(sender, "[lang]worlds.creating_end_random[/lang]");
                } else if (endSeed.equals(seed)) {
                    plugin.utils.message(sender, "[lang]worlds.creating_end_same[/lang] <purple>" + endSeed);
                } else {
                    plugin.utils.message(sender, "[lang]worlds.creating_end_set[/lang] <purple>" + endSeed);
                }

                endWorld = createWorld(endWorldString, endSeed, World.Environment.THE_END, false);
                plugin.utils.message(sender, "[lang]worlds.end_created[/lang]");
            } else {
                plugin.utils.message(sender, "[lang]worlds.end_exists[/lang]");
            }
        }
    }

    public boolean areWorldsCreated() {
        return uhcWorld != null && (!nether || netherWorld != null) && (!end || endWorld != null);
    }

    private World createWorld(String name, Long seed, World.Environment env, boolean amplified) {
        WorldType type = amplified ? WorldType.AMPLIFIED : WorldType.NORMAL;
        return new WorldBuilder(name)
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
    private boolean antiBurn;
    private boolean autoLS;
    private boolean bookshelves;
    private boolean end;
    private boolean explosives;
    private boolean fireAspect;
    private boolean flame;
    private boolean horses;
    private boolean mobs;
    private boolean nerfedStrength;
    private boolean nether;
    private boolean notch;
    private boolean starterBooks;
    private boolean stats;
    private boolean trades;

    private void initializeToggle() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle");
        assert config != null;
        advancements = config.getBoolean("advancements");
        antiBurn = config.getBoolean("antiburn");
        autoLS = config.getBoolean("autols");
        bookshelves = config.getBoolean("bookshelves");
        end = config.getBoolean("end");
        explosives = config.getBoolean("explosives");
        fireAspect = config.getBoolean("fireaspect");
        flame = config.getBoolean("flame");
        horses = config.getBoolean("horses");
        mobs = config.getBoolean("mobs");
        nerfedStrength = config.getBoolean("nerfedstrength");
        nether = config.getBoolean("nether");
        notch = config.getBoolean("notch");
        starterBooks = config.getBoolean("starterbooks");
        stats = config.getBoolean("stats");
        trades = config.getBoolean("trades");
        initializePotions();
        initializeCustomCrafts();
    }

    // Pots
    private PotionBoolean poisonPotion;
    private PotionBoolean swiftnessPotion;
    private PotionBoolean fireResistancePotion;
    private PotionBoolean turtleMasterPotion;
    private PotionBoolean slownessPotion;
    private PotionBoolean invisibilityPotion;
    private PotionBoolean regenerationPotion;
    private PotionBoolean strengthPotion;
    private PotionBoolean healingPotion;
    private PotionBoolean harmingPotion;
    private PotionBoolean waterBreathingPotion;
    private PotionBoolean weaknessPotion;
    private PotionBoolean leapingPotion;
    private PotionBoolean slowFallingPotion;
    private PotionBoolean infestationPotion;
    private PotionBoolean windChargingPotion;
    private PotionBoolean oozingPotion;
    private PotionBoolean weavingPotion;

    private void initializePotions() {
        ConfigurationSection potions = plugin.getConfig().getConfigurationSection("toggle.potions");
        assert potions != null;
        poisonPotion = PotionBoolean.valueOf(potions.getString("poison", "OFF").toUpperCase());
        swiftnessPotion = PotionBoolean.valueOf(potions.getString("swiftness", "OFF").toUpperCase());
        fireResistancePotion = PotionBoolean.valueOf(potions.getString("fireresistance", "OFF").toUpperCase());
        turtleMasterPotion = PotionBoolean.valueOf(potions.getString("turtlemaster", "OFF").toUpperCase());
        slownessPotion = PotionBoolean.valueOf(potions.getString("slowness", "OFF").toUpperCase());
        invisibilityPotion = PotionBoolean.valueOf(potions.getString("invisibility", "OFF").toUpperCase());
        regenerationPotion = PotionBoolean.valueOf(potions.getString("regeneration", "OFF").toUpperCase());
        strengthPotion = PotionBoolean.valueOf(potions.getString("strength", "OFF").toUpperCase());
        healingPotion = PotionBoolean.valueOf(potions.getString("healing", "OFF").toUpperCase());
        harmingPotion = PotionBoolean.valueOf(potions.getString("harming", "OFF").toUpperCase());
        waterBreathingPotion = PotionBoolean.valueOf(potions.getString("waterbreathing", "OFF").toUpperCase());
        weaknessPotion = PotionBoolean.valueOf(potions.getString("weakness", "OFF").toUpperCase());
        leapingPotion = PotionBoolean.valueOf(potions.getString("leaping", "OFF").toUpperCase());
        slowFallingPotion = PotionBoolean.valueOf(potions.getString("slowfalling", "OFF").toUpperCase());
        infestationPotion = PotionBoolean.valueOf(potions.getString("infestation", "OFF").toUpperCase());
        windChargingPotion = PotionBoolean.valueOf(potions.getString("windcharging", "OFF").toUpperCase());
        oozingPotion = PotionBoolean.valueOf(potions.getString("oozing", "OFF").toUpperCase());
        weavingPotion = PotionBoolean.valueOf(potions.getString("weaving", "OFF").toUpperCase());
    }

    // Custom Crafts
    private boolean totemCraft;
    private boolean maceCraft;
    private boolean breezeRodCraft;
    private boolean tridentCraft;
    private boolean elytraCraft;
    private boolean goldenHeadCraft;
    private boolean spectralArrowCraft;
    private boolean glisteringMelonCraft;
    private boolean tntMinecartCraft;
    private boolean lecternCraft;
    private void initializeCustomCrafts() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("toggle.customcrafts");
        assert config != null;
        totemCraft = config.getBoolean("totem");
        maceCraft = config.getBoolean("mace");
        breezeRodCraft = config.getBoolean("breezerod");
        tridentCraft = config.getBoolean("trident");
        elytraCraft = config.getBoolean("elytra");
        goldenHeadCraft = config.getBoolean("goldenhead");
        spectralArrowCraft = config.getBoolean("spectralarrow");
        glisteringMelonCraft = config.getBoolean("glisteringmelon");
        tntMinecartCraft = config.getBoolean("tntminecart");
        lecternCraft = config.getBoolean("lectern");
    }
    //endregion

    //region [Duración]
    private int pvpTime;
    private int meetupTime;
    private int finalHealTime;

    private void initializeDuration() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("rates");
        assert config != null;
        pvpTime = config.getInt("pvp");
        meetupTime = config.getInt("meetup");
        finalHealTime = config.getInt("finalheal");
    }
    //endregion

    //region [Borde]
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
    //endregion

    //region [Rates]
    private int xpKillRate;
    private int flintRate;
    private int appleRate;
    private int glassRate;

    private void initializeRates() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("rates");
        assert config != null;
        xpKillRate = config.getInt("xpkill");
        flintRate = config.getInt("flint");
        appleRate = config.getInt("apple");
        glassRate = config.getInt("glass");
    }
    //endregion

    public ItemStack goBack(Player player) {
        return plugin.utils.ib(Material.BARRIER).name("[lang]common.go_back[/lang]").lore("[lang]common.go_back_desc[/lang]").build();
    }

    public void toggleSetting(Player p, ToggleSetting setting, BiFunction<Player, Main, FastInv> guiCreator) {
        setting.toggle(this);

        if (setting.get(this)) {
            p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
        } else {
            p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        }

        guiCreator.apply(p, plugin);
    }

    public void toggleSetting(Player p, PotionSetting setting, BiFunction<Player, Main, FastInv> guiCreator) {
        setting.next(this);

        switch (setting.get(this)) {
            case ON -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
            case TIER1 -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1.5f));
            case OFF -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        }

        guiCreator.apply(p, plugin);
    }

    public void increase(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, int maximum, BiFunction<Player, Main, FastInv> guiCreator) {
        Player player = (Player) event.getWhoClicked();
        if (getter.get() >= maximum) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = event.getClick().isRightClick() ? 5 : 1;
        int newValue = Math.min(getter.get() + value, maximum);
        float pitch = event.getClick().isRightClick() ? 1.5f : 1;
        setter.accept(newValue);
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, pitch));
        guiCreator.apply(player, plugin);
    }

    public void decrease(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, BiFunction<Player, Main, FastInv> guiCreator) {
        Player player = (Player) event.getWhoClicked();
        if (getter.get() <= minimum) {
            player.playSound(Sound.sound(SoundEventKeys.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
            return;
        }
        int value = event.getClick().isRightClick() ? 5 : 1;
        int newValue = Math.max(getter.get() - value, minimum);
        float pitch = event.getClick().isRightClick() ? 1.5f : 1;
        setter.accept(newValue);
        player.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, pitch));
        guiCreator.apply(player, plugin);
    }

    public void openIntInputSign(Player player, Supplier<Integer> getter, Consumer<Integer> setter, int minimum, int maximum, BiFunction<Player, Main, FastInv> guiCreator) {
        try {
            Object[] lines = new Object[] {
                    plugin.utils.chat(""),
                    plugin.utils.chat("[lang]common.new_value[/lang]"),
                    plugin.utils.chat("[lang]common.current_value[/lang]"),
                    plugin.utils.chat(String.valueOf(getter.get()))
            };
            //TODO - cambia esto a pale oak sign cuando lo añadan xdd

            SignGUI gui = SignGUI.builder().setAdventureLines(lines).setType(Material.CHERRY_SIGN).setHandler((p, result) -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        int value = extractFirstNumber(result);
                        value = Math.max(minimum, Math.min(value, maximum));
                        setter.accept(value);
                        p.playSound(Sound.sound(SoundEventKeys.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 10, 2));
                        guiCreator.apply(p, plugin);
                    } catch (NumberFormatException e) {
                        plugin.utils.message(p, "[lang]common.invalid_number[/lang]");
                        guiCreator.apply(p, plugin);
                    }
                });
                return Collections.emptyList();
            }).build();

            gui.open(player);
        } catch (SignGUIVersionException e) {
            plugin.utils.message(player, "[lang]common.error[/lang]");
            plugin.utils.severe(e.getMessage());
        }
    }

    public void increase(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        increase(event, getter, setter, Integer.MAX_VALUE, guiCreator);
    }

    public void decrease(InventoryClickEvent event, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        decrease(event, getter, setter, 1, guiCreator);
    }

    public void openIntInputSign(Player player, Supplier<Integer> getter, Consumer<Integer> setter, BiFunction<Player, Main, FastInv> guiCreator) {
        openIntInputSign(player, getter, setter, 1, Integer.MAX_VALUE, guiCreator);
    }

    private int extractFirstNumber(SignGUIResult result) throws NumberFormatException {
        String line = result.getLineWithoutColor(0).trim();
        String numeric = line.replaceAll("[^0-9]", "");

        if (!numeric.isEmpty()) {
            int value = Integer.parseInt(numeric);
            if (value > 0) return value;
        }

        throw new NumberFormatException("The first line does not contain a valid positive integer.");
    }
}
