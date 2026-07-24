package me.topishere.anticheat.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.topishere.anticheat.velocity.listeners.ConnectionListener;
import me.topishere.anticheat.velocity.detection.ClientDetector;
import org.slf4j.Logger;

import javax.inject.Inject;

@Plugin(
    id = "anticheat-velocity",
    name = "TopIsHereAnticheat-Velocity",
    version = "1.0.0",
    description = "Advanced anticheat system for Velocity",
    authors = {"TopIsHere"}
)
public class AnticheatPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private static AnticheatPlugin instance;
    private ClientDetector clientDetector;

    @Inject
    public AnticheatPlugin(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        clientDetector = new ClientDetector();
        proxy.getEventManager().register(this, new ConnectionListener(this));
        logger.info("TopIsHereAnticheat-Velocity enabled!");
    }

    public static AnticheatPlugin getInstance() {
        return instance;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Logger getLogger() {
        return logger;
    }

    public ClientDetector getClientDetector() {
        return clientDetector;
    }
}