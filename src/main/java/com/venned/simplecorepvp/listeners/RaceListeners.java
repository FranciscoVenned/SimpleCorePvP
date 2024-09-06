package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.build.PlayerDeaths;
import com.venned.simplecorepvp.interfaces.OnDeathRace;
import com.venned.simplecorepvp.interfaces.OnHitRace;
import com.venned.simplecorepvp.interfaces.OnInteractRace;
import com.venned.simplecorepvp.interfaces.Races;
import com.venned.simplecorepvp.utils.MapUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RaceListeners implements Listener {

    private final MapUtils mapUtils;


    public RaceListeners(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    @EventHandler
    public void OnDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            PlayerData playerData = mapUtils.getPlayerData((OfflinePlayer) event.getDamager());
            if (playerData != null) {
                Player player = (Player) event.getDamager();
                Races races = playerData.getRace();
                if (races instanceof OnHitRace) {
                    ((OnHitRace) races).OnHit(event, player);
                }

            }
        }
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent event){
        if(event.getEntity().getKiller() instanceof Player) {
            Player player = (Player) event.getEntity().getKiller();
            PlayerData playerData = mapUtils.getPlayerData(player);
            if(playerData != null) {
                Races races = playerData.getRace();
                if(races instanceof OnDeathRace) {
                    ((OnDeathRace) races).OnDeath(event, player);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = mapUtils.getPlayerData(player);
        if(playerData != null) {
            Races races = playerData.getRace();
               if(races instanceof OnInteractRace) {
                   ((OnInteractRace) races).OnInteractRace(event);
               }
        }

    }

}
