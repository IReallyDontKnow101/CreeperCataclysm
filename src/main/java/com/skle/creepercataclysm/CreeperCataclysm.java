package com.skle.creepercataclysm;

import com.skle.creepercataclysm.api.CreeperCataclysmPlugin;
import com.skle.creepercataclysm.commands.*;
import com.skle.creepercataclysm.commands.debug.*;
import com.skle.creepercataclysm.listeners.*;
import com.skle.creepercataclysm.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

//TODO: config file, add shop items, custom enchants

public final class CreeperCataclysm extends JavaPlugin implements CreeperCataclysmPlugin {

    private FileConfiguration config = getConfig();

    private final GameManager gameManager = new GameManager(this);
    private final QueueManager queueManager = new QueueManager(this);
    private final GoldManager goldManager = new GoldManager(this);
    private final ShopManager shopManager = new ShopManager(this);
    private final ZoneManager zoneManager = new ZoneManager(this);

    private int MAX_PLAYERS = 2;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        // Register commands
        getCommand("play").setExecutor(new PlayCommand(this));
        getCommand("queue").setExecutor(new QueueCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("setplayers").setExecutor(new SetPlayers(this));
        getCommand("settime").setExecutor(new SetTime(this));
        getCommand("abort").setExecutor(new Abort(this));
        getCommand("forcestart").setExecutor(new ForceStart(  this));
        getCommand("addgold").setExecutor(new AddGold(  this));
        getCommand("zonelobby").setExecutor(new ZoneLobbyCommand(  this));
        getCommand("zonemap").setExecutor(new ZoneMapCommand(  this));
        getCommand("reloadconfig").setExecutor(new ReloadConfig(  this));
        getCommand("cancelzone").setExecutor(new CancelZoneCommand(  this));

        // Register listeners
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityInteractListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(this), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        if(gameManager.isGameStarted()) {
            gameManager.endGame(0);
        }
    }

    @Override
    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public QueueManager getQueueManager() {
        return queueManager;
    }

    @Override
    public GoldManager getGoldManager() { return goldManager;}

    @Override
    public ShopManager getShopManager() { return shopManager; }

    @Override
    public ZoneManager getZoneManager() { return zoneManager; }

    @Override
    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.MAX_PLAYERS = maxPlayers;
    }

    @Override
    public FileConfiguration getPluginConfig() {
        return config;
    }

    public void reloadPluginConfig() {
        saveConfig();
        reloadConfig();
        config = getConfig();
        this.getGameManager().initConfig();
        Bukkit.getLogger().info("Configs reloaded.");
    }

    public void reloadConfigFromDisk() {
        reloadConfig();
        config = getConfig();
        this.getGameManager().initConfig();
        Bukkit.getLogger().info("Configs reloaded from disk.");
    }
}