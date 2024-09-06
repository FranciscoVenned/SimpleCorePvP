package com.venned.simplecorepvp.utils;

import com.venned.simplecorepvp.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemsJoin {

    public static void playerGiveItems(@NotNull Player player) {
        ConfigurationSection itemsHubConfig = Main.getPlugin(Main.class).getConfig().getConfigurationSection("items-hub");

        if (itemsHubConfig == null) {
            Bukkit.getLogger().warning("Items-hub configuration section is missing.");
            return;
        }

        Set<String> slots = itemsHubConfig.getKeys(false);
        boolean hasAnyItem = false;

        // Verifica si el jugador ya tiene alguno de los ítems
        for (String slotKey : slots) {
            String materialName = itemsHubConfig.getString(slotKey + ".MATERIAL", "AIR").toUpperCase();
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                Bukkit.getLogger().warning("Invalid material in config: " + materialName);
                continue;
            }

            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                // Establece el nombre del ítem
                String itemName = itemsHubConfig.getString(slotKey + ".NAME");
                if (itemName != null) {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
                }

                // Establece la lore del ítem
                List<String> lore = itemsHubConfig.getStringList(slotKey + ".LORE");
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(coloredLore);

                itemStack.setItemMeta(meta);
            }

            // Verifica si el jugador ya tiene este ítem
            if (playerHasItem(player, itemStack)) {
                hasAnyItem = true;
                break; // Si el jugador tiene alguno de los ítems, no hacemos nada
            }
        }

        // Si el jugador no tiene ninguno de los ítems, se los damos
        if (!hasAnyItem) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            for (String slotKey : slots) {
                int slot;
                try {
                    slot = Integer.parseInt(slotKey);
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning("Invalid slot number in config: " + slotKey);
                    continue;
                }

                String materialName = itemsHubConfig.getString(slotKey + ".MATERIAL", "AIR").toUpperCase();
                Material material = Material.getMaterial(materialName);
                if (material == null) {
                    Bukkit.getLogger().warning("Invalid material in config: " + materialName);
                    continue;
                }

                ItemStack itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    // Establece el nombre del ítem
                    String itemName = itemsHubConfig.getString(slotKey + ".NAME");
                    if (itemName != null) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
                    }

                    // Establece la lore del ítem
                    List<String> lore = itemsHubConfig.getStringList(slotKey + ".LORE");
                    List<String> coloredLore = new ArrayList<>();
                    for (String line : lore) {
                        coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                    }
                    meta.setLore(coloredLore);

                    itemStack.setItemMeta(meta);
                }

                player.getInventory().setItem(slot - 1, itemStack); // -1 porque los números de los slots en la config son 1-based
            }
        }
    }

    private static boolean playerHasItem(@NotNull Player player, @NotNull ItemStack itemStack) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                return true;
            }
        }
        return false;
    }
}