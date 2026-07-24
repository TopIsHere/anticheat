package me.topishere.anticheat.paper.commands;

import me.topishere.anticheat.paper.AnticheatPlugin;
import me.topishere.anticheat.paper.storage.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnticheatCommand implements CommandExecutor {
    private final AnticheatPlugin plugin;

    public AnticheatCommand(AnticheatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("anticheat.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "reload":
                plugin.reloadConfig();
                sender.sendMessage("§a§l[ANTICHEAT] §r§aConfiguration reloaded!");
                break;

            case "ban":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /anticheat ban <player>");
                    return true;
                }
                handleBan(sender, args[1]);
                break;

            case "unban":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /anticheat unban <player>");
                    return true;
                }
                handleUnban(sender, args[1]);
                break;

            case "reset":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /anticheat reset <player>");
                    return true;
                }
                handleReset(sender, args[1]);
                break;

            case "stats":
                sendStats(sender);
                break;

            default:
                sender.sendMessage("§cUnknown subcommand. Use /anticheat for help.");
                break;
        }

        return true;
    }

    private void handleBan(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerDataManager.PlayerStats stats = plugin.getPlayerDataManager().getOrCreateStats(player.getUniqueId());
        stats.ban();
        player.kickPlayer("§c§lANTICHEAT §r§cYou have been banned for cheating.");
        
        sender.sendMessage("§a§l[ANTICHEAT] §r§a" + playerName + " has been banned.");
        plugin.getLogger().warning(sender.getName() + " banned " + playerName);
    }

    private void handleUnban(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        PlayerDataManager.PlayerStats stats = plugin.getPlayerDataManager().getOrCreateStats(player.getUniqueId());
        stats.unban();
        
        sender.sendMessage("§a§l[ANTICHEAT] §r§a" + playerName + " has been unbanned.");
        plugin.getLogger().info(sender.getName() + " unbanned " + playerName);
    }

    private void handleReset(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        plugin.getCheckManager().removePlayerData(player.getUniqueId());
        plugin.getViolationStorage().resetPlayerFlags(player.getUniqueId());
        
        sender.sendMessage("§a§l[ANTICHEAT] §r§a" + playerName + "'s violation data has been reset.");
    }

    private void sendStats(CommandSender sender) {
        sender.sendMessage("§a§l=== ANTICHEAT STATS ===");
        sender.sendMessage("§aSystem Status: §2ONLINE");
        sender.sendMessage("§aOnline Players: §2" + Bukkit.getOnlinePlayers().size());
        sender.sendMessage("§a§l====================");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§a§l=== ANTICHEAT COMMANDS ===");
        sender.sendMessage("§a/anticheat reload §7- Reload configuration");
        sender.sendMessage("§a/anticheat ban <player> §7- Ban a player");
        sender.sendMessage("§a/anticheat unban <player> §7- Unban a player");
        sender.sendMessage("§a/anticheat reset <player> §7- Reset violations");
        sender.sendMessage("§a/anticheat stats §7- Show stats");
        sender.sendMessage("§a/acheck <player> §7- Check a player");
        sender.sendMessage("§a§l==========================");
    }
}