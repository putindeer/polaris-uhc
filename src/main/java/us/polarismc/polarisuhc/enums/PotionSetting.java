package us.polarismc.polarisuhc.enums;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;
import me.putindeer.api.util.builder.ItemBuilder;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum PotionSetting {
    POISON(UHCManager::getPoisonPotion, UHCManager::setPoisonPotion, PotionType.POISON),
    SWIFTNESS(UHCManager::getSwiftnessPotion, UHCManager::setSwiftnessPotion, PotionType.SWIFTNESS),
    FIRE_RESISTANCE(UHCManager::getFireResistancePotion, UHCManager::setFireResistancePotion, PotionType.FIRE_RESISTANCE),
    TURTLE_MASTER(UHCManager::getTurtleMasterPotion, UHCManager::setTurtleMasterPotion, PotionType.TURTLE_MASTER),
    SLOWNESS(UHCManager::getSlownessPotion, UHCManager::setSlownessPotion, PotionType.SLOWNESS),
    INVISIBILITY(UHCManager::getInvisibilityPotion, UHCManager::setInvisibilityPotion, PotionType.INVISIBILITY),
    REGENERATION(UHCManager::getRegenerationPotion, UHCManager::setRegenerationPotion, PotionType.REGENERATION),
    STRENGTH(UHCManager::getStrengthPotion, UHCManager::setStrengthPotion, PotionType.STRENGTH),
    HEALING(UHCManager::getHealingPotion, UHCManager::setHealingPotion, PotionType.HEALING),
    HARMING(UHCManager::getHarmingPotion, UHCManager::setHarmingPotion, PotionType.HARMING),
    WATER_BREATHING(UHCManager::getWaterBreathingPotion, UHCManager::setWaterBreathingPotion, PotionType.WATER_BREATHING),
    WEAKNESS(UHCManager::getWeaknessPotion, UHCManager::setWeaknessPotion, PotionType.WEAKNESS),
    LEAPING(UHCManager::getLeapingPotion, UHCManager::setLeapingPotion, PotionType.LEAPING),
    SLOW_FALLING(UHCManager::getSlowFallingPotion, UHCManager::setSlowFallingPotion, PotionType.SLOW_FALLING),
    INFESTATION(UHCManager::getInfestationPotion, UHCManager::setInfestationPotion, PotionType.INFESTED),
    WIND_CHARGING(UHCManager::getWindChargingPotion, UHCManager::setWindChargingPotion, PotionType.WIND_CHARGED),
    OOZING(UHCManager::getOozingPotion, UHCManager::setOozingPotion, PotionType.OOZING),
    WEAVING(UHCManager::getWeavingPotion, UHCManager::setWeavingPotion, PotionType.WEAVING);

    private final Function<UHCManager, PotionBoolean> getter;
    private final BiConsumer<UHCManager, PotionBoolean> setter;
    @Getter
    private final PotionType type;
    @Getter
    private final Material material;

    PotionSetting(Function<UHCManager, PotionBoolean> getter, BiConsumer<UHCManager, PotionBoolean> setter, PotionType type) {
        this.getter = getter;
        this.setter = setter;
        this.type = type;
        this.material = Material.POTION;
    }

    public PotionBoolean get(UHCManager manager) {
        return getter.apply(manager);
    }

    public void set(UHCManager manager, PotionBoolean value) {
        setter.accept(manager, value);
    }

    public void next(UHCManager manager) {
        set(manager, get(manager).next());
    }

    public ItemBuilder buildIcon(Main plugin) {
        PotionBoolean value = get(plugin.uhc);
        String color;
        String loreKey = switch (value) {
            case ON -> {
                color = "<green>";
                yield "potion.to_off";
            }
            case TIER1 -> {
                color = "<yellow>";
                yield "potion.to_on";
            }
            default -> {
                color = "<red>";
                yield "potion.to_tier1";
            }
        };
        return plugin.utils.ib(material).customName(color + "[lang]potion." + name().toLowerCase() + "[/lang]").lore("[lang]" + loreKey + "[/lang]").potionType(type).hidePotionEffects();
    }

    public ItemBuilder buildToggleGlass(Main plugin) {
        PotionBoolean value = get(plugin.uhc);
        Material glassMaterial = switch (value) {
            case ON -> Material.LIME_STAINED_GLASS_PANE;
            case TIER1 -> Material.YELLOW_STAINED_GLASS_PANE;
            default -> Material.RED_STAINED_GLASS_PANE;
        };
        return plugin.utils.ib(glassMaterial).hideTooltip();
    }
}