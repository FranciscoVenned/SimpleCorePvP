package com.venned.simplecorepvp.build;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerStreak {

    private List<ItemStack> list_items;
    private final Player player;
    private int kills;

    public PlayerStreak(Player player, List<ItemStack> list_items, int kills) {
        this.player = player;
        this.list_items = list_items;
        this.kills = kills;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setList_items(List<ItemStack> list_items) {
        this.list_items = list_items;
    }

    public List<ItemStack> getList_items() {
        return list_items;
    }

    public void addItems(ItemStack item) {
        list_items.add(item);
    }

    public void incrementKills(){
        this.kills++;
    }
}
