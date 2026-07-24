package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class SpeedCheck extends Check {
    private static final double LEGIT_SPEED = 0.5;
    private static final double HIGH_SPEED = 0.75;
    private static final double EXTREME_SPEED = 1.2;

    public SpeedCheck() {
        super("Speed");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        if (data.getCurrentLocation() == null || data.getLastLocation() == null) {
            return null;
        }

        double distance = data.getCurrentLocation().distance(data.getLastLocation());
        long timeDelta = System.currentTimeMillis() - data.getLastUpdateTime();

        if (timeDelta < 50) return null;

        double speed = distance / (timeDelta / 1000.0);

        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            return null;
        }

        if (speed > EXTREME_SPEED && player.isOnGround()) {
            double violationLevel = Math.min(100, (speed - EXTREME_SPEED) * 20);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.SPEED,
                    violationLevel,
                    String.format("Extreme speed: %.2f blocks/sec", speed)
            );
        }

        if (speed > HIGH_SPEED && player.isOnGround()) {
            double violationLevel = (speed - HIGH_SPEED) * 10;
            if (violationLevel > 15) {
                return new ViolationRecord(
                        player.getUniqueId(),
                        CheatType.SPEED,
                        violationLevel,
                        String.format("High speed: %.2f blocks/sec", speed)
                );
            }
        }

        return null;
    }
}