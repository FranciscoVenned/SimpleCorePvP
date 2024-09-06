package com.venned.simplecorepvp.abstracts;

import com.venned.simplecorepvp.interfaces.Races;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractRace implements Races {

    private final String race_name;
    protected String material_name;
    protected String raceDescription;
    protected int level_requirement;
    protected String permission;
    protected int priceGCoins;

    public AbstractRace(String race_name) {
        this.race_name = race_name;
    }

    @Override
    public String getRaceName() {
        return race_name;
    }

    @Override
    public Material getMaterialMenu() {
        return Material.valueOf(material_name);
    }

    @Override
    public String getRaceDescription() {
        return raceDescription;
    }

    @Override
    public int getLevelRequirement() {
        return level_requirement;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public int getPriceGCoins() {
        return priceGCoins;
    }
}
