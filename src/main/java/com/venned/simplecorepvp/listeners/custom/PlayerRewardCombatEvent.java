package com.venned.simplecorepvp.listeners.custom;

import com.venned.simplecorepvp.build.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRewardCombatEvent extends Event implements Cancellable {

    private final PlayerData playerData;
    private final int GCoinsReward;
    private final int XPReward;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public PlayerRewardCombatEvent(PlayerData playerData, int GCoinsReward, int XPReward) {
        this.playerData = playerData;
        this.GCoinsReward = GCoinsReward;
        this.XPReward = XPReward;
        this.isCancelled = false;
    }

    public int getGCoinsReward() {
        return GCoinsReward;
    }

    public int getXPReward() {
        return XPReward;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}