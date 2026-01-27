package us.polarismc.polarisuhc.managers.info.nametag;

import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public enum NametagPreset {
    NORMAL(Display.Billboard.CENTER, TextDisplay.TextAlignment.CENTER,
            false, false, 256f,
            true, 255,
            new Vector3f(-0.025f, 0.275f, 0.0f), new Vector3f(1f, 1f, 1f)),

    SNEAKING(Display.Billboard.CENTER, TextDisplay.TextAlignment.CENTER,
            false, false, 256f,
            false, 128,
            new Vector3f(-0.025f, 0.15f, 0.0f), new Vector3f(1f, 1f, 1f));

    private final Display.Billboard billboard;
    private final TextDisplay.TextAlignment alignment;

    private final boolean shadowed;
    private final boolean defaultBackground;
    private final float viewRange;

    private final boolean seeThrough;
    private final int opacity;

    private final Vector3f translation;
    private final Vector3f scale;

    NametagPreset(Display.Billboard billboard, TextDisplay.TextAlignment alignment,
                  boolean shadowed, boolean defaultBackground, float viewRange,
                  boolean seeThrough, int opacity,
                  Vector3f translation, Vector3f scale) {
        this.billboard = billboard;
        this.alignment = alignment;
        this.shadowed = shadowed;
        this.defaultBackground = defaultBackground;
        this.viewRange = viewRange;
        this.seeThrough = seeThrough;
        this.opacity = clamp(opacity);
        this.translation = new Vector3f(translation);
        this.scale = new Vector3f(scale);
    }

    public void apply(TextDisplay display) {
        display.setBillboard(billboard);
        display.setAlignment(alignment);

        display.setShadowed(shadowed);
        display.setDefaultBackground(defaultBackground);
        display.setViewRange(viewRange);

        display.setSeeThrough(seeThrough);
        display.setTextOpacity((byte) (opacity & 0xFF));

        display.setTransformation(new Transformation(new Vector3f(translation), new Quaternionf(), new Vector3f(scale), new Quaternionf()));
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
