package me.topishere.anticheat.paper;

import me.topishere.anticheat.paper.checks.*;
import me.topishere.anticheat.paper.commands.AnticheatCommand;
import me.topishere.anticheat.paper.commands.CheckCommand;
import me.topishere.anticheat.paper.listeners.PlayerListener;
import me.topishere.anticheat.paper.storage.ViolationStorage;
import me.topishere.anticheat.paper.storage.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AnticheatPlugin extends JavaPlugin {

    private static AnticheatPlugin instance;
    private ViolationStorage violationStorage;
    private PlayerDataManager playerDataManager;
    private CheckManager checkManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        violationStorage = new ViolationStorage();
        playerDataManager = new PlayerDataManager();
        checkManager = new CheckManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getCommand("anticheat").setExecutor(new AnticheatCommand(this));
        getCommand("acheck").setExecutor(new CheckCommand(this));

        getServer().getScheduler().runTaskTimerAsynchronously(this, 
                () -> violationStorage.processPendingViolations(this), 20L, 20L);

        getLogger().info("TopIsHereAnticheat enabled! Ready to detect cheaters.");
    }

    @Override
    public void onDisable() {
        violationStorage.saveAllData();
        playerDataManager.saveAllData();
        getLogger().info("TopIsHereAnticheat disabled!");
    }

    public static AnticheatPlugin getInstance() {
        return instance;
    }

    public ViolationStorage getViolationStorage() {
        return violationStorage;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }
}