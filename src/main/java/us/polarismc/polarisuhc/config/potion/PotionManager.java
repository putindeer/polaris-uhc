package us.polarismc.polarisuhc.config.potion;

import fr.mrmicky.fastinv.FastInv;
import io.papermc.paper.registry.keys.SoundEventKeys;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;

import java.util.function.BiFunction;

@Getter
@Setter
public class PotionManager {
    private final Main plugin;

    public PotionManager(Main plugin) {
        this.plugin = plugin;
        initializePotions();
    }

    public void togglePotion(Player p, PotionSetting setting, BiFunction<Player, Main, FastInv> guiCreator) {
        setting.next(plugin);

        switch (setting.get(plugin)) {
            case TRUE -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_ON, Sound.Source.MASTER, 10, 1));
            case TIER1 -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1.5f));
            case FALSE -> p.playSound(Sound.sound(SoundEventKeys.BLOCK_STONE_BUTTON_CLICK_OFF, Sound.Source.MASTER, 10, 1));
        }

        guiCreator.apply(p, plugin);
    }

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
        poisonPotion = PotionBoolean.valueOf(potions.getString("poison", "FALSE").toUpperCase());
        swiftnessPotion = PotionBoolean.valueOf(potions.getString("swiftness", "FALSE").toUpperCase());
        fireResistancePotion = PotionBoolean.valueOf(potions.getString("fire-resistance", "FALSE").toUpperCase());
        turtleMasterPotion = PotionBoolean.valueOf(potions.getString("turtle-master", "FALSE").toUpperCase());
        slownessPotion = PotionBoolean.valueOf(potions.getString("slowness", "FALSE").toUpperCase());
        invisibilityPotion = PotionBoolean.valueOf(potions.getString("invisibility", "FALSE").toUpperCase());
        regenerationPotion = PotionBoolean.valueOf(potions.getString("regeneration", "FALSE").toUpperCase());
        strengthPotion = PotionBoolean.valueOf(potions.getString("strength", "FALSE").toUpperCase());
        healingPotion = PotionBoolean.valueOf(potions.getString("healing", "FALSE").toUpperCase());
        harmingPotion = PotionBoolean.valueOf(potions.getString("harming", "FALSE").toUpperCase());
        waterBreathingPotion = PotionBoolean.valueOf(potions.getString("water-breathing", "FALSE").toUpperCase());
        weaknessPotion = PotionBoolean.valueOf(potions.getString("weakness", "FALSE").toUpperCase());
        leapingPotion = PotionBoolean.valueOf(potions.getString("leaping", "FALSE").toUpperCase());
        slowFallingPotion = PotionBoolean.valueOf(potions.getString("slow-falling", "FALSE").toUpperCase());
        infestationPotion = PotionBoolean.valueOf(potions.getString("infestation", "FALSE").toUpperCase());
        windChargingPotion = PotionBoolean.valueOf(potions.getString("wind-charging", "FALSE").toUpperCase());
        oozingPotion = PotionBoolean.valueOf(potions.getString("oozing", "FALSE").toUpperCase());
        weavingPotion = PotionBoolean.valueOf(potions.getString("weaving", "FALSE").toUpperCase());
    }
}
