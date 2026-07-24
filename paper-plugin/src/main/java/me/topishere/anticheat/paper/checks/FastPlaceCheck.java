package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public class FastPlaceCheck extends Check {
    private static final long MIN_PLACE_TIME = 50;

    public FastPlaceCheck() {
        super("FastPlace");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        long timeSinceLastPlace = data.getTimeSinceLastBlockPlace();

        if (timeSinceLastPlace > 0 && timeSinceLastPlace < MIN_PLACE_TIME) {
            double violationLevel = Math.min(100, (MIN_PLACE_TIME - timeSinceLastPlace) * 2);
            return new ViolationRecord(
                    player.getUniqueId(),
                    CheatType.FASTPLACE,
                    violationLevel,
                    String.format("Fast place detected: %d ms between placements", timeSinceLastPlace)
            );
        }

        return null;
    }
}