package us.polarismc.polarisuhc.managers.team;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

@Getter
public enum TeamColor {
    DARK_GREEN("Dark Green", "#00AA00", NamedTextColor.DARK_GREEN),
    DARK_AQUA("Dark Aqua", "#00AAAA", NamedTextColor.DARK_AQUA),
    DARK_RED("Dark Red", "#AA0000", NamedTextColor.DARK_RED),
    DARK_PURPLE("Dark Purple", "#AA00AA", NamedTextColor.DARK_PURPLE),
    GOLD("Gold", "#FFAA00", NamedTextColor.GOLD),
    GRAY("Gray", "#AAAAAA", NamedTextColor.GRAY),
    BLUE("Blue", "#5555FF", NamedTextColor.BLUE),
    GREEN("Green", "#55FF55", NamedTextColor.GREEN),
    AQUA("Aqua", "#55FFFF", NamedTextColor.AQUA),
    RED("Red", "#FF5555", NamedTextColor.RED),
    LIGHT_PURPLE("Light Purple", "#FF55FF", NamedTextColor.LIGHT_PURPLE),
    YELLOW("Yellow", "#FFFF55", NamedTextColor.YELLOW),
    WHITE("White", "#FFFFFF", NamedTextColor.WHITE),
    DEEP_PINK("Deep Pink", "#FF1493", NamedTextColor.LIGHT_PURPLE),
    MEDIUM_PURPLE("Medium Purple", "#9370DB", NamedTextColor.DARK_PURPLE),
    LIME_GREEN("Lime Green", "#32CD32", NamedTextColor.GREEN),
    TOMATO("Tomato", "#FF6347", NamedTextColor.RED),
    GOLDEN("Golden", "#FFD700", NamedTextColor.YELLOW),
    HOT_PINK("Hot Pink", "#FF69B4", NamedTextColor.LIGHT_PURPLE),
    SADDLE_BROWN("Saddle Brown", "#8B4513", NamedTextColor.GOLD),
    SEA_GREEN("Sea Green", "#2E8B57", NamedTextColor.DARK_GREEN),
    CRIMSON("Crimson", "#DC143C", NamedTextColor.RED),
    DEEP_SKY_BLUE("Deep Sky Blue", "#00BFFF", NamedTextColor.AQUA),
    ORANGE_RED("Orange Red", "#FF4500", NamedTextColor.RED),
    ORCHID("Orchid", "#DA70D6", NamedTextColor.LIGHT_PURPLE),
    ORANGE("Orange", "#FF8C00", NamedTextColor.GOLD),
    DARK_ORCHID("Dark Orchid", "#9932CC", NamedTextColor.DARK_PURPLE),
    SALMON("Salmon", "#E9967A", NamedTextColor.RED),
    LIGHT_SEA_GREEN("Light Sea Green", "#8FBC8F", NamedTextColor.GREEN),
    MEDIUM_SPRING_GREEN("Medium Spring Green", "#00FA9A", NamedTextColor.GREEN),
    TURQUOISE("Turquoise", "#48D1CC", NamedTextColor.AQUA),
    VIOLET_RED("Violet Red", "#C71585", NamedTextColor.DARK_PURPLE),
    LIGHT_CORAL("Light Coral", "#FF8888", NamedTextColor.RED),
    LIGHT_STEEL_BLUE("Light Steel Blue", "#B0C4DE", NamedTextColor.GRAY),
    LIGHT_YELLOW("Light Yellow", "#FFFFB9", NamedTextColor.YELLOW),
    LAWN_GREEN("Lawn Green", "#7CFC00", NamedTextColor.GREEN),
    YELLOW_GREEN("Yellow Green", "#ADFF2F", NamedTextColor.GREEN),
    AQUAMARINE("Aquamarine", "#7FFFD4", NamedTextColor.AQUA),
    BURGUNDY("Burgundy", "#C41E3A", NamedTextColor.DARK_RED),
    INDIGO("Indigo", "#6A5ACD", NamedTextColor.BLUE),
    TEAL("Teal", "#008080", NamedTextColor.DARK_AQUA),
    CORAL("Coral", "#FF7F50", NamedTextColor.RED),
    PEACH("Peach", "#FFCC99", NamedTextColor.GOLD),
    MINT("Mint", "#98FF98", NamedTextColor.GREEN),
    OLIVE("Olive", "#6B8E23", NamedTextColor.GREEN),
    SKY_BLUE("Sky Blue", "#87CEEB", NamedTextColor.AQUA),
    VIOLET("Violet", "#9400D3", NamedTextColor.DARK_PURPLE),
    BRONZE("Bronze", "#CD7F32", NamedTextColor.GOLD),
    EMERALD("Emerald", "#50C878", NamedTextColor.GREEN),
    SAPPHIRE("Sapphire", "#0F52BA", NamedTextColor.BLUE),
    SLATE("Slate", "#708090", NamedTextColor.GRAY),
    STEEL("Steel", "#4682B4", NamedTextColor.BLUE);

    private final String displayName;
    private final String hex;
    private final NamedTextColor fallbackColor;

    TeamColor(String displayName, String hex, NamedTextColor fallbackColor) {
        this.displayName = displayName;
        this.hex = hex;
        this.fallbackColor = fallbackColor;
    }

    public String getMiniMessageHex() {
        return "<" + hex + ">";
    }


    /**
     * Converts the given input string to the corresponding TeamColor instance.
     * The method uses different strategies to interpret the input, including:
     * - Matching the name of a predefined TeamColor (case-insensitive).
     * - Parsing hexadecimal color codes in formats such as "#RRGGBB" or "<#RRGGBB>".
     *
     * @param input the input string to be parsed into a TeamColor instance.
     *              If the input is null or cannot be matched, the method returns null.
     * @return the corresponding TeamColor instance if a match is found, or null if the input is invalid or not recognized.
     */
    public static @Nullable TeamColor fromInput(String input) {
        if (input == null) return null;
        String string = input.trim();

        String key = string.toUpperCase();
        for (TeamColor color : values()) {
            if (color.name().equals(key)) {
                return color;
            }
        }

        if (string.matches("^<#[A-Fa-f0-9]{6}>$")) {
            String hex = ("#" + string.substring(2, 8)).toUpperCase();
            return fromHex(hex);
        }

        if (string.matches("^#[A-Fa-f0-9]{6}$")) {
            return fromHex(string.toUpperCase());
        }

        return null;
    }

    private static TeamColor fromHex(String hex) {
        String hexString = normalizeHex(hex);
        for (TeamColor color : values()) {
            if (color.hex.equals(hexString)) {
                return color;
            }
        }
        return null;
    }

    private static String normalizeHex(String hex) {
        String hexString = hex.trim().toUpperCase();
        if (!hexString.startsWith("#")) hexString = "#" + hexString;
        if (!hexString.matches("^#[0-9A-F]{6}$")) throw new IllegalArgumentException("Invalid hex: " + hex);
        return hexString;
    }
}
