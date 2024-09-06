package com.venned.simplecorepvp.interfaces;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Races {

    String getRaceName();
    Material getMaterialMenu();
    String getRaceDescription();
    int getLevelRequirement();
    String getPermission();
    int getPriceGCoins();
}
