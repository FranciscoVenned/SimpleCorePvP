package com.venned.simplecorepvp.utils.config;

import com.venned.simplecorepvp.interfaces.ConfigRace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public class ConfigLoader {

    private final Logger logger;

    public ConfigLoader(Logger logger) {
        this.logger = logger;
    }

    public void loadConfig(Object spells, Plugin plugin) {
        Class<?> clazz = spells.getClass();
        if (clazz.isAnnotationPresent(ConfigRace.class)) {
            ConfigRace configEnchant = clazz.getAnnotation(ConfigRace.class);
            String fileName = configEnchant.fileName();

            // Ensure the directory exists
            File directory = new File(plugin.getDataFolder() + "/races");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    logger.severe("Failed to create directory: " + directory.getPath());
                    return;
                }
            }

            // Ensure the file exists
            File file = new File(directory, fileName);
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        logger.severe("Failed to create config file: " + file.getPath());
                        return;
                    }
                    // Create a default configuration in the new file
                    FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(file);
                    defaultConfig.set("level_requirement", 1);
                    defaultConfig.set("permission", "raza.vampiro");
                    defaultConfig.set("raceDescription", "&6Esta raza es de virgos \n &5Pete");
                    defaultConfig.set("priceGCoins", 30);
                    defaultConfig.set("chance", 0.1);
                    defaultConfig.set("material_name", "DIAMOND");
                    defaultConfig.save(file);
                } catch (IOException e) {
                    logger.severe("Error creating config file: " + file.getPath());
                    e.printStackTrace();
                    return;
                }
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            for (Field field : clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                if (config.contains(fieldName)) {
                    field.setAccessible(true);
                    try {
                        if (field.getType() == double.class) {
                            field.setDouble(spells, config.getDouble(fieldName));
                        } else if (field.getType() == int.class) {
                            field.setInt(spells, config.getInt(fieldName));
                        } else if (field.getType() == String.class) {
                            field.set(spells, config.getString(fieldName));
                        }
                    } catch (IllegalAccessException e) {
                        logger.severe("Failed to set field " + fieldName + " from config file " + fileName);
                        e.printStackTrace();
                    }
                }
            }

            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                for (Field field : superClass.getDeclaredFields()) {
                    String fieldName = field.getName();
                    if (config.contains(fieldName)) {
                        field.setAccessible(true);
                        try {
                            if (field.getType() == double.class) {
                                field.setDouble(spells, config.getDouble(fieldName));
                            } else if (field.getType() == int.class) {
                                field.setInt(spells, config.getInt(fieldName));
                            } else if (field.getType() == String.class) {
                                field.set(spells, config.getString(fieldName));
                            }
                        } catch (IllegalAccessException e) {
                            logger.severe("Failed to set field " + fieldName + " from config file " + fileName);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
