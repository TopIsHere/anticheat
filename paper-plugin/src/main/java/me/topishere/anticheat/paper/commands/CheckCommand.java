package me.topishere.anticheat.paper.commands;

import me.topishere.anticheat.common.CheatType;
import me.topishere.anticheat.paper.AnticheatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class CheckCommand implements CommandExecutor {
    private final AnticheatPlugin plugin;

    public CheckCommand(AnticheatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.check")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /acheck <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        UUID uuid = target.getUniqueId();
        Map<CheatType, Integer> violations = plugin.getCheckManager().getPlayerViolations(uuid);
        double suspicionLevel = plugin.getCheckManager().getPlayerSuspicionLevel(uuid);

        sender.sendMessage("§a§l=== ANTICHEAT CHECK: " + target.getName() + " ===");
        sender.sendMessage("§aUUID: §2" + uuid);
        sender.sendMessage("§aSuspicion Level: §2" + String.format("%.1f%%", suspicionLevel));
        sender.sendMessage("§aViolation Flags: §2" + plugin.getViolationStorage().getPlayerViolationFlags(uuid));
        sender.sendMessage("§a");
        sender.sendMessage("§a§lVIOLATIONS:");

        boolean hasViolations = false;
        for (CheatType type : CheatType.values()) {
            int count = violations.getOrDefault(type, 0);
            if (count > 0) {
                hasViolations = true;
                sender.sendMessage("§a  " + type.getDisplayName() + ": §c" + count);
            }
        }

        if (!hasViolations) {
            sender.sendMessage("§a  No violations detected!");
        }

        sender.sendMessage("§a§l=======================");

        return true;
    }
}