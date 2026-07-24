package me.topishere.anticheat.common;

import java.util.Locale;

public enum ClientType {
    VANILLA("Vanilla", false),
    METEOR("Meteor", true),
    WURST("Wurst", true),
    IMPACT("Impact", true),
    FUTURE("Future", true),
    BARITONE("Baritone", true),
    ARISTOIS("Aristois", true),
    MINEMENTAT("MinemenTat", true),
    RUSHERHACK("RusherHack", true),
    LIQUIDBOUNCE("LiquidBounce", true),
    NOVOLINE("Novoline", true),
    SKIDDERMOD("SkidderMod", true),
    ARES("Ares", true),
    BLEACHHACK("BleachHack", true),
    CRYPTIC("Cryptic", true),
    SIGMA("Sigma", true),
    UNKNOWNMOD("UnknownMod", true);

    private final String displayName;
    private final boolean isSuspicious;

    ClientType(String displayName, boolean isSuspicious) {
        this.displayName = displayName;
        this.isSuspicious = isSuspicious;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSuspicious() {
        return isSuspicious;
    }

    public static ClientType fromResourcePack(String hash) {
        if (hash == null || hash.isEmpty()) {
            return VANILLA;
        }

        String hashLower = hash.toLowerCase(Locale.ROOT);

        if (hashLower.contains("meteor")) return METEOR;
        if (hashLower.contains("wurst")) return WURST;
        if (hashLower.contains("impact")) return IMPACT;
        if (hashLower.contains("future")) return FUTURE;
        if (hashLower.contains("baritone")) return BARITONE;
        if (hashLower.contains("aristois")) return ARISTOIS;
        if (hashLower.contains("minementat")) return MINEMENTAT;
        if (hashLower.contains("rusherhack")) return RUSHERHACK;
        if (hashLower.contains("liquidbounce")) return LIQUIDBOUNCE;
        if (hashLower.contains("novoline")) return NOVOLINE;
        if (hashLower.contains("skidder")) return SKIDDERMOD;
        if (hashLower.contains("ares")) return ARES;
        if (hashLower.contains("bleach")) return BLEACHHACK;
        if (hashLower.contains("cryptic")) return CRYPTIC;
        if (hashLower.contains("sigma")) return SIGMA;

        return UNKNOWNMOD;
    }
}