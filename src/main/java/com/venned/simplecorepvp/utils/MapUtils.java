package com.venned.simplecorepvp.utils;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.CombatData;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.build.PlayerDeaths;
import com.venned.simplecorepvp.interfaces.Races;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class MapUtils {
    public HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
    public Map<UUID, Set<UUID>> attackerMap = new HashMap<>();
    public Set<Races> races = new HashSet<>();
    public Set<CombatData> combatData = new HashSet<>();
    public Map<UUID, List<PlayerDeaths>> listPlayerKills = new HashMap<>();
    public Map<UUID, Map<UUID, Double>> damageMap = new HashMap<>();
    public List<Block> blockPlaced = new ArrayList<>();

    public CombatData getCombat(Player player){
        Optional<CombatData> combat_data = combatData.stream().findFirst().filter(p -> (
                p.getPlayer().equals(player)
        ));
        return combat_data.orElse(null);
    }

    public CombatData getCombatData(Player player) {
        CombatData existingCombatData = combatData.stream()
                .filter(cd -> cd.getPlayer().equals(player))
                .findFirst()
                .orElse(null);

        if (existingCombatData != null) {
            return existingCombatData;
        }


        CombatData newCombatData = new CombatData(player, 0);
        combatData.add(newCombatData);
        return newCombatData;
    }

    public String getTimeRemainingInCombat(OfflinePlayer player) {
        CombatData combatData = getCombatData(player.getPlayer());
        if (combatData == null  || combatData.getAttacker() == null) {
            return "Fuera de combate";
        }

        long lastCombatInteract = combatData.getLast_in_combat();
        long timeSinceLastCombat = System.currentTimeMillis() - lastCombatInteract;
        long combatDurationMillis = 60000L * Main.getPlugin(Main.class).getConfig().getInt("time-combat");

        if (timeSinceLastCombat >= combatDurationMillis) {
            combatData.setAttacker(null);
            return "Fuera de combate";
        }

        long timeRemainingMillis = combatDurationMillis - timeSinceLastCombat;
        long minutesRemaining = (timeRemainingMillis / 1000) / 60;
        long secondsRemaining = (timeRemainingMillis / 1000) % 60;

        return String.format("%d min %d s", minutesRemaining, secondsRemaining);
    }

    public Races getRace(String name){
        return races.stream()
                .filter(r -> r.getRaceName().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);
    }


    public PlayerData getPlayerData(OfflinePlayer player) {
        PlayerData playerData = playerDataMap.get(player.getUniqueId());
        if (playerData != null) {
            return playerData;
        } else {
            return null;
        }
    }
}
