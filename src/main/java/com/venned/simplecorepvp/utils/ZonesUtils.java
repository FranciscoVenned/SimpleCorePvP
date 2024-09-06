package com.venned.simplecorepvp.utils;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ZonesUtils {

    private final Main plugin;
    private Cuboid zoneSpawn;
    private Location spawn;


    public ZonesUtils(Main plugin) {
        this.plugin = plugin;
        setPortalConfig();
    }

    public void reloadZone(){
        setPortalConfig();
    }

    public void setPortalConfig(){

        String[] pos1 = plugin.getConfig().getString("lobby-data.location-positions.pos1").split(",");
        String[] pos2 = plugin.getConfig().getString("lobby-data.location-positions.pos2").split(",");
        Location loc1 = new Location(Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world")),
                Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]), Double.parseDouble(pos1[2]));
        Location loc2 = new Location(Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world")),
                Double.parseDouble(pos2[0]), Double.parseDouble(pos2[1]), Double.parseDouble(pos2[2]));
        World world = Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world"));

        String[] location_spawn = plugin.getConfig().getString("lobby-data.location").split(",");
        Location locationSpawn = new Location(world, Double.parseDouble(location_spawn[0]),
                Double.parseDouble(location_spawn[1]), Double.parseDouble(location_spawn[2]));

        spawn = new Location(world, locationSpawn.getX(), locationSpawn.getY(), locationSpawn.getZ());
        zoneSpawn = new Cuboid(loc1, loc2, world);
        Bukkit.getLogger().info("Lobby establecido ");
    }

    public Location getSpawn() {
        return spawn;
    }

    public Cuboid getZoneSpawn() {
        return zoneSpawn;
    }
}

