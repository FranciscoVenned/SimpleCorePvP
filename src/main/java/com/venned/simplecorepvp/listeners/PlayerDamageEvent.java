package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.CombatData;
import com.venned.simplecorepvp.build.Cuboid;
import com.venned.simplecorepvp.build.PlayerDeaths;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerDamageEvent implements Listener {

    private final MapUtils mapUtils;

    public PlayerDamageEvent(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(event.getMessage().startsWith("save")){
            event.setCancelled(true);
            event.getPlayer().performCommand("simplekits save");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Cuboid cuboid = Main.getPlugin(Main.class).getZonesUtils().getZoneSpawn();
        if(cuboid.isInCuboid(e.getDamager().getLocation())) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Player) {
                Player victim = (Player) e.getEntity();
                Player damager = (Player) e.getDamager();

                CombatData victimCombatData = mapUtils.getCombatData(victim);
                if(victimCombatData.getAttacker() == null){
                    MessageUtils.sendMessage(victim, MessageUtils.MessageType.COMBAT_JOIN, "%player%", damager.getName());
                }
                victimCombatData.setAttacker(damager);
                victimCombatData.setLast_in_combat(System.currentTimeMillis());
                CombatData damagerCombatData = mapUtils.getCombatData(damager);
                if(damagerCombatData.getAttacker() == null){
                    MessageUtils.sendMessage(damager, MessageUtils.MessageType.COMBAT_JOIN, "%player%", victim.getName());
                }
                damagerCombatData.setAttacker(victim);
                damagerCombatData.setLast_in_combat(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player victim = event.getEntity();
        if(victim.getKiller() != null) {
            Player assasin = victim.getKiller();
            CombatData victimCombatData = mapUtils.getCombatData(victim);
            victimCombatData.setAttacker(null);
            victimCombatData.setLast_in_combat(0);
            CombatData damagerCombatData = mapUtils.getCombatData(assasin);
            damagerCombatData.setAttacker(null);
            damagerCombatData.setLast_in_combat(0);

            MessageUtils.sendMessage(victim, MessageUtils.MessageType.COMBAT_LEAVE);
            MessageUtils.sendMessage(assasin, MessageUtils.MessageType.COMBAT_LEAVE);
        } else {
            MessageUtils.sendMessage(victim, MessageUtils.MessageType.COMBAT_LEAVE);
            CombatData victimCombatData = mapUtils.getCombatData(victim);
            victimCombatData.setAttacker(null);
            victimCombatData.setLast_in_combat(0);
        }
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        CombatData combatData = mapUtils.getCombatData(player);
        if(combatData != null) {
            if(combatData.getAttacker() != null){
                String command = event.getMessage();
                List<String> list_commands = Main.getPlugin(Main.class).getConfig().getStringList("list-commands-block");
                boolean command_in_present = list_commands.stream().anyMatch(command::equalsIgnoreCase);
                if(command_in_present) {
                    MessageUtils.sendMessage(player, MessageUtils.MessageType.NO_COMMAND_COMBAT);
                    event.setCancelled(true);
                }
            }
        }
    }

}
