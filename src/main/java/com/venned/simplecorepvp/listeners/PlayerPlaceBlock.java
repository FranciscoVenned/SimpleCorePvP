package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.Cuboid;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPlaceBlock implements Listener {

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Cuboid cuboid = Main.getPlugin(Main.class).getZonesUtils().getZoneSpawn();
        if(cuboid.isInCuboid(player.getLocation())) {
            if(!player.isOp()){
                event.setCancelled(true);
                return;
            }
        }
        if(event.getBlock().getType() == Material.COBBLESTONE) {
            Block block = event.getBlock();
            Main.getPlugin(Main.class).getMapUtils().blockPlaced.add(block);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.getPlugin(Main.class).getMapUtils().blockPlaced.remove(block);
                    block.setType(Material.AIR);
                }
            }.runTaskLater(Main.getPlugin(Main.class), Main.getPlugin(Main.class).getConfig().getInt("DELAY-BLOCK") * 20L); // 200L = 10 segundos (20 ticks = 1 segundo)
            return;
        }

        if(!player.isOp()) {
            event.setCancelled(true);
        }
    }

}
