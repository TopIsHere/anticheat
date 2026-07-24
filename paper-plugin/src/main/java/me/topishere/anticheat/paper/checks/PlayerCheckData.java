package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ClientType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerCheckData {
    private final UUID uuid;
    private final String playerName;

    private Vector lastLocation;
    private Vector currentLocation;
    private long lastUpdateTime;
    private final Queue<Long> movementPackets = new LinkedList<>();
    private double lastDeltaY;
    private int airTime;
    private boolean wasInAir;

    private final Queue<Long> attackTimes = new LinkedList<>();
    private final Map<UUID, Long> lastAttackTimes = new HashMap<>();
    private int rotationDifferences;
    private double headRotationSensitivity;

    private long lastBlockPlace;
    private long lastBlockBreak;
    private int blocksPlaced;
    private int blocksDestroyed;

    private final Queue<Double> damageHistory = new LinkedList<>();
    private double totalDamage;
    private int damageEvents;

    private final Map<CheatType, Integer> violationCounts = new HashMap<>();
    private double suspicionLevel;
    private ClientType detectedClient = ClientType.VANILLA;

    public PlayerCheckData(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.lastUpdateTime = System.currentTimeMillis();

        for (CheatType type : CheatType.values()) {
            violationCounts.put(type, 0);
        }
    }

    public void updateMovementData(Player player) {
        long currentTime = System.currentTimeMillis();
        Vector playerLoc = player.getLocation().toVector();

        if (lastLocation != null && currentLocation != null) {
            double distance = playerLoc.distance(currentLocation);

            if (distance > 0) {
                movementPackets.offer(currentTime);
                if (movementPackets.size() > 100) {
                    movementPackets.poll();
                }
            }
        }

        lastLocation = currentLocation;
        currentLocation = playerLoc;
        lastUpdateTime = currentTime;

        if (player.isOnGround()) {
            wasInAir = false;
            airTime = 0;
        } else {
            if (!wasInAir) {
                wasInAir = true;
                airTime = 0;
            } else {
                airTime++;
            }
        }

        lastDeltaY = playerLoc.getY() - (lastLocation != null ? lastLocation.getY() : playerLoc.getY());
    }

    public void recordAttack(UUID targetUUID) {
        long now = System.currentTimeMillis();
        attackTimes.offer(now);
        lastAttackTimes.put(targetUUID, now);
        if (attackTimes.size() > 50) {
            attackTimes.poll();
        }
    }

    public void recordBlockPlace() {
        lastBlockPlace = System.currentTimeMillis();
        blocksPlaced++;
    }

    public void recordBlockBreak() {
        lastBlockBreak = System.currentTimeMillis();
        blocksDestroyed++;
    }

    public void recordDamage(double damage) {
        long now = System.currentTimeMillis();
        damageHistory.offer(damage);
        totalDamage += damage;
        damageEvents++;

        while (!damageHistory.isEmpty() && now - 10000 > now) {
            damageHistory.poll();
        }
    }

    public void addViolation(CheatType type) {
        violationCounts.put(type, violationCounts.getOrDefault(type, 0) + 1);
        suspicionLevel += 5.0;
    }

    public double getAttacksPerSecond() {
        if (attackTimes.isEmpty()) return 0;
        long timeSpan = System.currentTimeMillis() - attackTimes.peek();
        if (timeSpan == 0) return 0;
        return (attackTimes.size() * 1000.0) / timeSpan;
    }

    public double getMovementPacketsPerSecond() {
        if (movementPackets.isEmpty()) return 0;
        long timeSpan = System.currentTimeMillis() - movementPackets.peek();
        if (timeSpan == 0) return 0;
        return (movementPackets.size() * 1000.0) / timeSpan;
    }

    public long getTimeSinceLastAttack(UUID targetUUID) {
        Long lastAttack = lastAttackTimes.get(targetUUID);
        if (lastAttack == null) return Long.MAX_VALUE;
        return System.currentTimeMillis() - lastAttack;
    }

    public long getTimeSinceLastBlockPlace() {
        return System.currentTimeMillis() - lastBlockPlace;
    }

    public long getTimeSinceLastBlockBreak() {
        return System.currentTimeMillis() - lastBlockBreak;
    }

    public double getAverageDamagePerEvent() {
        if (damageEvents == 0) return 0;
        return totalDamage / damageEvents;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Vector getLastLocation() {
        return lastLocation;
    }

    public Vector getCurrentLocation() {
        return currentLocation;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public int getAirTime() {
        return airTime;
    }

    public double getLastDeltaY() {
        return lastDeltaY;
    }

    public Map<CheatType, Integer> getViolationCounts() {
        return new HashMap<>(violationCounts);
    }

    public double getSuspicionLevel() {
        return suspicionLevel;
    }

    public void setSuspicionLevel(double level) {
        this.suspicionLevel = Math.max(0, Math.min(100, level));
    }

    public ClientType getDetectedClient() {
        return detectedClient;
    }

    public void setDetectedClient(ClientType client) {
        this.detectedClient = client;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public int getBlocksDestroyed() {
        return blocksDestroyed;
    }
}