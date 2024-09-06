package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.utils.MapUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerAppleEvent implements Listener {

    private final MapUtils mapUtils;

    public PlayerAppleEvent(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        PlayerData playerData = mapUtils.playerDataMap.get(player.getUniqueId());
        if(playerData != null) {
            if (item.getType() == Material.GOLDEN_APPLE) {
                if (item.getDurability() == 0) {
                    playerData.incrementGApple();
                } else if (item.getDurability() == 1) {
                    playerData.incrementGApple();
                }
            }
        }
    }
}
