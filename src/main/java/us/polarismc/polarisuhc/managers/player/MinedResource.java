package us.polarismc.polarisuhc.managers.player;

import lombok.Getter;
import org.bukkit.Material;


@Getter
public enum MinedResource {
    NETHERITE_SCRAP("Netherite Scrap", "#3B2F2F", Material.NETHERITE_SCRAP),
    DIAMOND("Diamond", "#55FFFF", Material.DIAMOND),
    GOLD( "Gold", "#FFD700", Material.GOLD_INGOT),
    IRON("Iron", "#D8D8D8", Material.IRON_INGOT);

    public final String displayName;
    public final String hex;
    public final Material material;

    MinedResource(String displayName, String hex, Material material) {
        this.displayName = displayName;
        this.hex = hex;
        this.material = material;
    }

    public String getMiniMessageHex() {
        return "<" + hex + ">";
    }

    public String getFormattedDisplayName() {
        return getMiniMessageHex() + displayName;
    }
}