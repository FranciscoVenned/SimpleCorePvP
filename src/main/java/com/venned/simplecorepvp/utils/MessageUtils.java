package com.venned.simplecorepvp.utils;


import com.venned.simplecorepvp.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public enum MessageType {
        TIME_AFK("messages.TIME_AFK"),
        TIME_WARNING("messages.TIME_WARNING"),
        NO_COMMAND_COMBAT("messages.NO_COMMAND_COMBAT"),
        COMBAT_JOIN("messages.COMBAT_JOIN"),
        COMBAT_LEAVE("messages.COMBAT_LEAVE"),
        GIVE_COIN("messages.GIVE_COIN"),
        GIVE_XP("messages.GIVE_XP"),
        GIVE_XP_COMMAND("messages.GIVE_XP_COMMAND"),
        GIVE_COIN_COMMAND("messages.GIVE_COIN_COMMAND"),
        GIVE_LEVEL_COMMAND("messages.GIVE_LEVEL_COMMAND"),
        GIVE_LEVEL("messages.GIVE_LEVEL");

        // Añade más tipos de mensaje aquí

        private final String path;

        MessageType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    // Método para obtener el mensaje
    public static String getMessage(MessageType messageType) {
        String message = Main.getPlugin(Main.class).getConfig().getString(messageType.getPath());
        if (message == null) {
            return ChatColor.RED + "Missing message in config: " + messageType.getPath();
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Método para enviar el mensaje a un jugador
    public static void sendMessage(Player player, MessageType messageType) {
        String message = getMessage(messageType);
        player.sendMessage(message);
    }

    // Método para enviar un mensaje con placeholders
    public static void sendMessage(Player player, MessageType messageType, String... placeholders) {
        String message = getMessage(messageType);
        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }
        player.sendMessage(message);
    }
}
