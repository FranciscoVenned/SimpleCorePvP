package com.venned.simplecorepvp.build;

import java.util.UUID;

public class PlayerDeaths {

    private UUID uuid;
    private int kills;
    private long last_kill;

    public PlayerDeaths(UUID uuid, int kills, long last_kill) {
        this.uuid = uuid;
        this.kills = kills;
        this.last_kill = last_kill;
    }

    public long getLast_kill() {
        return last_kill;
    }

    public int getKills() {
        return kills;
    }

    public void incrementKills(long last_kill) {
        this.last_kill = last_kill;
        this.kills++;
    }

    public UUID getUUID() {
        return uuid;
    }
}
