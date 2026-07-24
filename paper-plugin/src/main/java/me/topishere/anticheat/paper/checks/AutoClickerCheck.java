package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class AutoClickerCheck extends Check {
    private static final double MAX_LEGITIMATE_CPS = 20.0;
    private static final double EXTREME_CPS_THRESHOLD = 30.0;
    private static final double HIGH_CPS_THRESHOLD = 25.0;

    public AutoClickerCheck() {
        super("AutoClicker");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        double cps = data.getAttacksPerSecond();

        if (cps > EXTREME_CPS_THRESHOLD) {
            double violationLevel = Math.min(100, cps - EXTREME_CPS_THRESHOLD);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.AUTOCLICKER,
                    violationLevel,
                    String.format("Extreme CPS detected: %.1f CPS", cps)
            );
        }

        if (cps > HIGH_CPS_THRESHOLD) {
            double violationLevel = (cps - HIGH_CPS_THRESHOLD) * 5;
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.AUTOCLICKER,
                    violationLevel,
                    String.format("High CPS detected: %.1f CPS", cps)
            );
        }

        if (cps > MAX_LEGITIMATE_CPS && cps > 18.0) {
            double violationLevel = (cps - MAX_LEGITIMATE_CPS) * 3;
            if (violationLevel > 10) {
                return new ViolationRecord(
                        player.getUniqueId(),
                        CheatType.AUTOCLICKER,
                        violationLevel,
                        String.format("Suspicious CPS: %.1f CPS", cps)
                );
            }
        }

        return null;
    }
}