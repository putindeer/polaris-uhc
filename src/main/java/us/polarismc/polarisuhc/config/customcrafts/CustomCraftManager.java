package us.polarismc.polarisuhc.config.customcrafts;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class CustomCraftManager {
    private final Main plugin;
    private final Map<CustomCraftSetting, CustomCraft> crafts = new EnumMap<>(CustomCraftSetting.class);

    public CustomCraftManager(Main plugin) {
        this.plugin = plugin;

        for (CustomCraftSetting setting : CustomCraftSetting.values()) {
            CustomCraft craft = setting.create();
            if (craft.isEnabled()) {
                craft.enable();
            }
            crafts.put(setting, setting.create());
        }
    }

    public void disableAll() {
        crafts.values().stream().filter(CustomCraft::isEnabled).forEach(CustomCraft::disable);
    }

    public @Nullable CustomCraft getCraft(CustomCraftSetting setting) {
        return crafts.get(setting);
    }

    public boolean isEnabled(CustomCraftSetting setting) {
        return crafts.get(setting).isEnabled();
    }

    private void setCraftEnabled(CustomCraftSetting setting, boolean value) {
        CustomCraft craft = crafts.get(setting);
        if (value) {
            craft.enable();
        } else {
            craft.disable();
        }
    }

    public void toggle(CustomCraftSetting setting) {
        setCraftEnabled(setting, !isEnabled(setting));
    }

    public void openRecipePreview(Player player, CustomCraftSetting setting) {
        CustomCraft craft = crafts.get(setting);
        if (craft == null) return;

        craft.openPreview(player, "<blue>" + setting.getInfo().displayName() + " Recipe</blue>");
    }
}
