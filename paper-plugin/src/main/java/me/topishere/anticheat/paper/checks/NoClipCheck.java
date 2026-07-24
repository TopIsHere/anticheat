package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NoClipCheck extends Check {
    private int noClipCounter = 0;

    public NoClipCheck() {
        super("NoClip");
    }

    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        if (data.getCurrentLocation() == null) {
            return null;
        }

        Block playerBlock = data.getCurrentLocation().toLocation(player.getWorld()).getBlock();
        
        if (playerBlock.isSolid() && !player.isFlying()) {
            noClipCounter++;
            if (noClipCounter > 5) {
                double violationLevel = Math.min(100, noClipCounter * 10);
                return new ViolationRecord(
                        player.getUniqueId(),
                        CheatType.NOCLIP,
                        violationLevel,
                        String.format("Player inside solid block: %s", playerBlock.getType())
                );
            }
        } else {
            noClipCounter = Math.max(0, noClipCounter - 1);
        }

        return null;
    }
}