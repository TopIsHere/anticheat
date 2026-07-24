package me.topishere.anticheat.paper.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, PlayerStats> playerStats = new HashMap<>();

    public PlayerStats getOrCreateStats(UUID uuid) {
        return playerStats.computeIfAbsent(uuid, k -> new PlayerStats(uuid));
    }

    public void removeStats(UUID uuid) {
        playerStats.remove(uuid);
    }

    public void saveAllData() {
    }

    public static class PlayerStats {
        private final UUID uuid;
        private long lastCheckTime;
        private int totalViolations;
        private boolean banned;
        private long banTime;

        public PlayerStats(UUID uuid) {
            this.uuid = uuid;
            this.lastCheckTime = System.currentTimeMillis();
            this.totalViolations = 0;
            this.banned = false;
            this.banTime = 0;
        }

        public UUID getUUID() {
            return uuid;
        }

        public long getLastCheckTime() {
            return lastCheckTime;
        }

        public void setLastCheckTime(long time) {
            this.lastCheckTime = time;
        }

        public int getTotalViolations() {
            return totalViolations;
        }

        public void incrementViolations() {
            this.totalViolations++;
        }

        public boolean isBanned() {
            return banned;
        }

        public void ban() {
            this.banned = true;
            this.banTime = System.currentTimeMillis();
        }

        public void unban() {
            this.banned = false;
        }

        public long getBanTime() {
            return banTime;
        }
    }
}