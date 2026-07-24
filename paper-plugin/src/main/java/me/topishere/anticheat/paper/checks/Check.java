package me.topishere.anticheat.paper.checks;

import me.topishere.anticheat.common.ViolationRecord;
import org.bukkit.entity.Player;

public abstract class Check {
    protected final String checkName;

    public Check(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckName() {
        return checkName;
    }

    public abstract ViolationRecord check(Player player, PlayerCheckData data);

    public boolean shouldCheck(Player player) {
        return !player.isOp() && player.getGameMode().isAlive();
    }
}