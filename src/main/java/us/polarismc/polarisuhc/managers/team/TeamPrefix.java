package us.polarismc.polarisuhc.managers.team;

import lombok.Getter;

@Getter
public enum TeamPrefix {
    PICKAXE("⛏"),
    TRIDENT("🔱"),
    AXE("🪓"),
    SHIELD("🛡"),
    SWORD("🗡"),
    BOW("🏹"),
    FISHING_ROD("🎣"),
    POTION("⚗"),
    FIRE("🔥"),
    SNOWMAN("⛄"),
    RAIN("🌧"),
    STAR("⭐"),
    LIGHTNING("⚡"),
    PEN("✎"),
    SKULL("☠"),
    HOURGLASS("⌛"),
    WATCH("⌚"),
    CROSSED_SWORDS("⚔"),
    MOON("☽"),
    SUN("☀"),
    ANCHOR("⚓"),
    UMBRELLA("☂"),
    COMET("☄"),
    TARGET("◎"),
    BELL("🔔"),
    PEACE("☮"),
    YIN_YANG("☯"),
    INFINITY("∞"),
    OMEGA("Ω"),
    ALPHA("α"),
    DELTA("Δ"),
    NOTE("♪"),
    X("⌘"),
    LAMBDA("λ"),
    FOUR_POINT("✦"),
    DANGER("⚠"),
    CROSS("✝"),
    SNOWFLAKE("❄");

    private final String symbol;

    TeamPrefix(String symbol) {
        this.symbol = symbol;
    }
}