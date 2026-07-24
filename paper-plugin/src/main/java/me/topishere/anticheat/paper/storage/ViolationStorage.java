package me.topishere.anticheat.paper.storage;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import me.topishere.anticheat.paper.AnticheatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ViolationStorage {
    private final Queue<ViolationRecord> pendingViolations = new LinkedList<>();
    private final Map<UUID, List<ViolationRecord>> playerViolationHistory = new HashMap<>();
    private final Map<UUID, Integer> violationFlags = new HashMap<>();

    public void addViolation(ViolationRecord violation) {
        pendingViolations.offer(violation);
        
        playerViolationHistory.computeIfAbsent(violation.getPlayerUUID(), k -> new ArrayList<>())
                .add(violation);
    }

    public void processPendingViolations(AnticheatPlugin plugin) {
        while (!pendingViolations.isEmpty()) {
            ViolationRecord violation = pendingViolations.poll();
            
            if (violation.isHandled()) continue;

            Player player = Bukkit.getPlayer(violation.getPlayerUUID());
            if (player == null) continue;

            int flags = violationFlags.getOrDefault(violation.getPlayerUUID(), 0) + 1;
            violationFlags.put(violation.getPlayerUUID(), flags);

            plugin.getLogger().warning(String.format(
                    "[ANTICHEAT] %s flagged: %s | Evidence: %s | Level: %.1f | Flags: %d",
                    player.getName(),
                    violation.getCheatType().getDisplayName(),
                    violation.getEvidence(),
                    violation.getViolationLevel(),
                    flags
            ));

            if (violation.getViolationLevel() > 80) {
                player.kickPlayer("§c§lANTICHEAT §r§cYou have been kicked for suspected cheating.\n" +
                        "§cReason: " + violation.getCheatType().getDisplayName() + "\n" +
                        "§cAppeal at: §bhttps://example.com");
                plugin.getLogger().severe("KICKED: " + violation);
            } else if (violation.getViolationLevel() > 50 && flags > 3) {
                player.kickPlayer("§c§lANTICHEAT §r§cToo many violations detected.\n" +
                        "§cAppeal at: §bhttps://example.com");
                plugin.getLogger().severe("KICKED (Multiple Violations): " + violation);
            } else if (violation.getViolationLevel() > 60) {
                player.sendMessage("§c§lANTICHEAT §r§cWarning: Suspicious behavior detected.");
            }

            violation.setHandled(true);
        }
    }

    public List<ViolationRecord> getPlayerViolationHistory(UUID uuid) {
        return new ArrayList<>(playerViolationHistory.getOrDefault(uuid, new ArrayList<>()));
    }

    public int getPlayerViolationFlags(UUID uuid) {
        return violationFlags.getOrDefault(uuid, 0);
    }

    public void resetPlayerFlags(UUID uuid) {
        violationFlags.put(uuid, 0);
    }

    public void saveAllData() {
    }
}