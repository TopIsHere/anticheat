package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import me.topishere.anticheat.paper.AnticheatPlugin;
import org.bukkit.entity.Player;

import java.util.*;

public class CheckManager {
    private final AnticheatPlugin plugin;
    private final Map<UUID, PlayerCheckData> playerData = new HashMap<>();
    private final List<Check> checks;

    public CheckManager(AnticheatPlugin plugin) {
        this.plugin = plugin;
        this.checks = new ArrayList<>();

        checks.add(new AutoClickerCheck());
        checks.add(new KillAuraCheck());
        checks.add(new ReachCheck());
        checks.add(new SpeedCheck());
        checks.add(new FlyCheck());
        checks.add(new TimerCheck());
        checks.add(new NoClipCheck());
        checks.add(new VelocityCheck());
        checks.add(new FastPlaceCheck());
        checks.add(new FastBreakCheck());
    }

    public PlayerCheckData getOrCreatePlayerData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), 
                uuid -> new PlayerCheckData(player.getUniqueId(), player.getName()));
    }

    public void removePlayerData(UUID uuid) {
        playerData.remove(uuid);
    }

    public void handleMovement(Player player) {
        PlayerCheckData data = getOrCreatePlayerData(player);
        data.updateMovementData(player);

        for (Check check : checks) {
            if (check.shouldCheck(player)) {
                ViolationRecord violation = check.check(player, data);
                if (violation != null) {
                    plugin.getViolationStorage().addViolation(violation);
                }
            }
        }
    }

    public void handleAttack(Player attacker, Player victim) {
        PlayerCheckData data = getOrCreatePlayerData(attacker);
        data.recordAttack(victim.getUniqueId());

        KillAuraCheck killAuraCheck = (KillAuraCheck) checks.stream()
                .filter(c -> c instanceof KillAuraCheck)
                .findFirst()
                .orElse(null);

        if (killAuraCheck != null) {
            ViolationRecord violation = killAuraCheck.checkAttack(attacker, victim, data);
            if (violation != null) {
                plugin.getViolationStorage().addViolation(violation);
            }
        }

        ReachCheck reachCheck = (ReachCheck) checks.stream()
                .filter(c -> c instanceof ReachCheck)
                .findFirst()
                .orElse(null);

        if (reachCheck != null) {
            ViolationRecord violation = reachCheck.checkAttack(attacker, victim, data);
            if (violation != null) {
                plugin.getViolationStorage().addViolation(violation);
            }
        }
    }

    public void handleDamage(Player player, double damage) {
        PlayerCheckData data = getOrCreatePlayerData(player);
        
        VelocityCheck velocityCheck = (VelocityCheck) checks.stream()
                .filter(c -> c instanceof VelocityCheck)
                .findFirst()
                .orElse(null);

        if (velocityCheck != null) {
            ViolationRecord violation = velocityCheck.checkDamage(player, damage, data);
            if (violation != null) {
                plugin.getViolationStorage().addViolation(violation);
            }
        }
    }

    public void handleBlockPlace(Player player) {
        PlayerCheckData data = getOrCreatePlayerData(player);
        data.recordBlockPlace();

        FastPlaceCheck fastPlaceCheck = (FastPlaceCheck) checks.stream()
                .filter(c -> c instanceof FastPlaceCheck)
                .findFirst()
                .orElse(null);

        if (fastPlaceCheck != null) {
            ViolationRecord violation = fastPlaceCheck.check(player, data);
            if (violation != null) {
                plugin.getViolationStorage().addViolation(violation);
            }
        }
    }

    public void handleBlockBreak(Player player) {
        PlayerCheckData data = getOrCreatePlayerData(player);
        data.recordBlockBreak();

        FastBreakCheck fastBreakCheck = (FastBreakCheck) checks.stream()
                .filter(c -> c instanceof FastBreakCheck)
                .findFirst()
                .orElse(null);

        if (fastBreakCheck != null) {
            ViolationRecord violation = fastBreakCheck.check(player, data);
            if (violation != null) {
                plugin.getViolationStorage().addViolation(violation);
            }
        }
    }

    public Map<CheatType, Integer> getPlayerViolations(UUID uuid) {
        PlayerCheckData data = playerData.get(uuid);
        if (data == null) return new HashMap<>();
        return data.getViolationCounts();
    }

    public double getPlayerSuspicionLevel(UUID uuid) {
        PlayerCheckData data = playerData.get(uuid);
        if (data == null) return 0;
        return data.getSuspicionLevel();
    }
}