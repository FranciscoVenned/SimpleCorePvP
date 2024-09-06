package com.venned.simplecorepvp.utils.runnable;

import com.venned.simplecorepvp.build.CombatData;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.utils.MapUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatLogTask extends BukkitRunnable {

    private final MapUtils mapUtils;
    private final long combatDurationMillis;


    public CombatLogTask(MapUtils mapUtils, Plugin plugin) {
        this.mapUtils = mapUtils;
        this.combatDurationMillis = plugin.getConfig().getInt("time-combat") * 60000L;  // Convertimos de minutos a milisegundos
    }

    @Override
    public void run() {
            for(CombatData combatData : mapUtils.combatData){
                Player player = combatData.getPlayer();
                long lastCombatInteract = combatData.getLast_in_combat();
                long timeSinceLastCombat = System.currentTimeMillis() - lastCombatInteract;

                if (timeSinceLastCombat < combatDurationMillis) {
                    long timeRemainingMillis = combatDurationMillis - timeSinceLastCombat;

                    long minutesRemaining = (timeRemainingMillis / 1000) / 60;
                    long secondsRemaining = (timeRemainingMillis / 1000) % 60;

                    /*
                    String message = "Tiempo restante en combate: " + minutesRemaining + " min " + secondsRemaining + " s.";
                    BaseComponent component = new TextComponent(message);

                    player.sendMessage("tiempo restante " + minutesRemaining);
                    player.spigot().sendMessage(component);

                     */
                }
            }

    }
}
