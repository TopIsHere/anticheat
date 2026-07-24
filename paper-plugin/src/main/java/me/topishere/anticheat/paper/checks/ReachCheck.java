package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class ReachCheck extends Check {
    private static final double LEGIT_REACH = 3.0;
    private static final double REACH_THRESHOLD = 4.0;
    private static final double EXTREME_REACH = 6.0;

    public ReachCheck() {
        super("Reach");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        return null;
    }

    public ViolationRecord checkAttack(Player attacker, Player victim, PlayerCheckData data) {
        double distance = attacker.getLocation().distance(victim.getLocation());

        if (distance > EXTREME_REACH) {
            double violationLevel = Math.min(100, (distance - EXTREME_REACH) * 10);
            return new ViolationRecord(
                    attacker.getUniqueId(),
                    CheatType.REACH,
                    violationLevel,
                    String.format("Extreme reach: %.2f blocks (limit: %.1f)", distance, EXTREME_REACH)
            );
        }

        if (distance > REACH_THRESHOLD) {
            double violationLevel = (distance - REACH_THRESHOLD) * 5;
            if (violationLevel > 15) {
                return new ViolationRecord(
                        attacker.getUniqueId(),
                        CheatType.REACH,
                        violationLevel,
                        String.format("Extended reach: %.2f blocks (limit: %.1f)", distance, REACH_THRESHOLD)
                );
            }
        }

        return null;
    }
}