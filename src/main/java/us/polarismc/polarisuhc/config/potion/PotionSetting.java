package us.polarismc.polarisuhc.config.potion;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum PotionSetting {
    POISON(PotionManager::getPoisonPotion, PotionManager::setPoisonPotion, PotionType.POISON, "Poison"),
    SWIFTNESS(PotionManager::getSwiftnessPotion, PotionManager::setSwiftnessPotion, PotionType.SWIFTNESS, "Swiftness"),
    FIRE_RESISTANCE(PotionManager::getFireResistancePotion, PotionManager::setFireResistancePotion, PotionType.FIRE_RESISTANCE, "Fire Resistance"),
    TURTLE_MASTER(PotionManager::getTurtleMasterPotion, PotionManager::setTurtleMasterPotion, PotionType.TURTLE_MASTER, "Turtle Master"),
    SLOWNESS(PotionManager::getSlownessPotion, PotionManager::setSlownessPotion, PotionType.SLOWNESS, "Slowness"),
    INVISIBILITY(PotionManager::getInvisibilityPotion, PotionManager::setInvisibilityPotion, PotionType.INVISIBILITY, "Invisibility"),
    REGENERATION(PotionManager::getRegenerationPotion, PotionManager::setRegenerationPotion, PotionType.REGENERATION, "Regeneration"),
    STRENGTH(PotionManager::getStrengthPotion, PotionManager::setStrengthPotion, PotionType.STRENGTH, "Strength"),
    HEALING(PotionManager::getHealingPotion, PotionManager::setHealingPotion, PotionType.HEALING, "Healing"),
    HARMING(PotionManager::getHarmingPotion, PotionManager::setHarmingPotion, PotionType.HARMING, "Harming"),
    WATER_BREATHING(PotionManager::getWaterBreathingPotion, PotionManager::setWaterBreathingPotion, PotionType.WATER_BREATHING, "Water Breathing"),
    WEAKNESS(PotionManager::getWeaknessPotion, PotionManager::setWeaknessPotion, PotionType.WEAKNESS, "Weakness"),
    LEAPING(PotionManager::getLeapingPotion, PotionManager::setLeapingPotion, PotionType.LEAPING, "Leaping"),
    SLOW_FALLING(PotionManager::getSlowFallingPotion, PotionManager::setSlowFallingPotion, PotionType.SLOW_FALLING, "Slow Falling"),
    INFESTATION(PotionManager::getInfestationPotion, PotionManager::setInfestationPotion, PotionType.INFESTED, "Infestation"),
    WIND_CHARGING(PotionManager::getWindChargingPotion, PotionManager::setWindChargingPotion, PotionType.WIND_CHARGED, "Wind Charging"),
    OOZING(PotionManager::getOozingPotion, PotionManager::setOozingPotion, PotionType.OOZING, "Oozing"),
    WEAVING(PotionManager::getWeavingPotion, PotionManager::setWeavingPotion, PotionType.WEAVING, "Weaving");

    private final Function<PotionManager, PotionBoolean> getter;
    private final BiConsumer<PotionManager, PotionBoolean> setter;
    @Getter
    private final PotionType type;
    @Getter
    private final Material material;
    private final String displayName;

    PotionSetting(Function<PotionManager, PotionBoolean> getter, BiConsumer<PotionManager, PotionBoolean> setter, PotionType type, String displayName) {
        this.getter = getter;
        this.setter = setter;
        this.type = type;
        this.material = Material.POTION;
        this.displayName = displayName;
    }

    public PotionBoolean get(Main plugin) {
        return getter.apply(plugin.uhc.getPotion());
    }

    public void set(Main plugin, PotionBoolean value) {
        setter.accept(plugin.uhc.getPotion(), value);
    }

    public void next(Main plugin) {
        set(plugin, get(plugin).next());
    }

    public ItemBuilder buildIcon(Main plugin) {
        PotionBoolean value = get(plugin);
        String color;
        String lore = switch (value) {
            case TRUE -> {
                color = "<green>";
                yield "Click to switch to <red>OFF</red> state.";
            }
            case TIER1 -> {
                color = "<yellow>";
                yield "Click to switch to <green>ON</green> state.";
            }
            default -> {
                color = "<red>";
                yield "Click to switch to <yellow>TIER 1</yellow> state.";
            }
        };
        return plugin.utils.ib(material).customName(color + displayName).lore(lore).potionType(type).hidePotionEffects();
    }

    public ItemBuilder buildToggleGlass(Main plugin) {
        PotionBoolean value = get(plugin);
        Material glassMaterial = switch (value) {
            case TRUE -> Material.LIME_STAINED_GLASS_PANE;
            case TIER1 -> Material.YELLOW_STAINED_GLASS_PANE;
            default -> Material.RED_STAINED_GLASS_PANE;
        };
        return plugin.utils.ib(glassMaterial).hideTooltip();
    }

    public static PotionSetting getByType(PotionType type) {
        for (PotionSetting setting : PotionSetting.values()) {
            if (setting.getType() == type) {
                return setting;
            }
        }
        return null;
    }
}