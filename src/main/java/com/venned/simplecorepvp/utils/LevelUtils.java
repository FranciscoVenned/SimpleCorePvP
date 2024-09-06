package com.venned.simplecorepvp.utils;

import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.listeners.custom.PlayerRewardCombatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LevelUtils {

    Plugin plugin;

    public LevelUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public int experienceNextLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be 1 or higher.");
        }
        int experienceBase = plugin.getConfig().getInt("experience_base");
        double multiExperienceRequired= plugin.getConfig().getDouble("multi_experience_required");
        return (int) Math.round(experienceBase * Math.pow(multiExperienceRequired, level - 1));
    }

    public int gCoinsPerKill(){
        return plugin.getConfig().getInt("gcoins-per-kill");
    }

    public int experiencePerKill() {
        return plugin.getConfig().getInt("experience-per-kill");
    }

    public int getContributionKillSuperior() {
        return plugin.getConfig().getInt("contribution-kill-50-superior");
    }

    public int getContributionKillBelow() {
        return plugin.getConfig().getInt("contribution-kill-50-below");
    }

    private double getBoosterMultiplier(Player player) {
        for (org.bukkit.permissions.PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String permission = permInfo.getPermission();
            if (permission.startsWith("corepvp.booster.")) {
                try {
                    return Double.parseDouble(permission.substring("corepvp.booster.".length()));
                } catch (NumberFormatException e) {
                    // Ignore invalid boosters
                }
            }
        }
        return 1.0; // No booster found
    }

    public void giveRewards(PlayerData attackerData, double contribution, double totalDamage, Player attacker, Player victim) {
        double contributionPercentage = (contribution / totalDamage) * 100;


        int experienceReward;
        int gCoinsReward;
        if (contributionPercentage >= 100) {
            // Full base rewards if the player did 100% of the damage
            experienceReward = experiencePerKill();
            gCoinsReward = gCoinsPerKill();
        } else if (contributionPercentage >= 50) {
            // Rewards based on superior contribution percentage
            experienceReward = (int) (experiencePerKill() * (getContributionKillSuperior() / 100.0));
            gCoinsReward = (int) (gCoinsPerKill() * (getContributionKillSuperior() / 100.0));
        } else {
            // Rewards based on below contribution percentage
            experienceReward = (int) (experiencePerKill() * (getContributionKillBelow() / 100.0));
            gCoinsReward = (int) (gCoinsPerKill() * (getContributionKillBelow() / 100.0));
        }

        double boosterMultiplier = getBoosterMultiplier(attacker);
        experienceReward *= (int) boosterMultiplier;
        gCoinsReward *= (int) boosterMultiplier;

        attackerData.incrementExperience(experienceReward);
        attackerData.incrementGCoins(gCoinsReward);

        if (attackerData.getExperience() >= attackerData.getExperience_required()) {
            attackerData.incrementLevel();
            attackerData.setExperience(0);
            attackerData.setExperience_required(experienceNextLevel(attackerData.getLevel()));
        }
        attacker.sendMessage("§6§l>> §fHas constribuido §e" + contributionPercentage + "§f de daño a §a" + victim.getName() + " §fy recibiste §e" + gCoinsReward  + " §fGCoins. §7(§fXP " + experienceReward + "§7).");

        PlayerRewardCombatEvent rewardEvent = new PlayerRewardCombatEvent(attackerData, gCoinsReward, experienceReward);
        Bukkit.getPluginManager().callEvent(rewardEvent);
        attacker.playSound(attacker.getLocation(), Sound.ORB_PICKUP, 1, 1);

    }

    public void incrementXP(PlayerData playerData, int experienceReward) {
       playerData.incrementExperience(experienceReward);
        if (playerData.getExperience() >= playerData.getExperience_required()) {
            playerData.incrementLevel();
            playerData.setExperience(0);
            playerData.setExperience_required(experienceNextLevel(playerData.getLevel()));
        }
    }
}
