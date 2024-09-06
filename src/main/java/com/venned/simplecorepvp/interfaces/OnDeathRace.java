package com.venned.simplecorepvp.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public interface OnDeathRace {
    void OnDeath(PlayerDeathEvent event, Player killer);
}
