package me.topishere.anticheat.common;

import java.util.UUID;

public class ViolationRecord {
    private final UUID playerUUID;
    private final CheatType cheatType;
    private final double violationLevel;
    private final String evidence;
    private final long timestamp;
    private boolean handled;

    public ViolationRecord(UUID playerUUID, CheatType cheatType, double violationLevel, String evidence) {
        this.playerUUID = playerUUID;
        this.cheatType = cheatType;
        this.violationLevel = violationLevel;
        this.evidence = evidence;
        this.timestamp = System.currentTimeMillis();
        this.handled = false;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public CheatType getCheatType() {
        return cheatType;
    }

    public double getViolationLevel() {
        return violationLevel;
    }

    public String getEvidence() {
        return evidence;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    @Override
    public String toString() {
        return String.format("%s detected %s (Level: %.2f) - %s",
                playerUUID, cheatType.getDisplayName(), violationLevel, evidence);
    }
}