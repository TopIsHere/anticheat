package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class KillAuraCheck extends Check {
    private static final double SUSPICION_THRESHOLD = 30.0;

    public KillAuraCheck() {
        super("KillAura");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        return null;
    }

    public ViolationRecord checkAttack(Player attacker, Player victim, PlayerCheckData data) {
        Vector attackerEye = attacker.getEyeLocation().toVector();
        Vector victimPos = victim.getLocation().toVector();
        Vector direction = victimPos.subtract(attackerEye).normalize();

        Vector attackerLook = attacker.getLocation().getDirection();

        double angle = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, direction.dot(attackerLook)))));

        if (angle < 5 && data.getAttacksPerSecond() > 15) {
            double violationLevel = Math.min(100, (15 - angle) * 3);
            return new ViolationRecord(
                    attacker.getUniqueId(),
                    CheatType.KILLAURA,
                    violationLevel,
                    String.format("Perfect tracking angle: %.1f°", angle)
            );
        }

        if (angle < 2) {
            return new ViolationRecord(
                    attacker.getUniqueId(),
                    CheatType.KILLAURA,
                    80.0,
                    String.format("Impossible tracking angle: %.1f°", angle)
            );
        }

        if (data.getAttacksPerSecond() > 20) {
            return new ViolationRecord(
                    attacker.getUniqueId(),
                    CheatType.KILLAURA,
                    50.0,
                    String.format("High CPS combined with tracking: %.1f CPS", data.getAttacksPerSecond())
            );
        }

        return null;
    }
}