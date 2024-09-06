package com.venned.simplecorepvp.build;

import org.bukkit.entity.Player;

import java.util.UUID;

public class CombatData {

    private final Player player;
    private long last_in_combat;
    private Player attacker;
    private final UUID uuid;

    public CombatData(Player player, long last_in_combat) {
        this.player = player;
        this.last_in_combat = last_in_combat;
        this.uuid = player.getUniqueId();
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setAttacker(Player attacker) {
        this.attacker = attacker;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getPlayer() {
        return player;
    }

    public long getLast_in_combat() {
        return last_in_combat;
    }

    public void setLast_in_combat(long last_in_combat) {
        this.last_in_combat = last_in_combat;
    }
}
