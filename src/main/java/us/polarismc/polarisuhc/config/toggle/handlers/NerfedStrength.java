package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;

@ToggleInfo(id = "nerfed-strength", icon = Material.POTION, displayName = "Nerfed Strength")
public class NerfedStrength extends ToggleHandler {
    private final NamespacedKey strengthNerfKey = new NamespacedKey(plugin, "strength_nerf_modifier");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getModifiedType() != PotionEffectType.STRENGTH) return;

        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attribute == null) return;

        switch (event.getAction()) {
            case ADDED, CHANGED -> {
                removeStrengthNerf(player);
                PotionEffect type = event.getNewEffect();
                if (type == null) return;
                int amplifier = type.getAmplifier();

                attribute.addModifier(createAttributeModifier(amplifier));
            }
            case REMOVED, CLEARED -> removeStrengthNerf(player);
        }
    }

    private AttributeModifier createAttributeModifier(int amplifier) {
        double nerfValue = (amplifier + 1) * 1.5;
        return new AttributeModifier(strengthNerfKey, -nerfValue, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
    }

    private void removeStrengthNerf(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attribute == null) return;
        attribute.removeModifier(strengthNerfKey);
    }

    public void removeAllStrengthNerfs() {
        Bukkit.getOnlinePlayers().forEach(this::removeStrengthNerf);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeStrengthNerf(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.STRENGTH)) return;

        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attribute == null) return;

        PotionEffect effect = player.getPotionEffect(PotionEffectType.STRENGTH);
        if (effect == null) return;
        int amplifier = effect.getAmplifier();

        attribute.addModifier(createAttributeModifier(amplifier));
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        removeAllStrengthNerfs();
    }
}