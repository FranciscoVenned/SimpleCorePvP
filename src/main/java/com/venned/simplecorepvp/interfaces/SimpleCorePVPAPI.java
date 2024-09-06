package com.venned.simplecorepvp.interfaces;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.PlayerData;
import org.bukkit.entity.Player;

public interface SimpleCorePVPAPI {

    /**
     * Obtiene los datos del jugador.
     *
     * @param player El jugador.
     * @return Los datos del jugador.
     */
    PlayerData getPlayerData(Player player);

    /**
     * Obtiene EL PLUGIN
     *
     * @return Los datos del jugador.
     */
    Main getInstanceCore();




}
