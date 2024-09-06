package com.venned.simplecorepvp.listeners;

import com.venned.simplecorepvp.build.PlayerStreak;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerKillStreakEvent implements Listener {

    private final Plugin plugin;
    private final Set<PlayerStreak> playerStreak;

    public PlayerKillStreakEvent(Plugin plugin) {
        this.plugin = plugin;
        this.playerStreak = new HashSet<>();
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer != null) {
            Optional<PlayerStreak> playerStreakOptional = playerStreak.stream().findFirst().filter(p -> p.getPlayer().equals(killer));
            if(playerStreakOptional.isPresent()) {
                PlayerStreak playerStreak = playerStreakOptional.get();
                playerStreak.incrementKills();
                if (plugin.getConfig().contains("kill-streak-rewards." + playerStreak.getKills())) {
                    giveKillStreakRewards(killer, playerStreak.getKills(), playerStreak);
                }
            } else {
                PlayerStreak playerStreak_ = new PlayerStreak(killer, new ArrayList<>(), 1);
                playerStreak.add(playerStreak_);
            }
        }

        Optional<PlayerStreak> victim_optional = playerStreak.stream().findAny().filter(p -> p.getPlayer().equals(victim));
        if(victim_optional.isPresent()) {
            PlayerStreak playerStreak = victim_optional.get();
            List<ItemStack> listItems = playerStreak.getList_items();

            if (playerStreak.getPlayer().getInventory().getArmorContents() != null) {
                playerStreak.getPlayer().getInventory().setArmorContents(new ItemStack[0]); // Vacía la armadura
            }

            if (!listItems.isEmpty()) {
                for (ItemStack item : listItems) {
                    playerStreak.getPlayer().getInventory().removeItem(item);
                }
            }

            for (ItemStack item : listItems) {
                e.getDrops().remove(item);
            }

            playerStreak.setKills(0);
            playerStreak.setList_items(new ArrayList<>());
        }
    }

    private void giveKillStreakRewards(Player player, int streak, PlayerStreak playerStreak) {
        String streakPath = "kill-streak-rewards." + streak;

        if (plugin.getConfig().contains(streakPath + ".ARMORS")) {
            for (String armorType : plugin.getConfig().getConfigurationSection(streakPath + ".ARMORS").getKeys(false)) {
                String armorData = plugin.getConfig().getString(streakPath + ".ARMORS." + armorType);
                ItemStack armorItem = createItemStackWithEnchants(armorData);

                switch (armorType.toUpperCase()) {
                    case "HELMET":
                        player.getInventory().setHelmet(armorItem);
                        break;
                    case "CHEST_PLATE":
                        player.getInventory().setChestplate(armorItem);
                        break;
                    case "LEGGINGS":
                        player.getInventory().setLeggings(armorItem);
                        break;
                    case "BOOTS":
                        player.getInventory().setBoots(armorItem);
                        break;
                }
                playerStreak.addItems(armorItem);
            }
        }

        if (plugin.getConfig().contains(streakPath + ".WEAPON.SWORD")) {
            String swordData = plugin.getConfig().getString(streakPath + ".WEAPON.SWORD");
            ItemStack swordItem = createItemStackWithEnchants(swordData);
            player.getInventory().addItem(swordItem);
            playerStreak.addItems(swordItem);
        }

        if (plugin.getConfig().contains(streakPath + ".ITEMS")) {
            List<String> items = plugin.getConfig().getStringList(streakPath + ".ITEMS");
            for (String itemData : items) {
                ItemStack itemStack = createItemStackWithEnchants(itemData);
                player.getInventory().addItem(itemStack);
                playerStreak.addItems(itemStack);
            }
        }

        player.sendMessage("§7¡Has alcanzado una racha de " + streak + " muertes y has recibido recompensas!");
    }

    private ItemStack createItemStackWithEnchants(String itemData) {
        String[] parts = itemData.split(":");
        Material material = Material.valueOf(parts[0]);
        int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
        ItemStack itemStack = new ItemStack(material, amount);

        if (parts.length > 2 && parts[2].equals("ENCHANTS")) {
            for (int i = 3; i < parts.length; i += 2) {
                if (i + 1 < parts.length) {
                    String enchantmentName = parts[i];
                    int enchantmentLevel = Integer.parseInt(parts[i + 1]);
                    Enchantment enchantment = Enchantment.getByName(enchantmentName);
                    if (enchantment != null) {
                        itemStack.addUnsafeEnchantment(enchantment, enchantmentLevel);
                    }
                }
            }
        }

        return itemStack;
    }

}

