package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class FastBreakCheck extends Check {
    private static final long MIN_BREAK_TIME = 100;

    public FastBreakCheck() {
        super("FastBreak");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        long timeSinceLastBreak = data.getTimeSinceLastBlockBreak();

        if (timeSinceLastBreak > 0 && timeSinceLastBreak < MIN_BREAK_TIME) {
            double violationLevel = Math.min(100, (MIN_BREAK_TIME - timeSinceLastBreak) * 1.5);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.FASTBREAK,
                    violationLevel,
                    String.format("Fast break detected: %d ms between breaks", timeSinceLastBreak)
            );
        }

        return null;
    }
}