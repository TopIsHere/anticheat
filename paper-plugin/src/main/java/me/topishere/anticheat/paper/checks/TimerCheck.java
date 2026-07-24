package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class TimerCheck extends Check {
    private static final double LEGIT_PACKETS_PER_SECOND = 20.0;
    private static final double HIGH_PACKETS_THRESHOLD = 22.0;
    private static final double EXTREME_PACKETS_THRESHOLD = 30.0;

    public TimerCheck() {
        super("Timer");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        double pps = data.getMovementPacketsPerSecond();

        if (pps == 0) return null;

        if (pps > EXTREME_PACKETS_THRESHOLD) {
            double violationLevel = Math.min(100, (pps - EXTREME_PACKETS_THRESHOLD) * 5);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.TIMER,
                    violationLevel,
                    String.format("Extreme packet rate: %.1f packets/sec", pps)
            );
        }

        if (pps > HIGH_PACKETS_THRESHOLD) {
            double violationLevel = (pps - HIGH_PACKETS_THRESHOLD) * 8;
            if (violationLevel > 15) {
                return new ViolationRecord(
                        player.getUniqueId(),
                        CheatType.TIMER,
                        violationLevel,
                        String.format("High packet rate: %.1f packets/sec", pps)
                );
            }
        }

        return null;
    }
}