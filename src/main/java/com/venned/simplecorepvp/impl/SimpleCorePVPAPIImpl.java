package com.venned.simplecorepvp.impl;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.interfaces.SimpleCorePVPAPI;
import com.venned.simplecorepvp.utils.MapUtils;
import org.bukkit.entity.Player;

public class SimpleCorePVPAPIImpl implements SimpleCorePVPAPI {

    private final MapUtils mapUtils;
    private final Main plugin;

    public SimpleCorePVPAPIImpl(MapUtils mapUtils, Main plugin) {
        this.mapUtils = mapUtils;
        this.plugin = plugin;
    }

    @Override
    public PlayerData getPlayerData(Player player) {
        return mapUtils.getPlayerData(player);
    }

    @Override
    public Main getInstanceCore() {
        return plugin;
    }

}
