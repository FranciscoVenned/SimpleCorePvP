package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.CombatData;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.build.PlayerDeaths;
import com.venned.simplecorepvp.interfaces.Races;
import com.venned.simplecorepvp.races.Vampiro;
import com.venned.simplecorepvp.utils.ItemsJoin;
import com.venned.simplecorepvp.utils.LevelUtils;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.data.MongoConnection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static org.bukkit.Bukkit.getLogger;

public class PlayerEvents implements Listener {

    private final MapUtils mapUtils;
    private final MongoConnection mongoConnection;
    private final LevelUtils levelUtils;

    private static final long TWO_MINUTES_IN_MILLIS = 2 * 60 * 1000;

    public PlayerEvents(MapUtils mapUtils, MongoConnection mongoConnection, LevelUtils levelUtils) {
        this.mapUtils = mapUtils;
        this.mongoConnection = mongoConnection;
        this.levelUtils = levelUtils;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemsJoin.playerGiveItems(player);
        if (!mapUtils.playerDataMap.containsKey(uuid)) {
            Document doc = mongoConnection.getCollection().find(eq("uuid", uuid.toString())).first();
            if (doc == null) {
                mapUtils.playerDataMap.put(uuid, new PlayerData(uuid, 0, 0, 0, levelUtils.experienceNextLevel(1)
                , 1, 0, 0, 0, 0, 0, null, 0, System.currentTimeMillis(), new ArrayList<>()));
                getLogger().info("Player data created for: " + player.getName());
            } else {
                int kills = doc.getInteger("kills");
                int deaths = doc.getInteger("deaths");
                int experience = doc.getInteger("experience");
                int experience_required = doc.getInteger("experience_required");
                int level = doc.getInteger("level");
                int GCoins = doc.getInteger("GCoins");
                int remainder = doc.getInteger("remainder");
                int assist = doc.getInteger("assist");
                int apple_consumed = doc.getInteger("apple_consumed");
                int time_played = doc.getInteger("time_played");
                mapUtils.playerDataMap.put(uuid, new PlayerData(uuid, kills, deaths, experience, experience_required, level,
                        GCoins, remainder, assist, apple_consumed, time_played, null, 0, System.currentTimeMillis(), new ArrayList<>()));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            UUID victimUUID = victim.getUniqueId();
            UUID attackerUUID = attacker.getUniqueId();

            mapUtils.attackerMap.putIfAbsent(victimUUID, new HashSet<>());
            mapUtils.attackerMap.get(victimUUID).add(attackerUUID);

            mapUtils.damageMap.putIfAbsent(victimUUID, new HashMap<>());
            Map<UUID, Double> damageContribution = mapUtils.damageMap.get(victimUUID);
            damageContribution.put(attackerUUID, damageContribution.getOrDefault(attackerUUID, 0.0) + event.getDamage());
        }
    }

    @EventHandler
    public void OnPlayerDeathItems(PlayerRespawnEvent event){
        ItemsJoin.playerGiveItems(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID victimUUID = victim.getUniqueId();

        event.setDeathMessage(null);

        PlayerData victimData = mapUtils.playerDataMap.get(victimUUID);
        if (victimData != null) {
            victimData.incrementDeaths();
        }

        Map<UUID, Double> damageContribution = mapUtils.damageMap.get(victimUUID);
        if (damageContribution == null || damageContribution.isEmpty()) return;

        double totalDamage = damageContribution.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<UUID, Double> entry : damageContribution.entrySet()) {
            UUID attackerUUID = entry.getKey();
            double contribution = entry.getValue();
            Player attacker = Bukkit.getPlayer(attackerUUID);

            if (attacker == null || !attacker.isOnline()) continue;

            PlayerData attackerData = mapUtils.playerDataMap.get(attackerUUID);
            if (attackerData != null) {
                List<PlayerDeaths> deathsList = mapUtils.listPlayerKills.getOrDefault(attackerUUID, new ArrayList<>());
                Optional<PlayerDeaths> existingDeath = deathsList.stream()
                        .filter(pd -> pd.getUUID().equals(victimUUID))
                        .findFirst();

                if (existingDeath.isPresent()) {
                    PlayerDeaths pd = existingDeath.get();
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - pd.getLast_kill() > TWO_MINUTES_IN_MILLIS) {
                        deathsList.remove(pd); // Remove the record if 2 minutes have passed
                        mapUtils.listPlayerKills.put(attackerUUID, deathsList); // Update the list
                        deathsList.add(new PlayerDeaths(victimUUID, 1, currentTime)); // Add new record
                    } else {
                        pd.incrementKills(System.currentTimeMillis());
                        if (pd.getKills() > 3) {
                            attacker.sendMessage("No recibes experiencia ni GCoins por matar al mismo jugador más de 3 veces.");
                            continue;
                        }
                    }
                } else {
                    deathsList.add(new PlayerDeaths(victimUUID, 1, System.currentTimeMillis()));
                    mapUtils.listPlayerKills.put(attackerUUID, deathsList);
                }

                levelUtils.giveRewards(attackerData, contribution, totalDamage, attacker, victim);
                attackerData.incrementKills();
            }
        }
        mapUtils.attackerMap.remove(victimUUID);
        mapUtils.damageMap.remove(victimUUID);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

        CombatData combatData = mapUtils.getCombatData(event.getPlayer());
        if(combatData != null){
            if(combatData.getAttacker() != null){
                CombatData attackerCombat = mapUtils.getCombatData(combatData.getAttacker());
                if(attackerCombat != null){
                    attackerCombat.setLast_in_combat(0);
                    attackerCombat.setAttacker(null);
                }
            }
        }

        PlayerData victimData = mapUtils.playerDataMap.get(event.getPlayer().getUniqueId());
        if (victimData != null) {
            Map<UUID, Double> damageContribution = mapUtils.damageMap.get(event.getPlayer().getUniqueId());
            if (damageContribution == null || damageContribution.isEmpty()) return;
            double totalDamage = damageContribution.values().stream().mapToDouble(Double::doubleValue).sum();
            for (Map.Entry<UUID, Double> entry : damageContribution.entrySet()) {
                UUID attackerUUID = entry.getKey();
                double contribution = entry.getValue();
                Player attacker = Bukkit.getPlayer(attackerUUID);

                if (attacker == null || !attacker.isOnline()) continue;

                PlayerData attackerData = mapUtils.playerDataMap.get(attackerUUID);
                if (attackerData != null) {
                    List<PlayerDeaths> deathsList = mapUtils.listPlayerKills.getOrDefault(attackerUUID, new ArrayList<>());
                    Optional<PlayerDeaths> existingDeath = deathsList.stream()
                            .filter(pd -> pd.getUUID().equals(event.getPlayer().getUniqueId()))
                            .findFirst();

                    if (existingDeath.isPresent()) {
                        PlayerDeaths pd = existingDeath.get();
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - pd.getLast_kill() > TWO_MINUTES_IN_MILLIS) {
                            deathsList.remove(pd); // Remove the record if 2 minutes have passed
                            mapUtils.listPlayerKills.put(attackerUUID, deathsList); // Update the list
                            deathsList.add(new PlayerDeaths(event.getPlayer().getUniqueId(), 1, currentTime)); // Add new record
                        } else {
                            pd.incrementKills(System.currentTimeMillis());
                            if (pd.getKills() > 3) {
                                attacker.sendMessage("No recibes experiencia ni GCoins por matar al mismo jugador más de 3 veces.");
                                continue;
                            }
                        }
                    } else {
                        deathsList.add(new PlayerDeaths(event.getPlayer().getUniqueId(), 1, System.currentTimeMillis()));
                        mapUtils.listPlayerKills.put(attackerUUID, deathsList);
                    }

                    levelUtils.giveRewards(attackerData, contribution, totalDamage, attacker, event.getPlayer());
                    attackerData.incrementKills();
                }
            }
        }


        mapUtils.attackerMap.remove(event.getPlayer().getUniqueId());
        mapUtils.damageMap.remove(event.getPlayer().getUniqueId());

    }

}
