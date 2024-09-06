package com.venned.simplecorepvp.utils.runnable;

import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerMoveTask extends BukkitRunnable {

    private final MapUtils mapUtils;
    private final long timeAfk; // Tiempo AFK antes de ser kickeado en milisegundos
    private final long warningTime; // Tiempo antes del kick para advertir al jugador

    public PlayerMoveTask(MapUtils mapUtils, Plugin plugin) {
        this.mapUtils = mapUtils;
        this.timeAfk = plugin.getConfig().getInt("time_afk", 60) * 1000L;
        this.warningTime = timeAfk - plugin.getConfig().getInt("time_warning", 10) * 1000L;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            player.setFoodLevel(20); // Llenar la barra de comida

            PlayerData playerData = mapUtils.getPlayerData(player);
            if (playerData == null) continue;

            long currentTime = System.currentTimeMillis();
            long lastMovementTime = playerData.getLast_movement();

            Location currentLocation = player.getLocation();
            Location previousLocation = playerData.getLast_location();

            if (!currentLocation.equals(previousLocation)) {
                playerData.setLast_location(currentLocation);
                playerData.setLast_movement(currentTime);
                continue;
            }

            long timeRemainingMillis = timeAfk - (currentTime - lastMovementTime);
            long secondsRemaining = (timeRemainingMillis / 1000);


            if (currentTime - lastMovementTime > warningTime && currentTime - lastMovementTime <= timeAfk) {
                MessageUtils.sendMessage(player, MessageUtils.MessageType.TIME_WARNING, "%time_restante%", String.valueOf(secondsRemaining));
            }

            // Verificar si el jugador debería ser kickeado
            if (currentTime - lastMovementTime > timeAfk) {
                playerData.setLast_location(currentLocation);
                playerData.setLast_movement(currentTime);
                MessageUtils.sendMessage(player, MessageUtils.MessageType.TIME_AFK);
                player.performCommand("hub");
            }
        }
    }
}
