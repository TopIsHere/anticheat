package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class VelocityCheck extends Check {
    private static final double KNOCKBACK_REDUCTION = 0.7;

    public VelocityCheck() {
        super("Velocity");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        return null;
    }

    public ViolationRecord checkDamage(Player player, double damage, PlayerCheckData data) {
        data.recordDamage(damage);

        if (damage > 0 && player.getVelocity().lengthSquared() < 0.1) {
            double violationLevel = 40;
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.VELOCITY,
                    violationLevel,
                    String.format("No knockback from %.1f damage", damage)
            );
        }

        return null;
    }
}