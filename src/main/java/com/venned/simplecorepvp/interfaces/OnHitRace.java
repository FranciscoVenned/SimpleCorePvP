package com.venned.simplecorepvp.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface OnHitRace {

    void OnHit(EntityDamageByEntityEvent event, Player player);
}
