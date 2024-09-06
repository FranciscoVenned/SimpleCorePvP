package com.venned.simplecorepvp;

import com.venned.simplecorepvp.commands.MainCommand;
import com.venned.simplecorepvp.hook.SimpleCorePVPPlaceholders;
import com.venned.simplecorepvp.impl.SimpleCorePVPAPIImpl;
import com.venned.simplecorepvp.interfaces.SimpleCorePVPAPI;
import com.venned.simplecorepvp.listeners.*;
import com.venned.simplecorepvp.manager.LoadRace;
import com.venned.simplecorepvp.utils.LevelUtils;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.ZonesUtils;
import com.venned.simplecorepvp.utils.data.MongoConnection;
import com.venned.simplecorepvp.utils.data.MongoManager;
import com.venned.simplecorepvp.utils.runnable.CombatLogTask;
import com.venned.simplecorepvp.utils.runnable.PlayerMoveTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin{

    MongoConnection mongoConnection;
    MongoManager mongoManager;
    MapUtils mapUtils;
    LevelUtils levelUtils;
    LoadRace loadRace;
    ZonesUtils zonesUtils;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String host_name = getConfig().getString("host_name", "localhost");
        int port = getConfig().getInt("port_host", 27017);
        String data_base_name = getConfig().getString("data_base_name", "minecraft");
        String username = getConfig().getString("db_username", "yourUsername");
        String password = getConfig().getString("db_password", "yourPassword");
        mongoConnection = new MongoConnection(host_name, port, data_base_name, username, password);
        zonesUtils = new ZonesUtils(this);
        mapUtils = new MapUtils();
        loadRace = new LoadRace(mapUtils, this);
        mongoManager = new MongoManager(mapUtils, mongoConnection);
        mongoManager.loadPlayerData();

        levelUtils = new LevelUtils(this);


        registerListeners();
        startPlayerMoveTask();
        startCombatTask();
        registerCommands();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SimpleCorePVPPlaceholders(mapUtils).register();
        }
        // register api
        getServer().getServicesManager().register(SimpleCorePVPAPI.class, new SimpleCorePVPAPIImpl(mapUtils, this), this, org.bukkit.plugin.ServicePriority.Normal);

    }

    @Override
    public void onDisable() {
        mongoManager.savePlayerData();
        mongoConnection.getMongoClient().close();
        for(Block block : mapUtils.blockPlaced){
            block.setType(Material.AIR);
        }
    }

    public void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerPlaceBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageEvent(mapUtils), this);
        getServer().getPluginManager().registerEvents(new PlayerAppleEvent(mapUtils), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(mapUtils, mongoConnection, levelUtils), this);
        getServer().getPluginManager().registerEvents(new PlayerKillStreakEvent(this), this);
        getServer().getPluginManager().registerEvents(new RaceListeners(mapUtils), this);
    }

    public void startPlayerMoveTask() {
        new PlayerMoveTask(mapUtils, this).runTaskTimer(this, 20L, 20L); // Ejecuta cada minuto
    }

    public ZonesUtils getZonesUtils() {
        return zonesUtils;
    }

    public void startCombatTask(){
        new CombatLogTask(mapUtils, this).runTaskTimer(this, 20L, 20L * 2);
    }

    public LevelUtils getLevelUtils() {
        return levelUtils;
    }

    public MapUtils getMapUtils() {
        return mapUtils;
    }

    public void registerCommands(){
        getCommand("core").setExecutor(new MainCommand(mapUtils, this));
    }

}
