package me.topishere.anticheat.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import me.topishere.anticheat.common.ClientType;
import me.topishere.anticheat.velocity.AnticheatPlugin;

public class ConnectionListener {
    private final AnticheatPlugin plugin;

    public ConnectionListener(AnticheatPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        plugin.getLogger().info("Player attempting to join: " + event.getConnection().getCleanedAddress().toString());
    }

    @Subscribe
    public void onPlayerChooseServer(ServerPreConnectEvent event) {
        ClientType detected = plugin.getClientDetector()
                .detectClient(event.getPlayer(), "vanilla", "");

        if (detected.isSuspicious()) {
            plugin.getLogger().warn(String.format(
                    "SUSPICIOUS CLIENT DETECTED: %s is using %s",
                    event.getPlayer().getUsername(),
                    detected.getDisplayName()
            ));

            plugin.getProxy().getConsoleCommandSource()
                    .sendMessage("§c§l[ANTICHEAT] §r§c" + event.getPlayer().getUsername() + 
                            " joined with suspicious client: §e" + detected.getDisplayName());
        } else {
            plugin.getLogger().info(event.getPlayer().getUsername() + " using vanilla client");
        }
    }
}