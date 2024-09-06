package com.venned.simplecorepvp.hook;

import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.utils.MapUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SimpleCorePVPPlaceholders extends PlaceholderExpansion {

    private final MapUtils mapUtils;

    public SimpleCorePVPPlaceholders(MapUtils mapUtils) {
        this.mapUtils = mapUtils;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplecorepvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Venned";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        PlayerData playerData = mapUtils.getPlayerData(player);
        if (playerData == null) {
            return "N/A";
        }

        switch (params.toLowerCase()) {
            case "combatlog":
                return mapUtils.getTimeRemainingInCombat(player);
            case "coins":
                return String.valueOf(playerData.getGCoins());
            case "deaths":
                return String.valueOf(playerData.getDeaths());
            case "kills":
                return String.valueOf(playerData.getKills());
            case "level":
                return String.valueOf(playerData.getLevel());
            case "prestige":
                return String.valueOf(playerData.getPrestige());
            case "experience":
                return String.valueOf(playerData.getExperience());
            case "experience_required":
                return String.valueOf(playerData.getExperience_required());
            case "race":
                return (playerData.getRace() != null) ? playerData.getRace().getRaceName() : "Ninguna";
            case "gapples":
                return String.valueOf(playerData.getApple_consumed());
            default:
                return "Unknown placeholder";
        }
    }
}
