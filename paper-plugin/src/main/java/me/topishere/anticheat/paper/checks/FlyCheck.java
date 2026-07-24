package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FlyCheck extends Check {
    private static final int MAX_LEGIT_AIR_TIME = 40;
    private static final double ASCENSION_THRESHOLD = 0.5;

    public FlyCheck() {
        super("Fly");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        if (player.isOnGround() || player.hasPotionEffect(PotionEffectType.LEVITATION)) {
            return null;
        }

        int airTime = data.getAirTime();

        if (airTime > MAX_LEGIT_AIR_TIME * 2) {
            double violationLevel = Math.min(100, (airTime - MAX_LEGIT_AIR_TIME * 2) * 0.5);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.FLY,
                    violationLevel,
                    String.format("Extended air time: %d ticks", airTime)
            );
        }

        if (airTime > 10 && !player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
            double deltaY = data.getLastDeltaY();
            if (deltaY > ASCENSION_THRESHOLD) {
                double violationLevel = (deltaY - ASCENSION_THRESHOLD) * 30;
                if (violationLevel > 20) {
                    return new ViolationRecord(
                            player.getUniqueId(),
                            CheatType.FLY,
                            violationLevel,
                            String.format("Upward movement while airborne: %.2f delta", deltaY)
                    );
                }
            }
        }

        return null;
    }
}