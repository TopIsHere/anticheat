package me.topishere.anticheat.common;

public enum CheatType {
    AUTOCLICKER("AutoClicker", "Rapid clicking detection"),
    KILLAURA("KillAura", "Automatic targeting and hitting"),
    REACH("Reach", "Extended hit distance"),
    SPEED("Speed", "Movement speed hacks"),
    FLY("Fly", "Flight hacking"),
    TIMER("Timer", "Client-side timer manipulation"),
    NOCLIP("NoClip", "Walking through blocks"),
    SCAFFOLD("Scaffold", "Automatic block placement"),
    VELOCITY("Velocity", "Knockback resistance"),
    BLINK("Blink", "Teleportation hacks"),
    CHEST_STEALER("ChestStealer", "Automatic chest looting"),
    GLIDE("Glide", "Reduced fall damage"),
    JESUS("Jesus", "Water walking"),
    PHASE("Phase", "Phase through blocks"),
    FASTPLACE("FastPlace", "Rapid block placement"),
    FASTBREAK("FastBreak", "Rapid block breaking");

    private final String displayName;
    private final String description;

    CheatType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}