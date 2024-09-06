package com.venned.simplecorepvp.commands;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.interfaces.Races;
import com.venned.simplecorepvp.utils.LevelUtils;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MainCommand implements CommandExecutor {

    private final MapUtils mapUtils;
    private final Plugin plugin;

    public MainCommand(MapUtils mapUtils, Plugin plugin) {
        this.mapUtils = mapUtils;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (args.length == 0) {
                if(sender.hasPermission("simplecorepvp.*")) {
                    sender.sendMessage("§7§m-----------------------------");
                    sender.sendMessage("§cComandos Administrativos ");
                    sender.sendMessage(" ");
                    sender.sendMessage("§c- §7givelvl <player> <nivel> / Aumentar nivel al jugador");
                    sender.sendMessage("§c- §7prestige / Aumentar Prestigio");
                    sender.sendMessage("§c- §7reload / Recarga el Plugin");
                    sender.sendMessage("§c- §7givecoins <player> <monto> / Aumentar los coins a un jugador");
                    sender.sendMessage(" ");
                    sender.sendMessage("§7§m-----------------------------");
                    return true;
                } else {
                    sender.sendMessage("§7§m-----------------------------");
                    sender.sendMessage("§cGUIA DE COMANDOS");
                    sender.sendMessage(" ");
                    sender.sendMessage("§c- §7prestige / Aumenta tu Prestigio");
                    sender.sendMessage(" ");
                    sender.sendMessage("§7§m-----------------------------");
                    return true;
                }

            }

        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("simplecorepvp.*")) return true;
            sender.sendMessage("reload");
            plugin.reloadConfig();
            ((Main) plugin).getZonesUtils().reloadZone();
            return true;
        }
        if(args[0].equalsIgnoreCase("givelvl")) {
            if(!sender.hasPermission("simplecorepvp.*")) return true;
            if(args.length < 3){
                sender.sendMessage("§cDebes incluir al jugador y el nivel");
                return true;
            }
            String name_player = args[1];
            Player player = Bukkit.getPlayer(name_player);
            if (player != null){
                int nivel;
                try{
                    nivel = Integer.parseInt(args[2]);
                } catch (NumberFormatException e){
                    sender.sendMessage("§cColoca un numero valido");
                    return true;
                }
                PlayerData playerData = mapUtils.playerDataMap.get(player.getUniqueId());
                if(playerData != null){
                    playerData.incrementLevel(nivel);
                    MessageUtils.sendMessage((Player) sender, MessageUtils.MessageType.GIVE_LEVEL_COMMAND, "%player%", player.getName(), "%nivel%", String.valueOf(nivel));
                    MessageUtils.sendMessage(player, MessageUtils.MessageType.GIVE_LEVEL, "%nivel%", String.valueOf(nivel));
                }
            }
        }
        if(args[0].equalsIgnoreCase("race")){
            if(sender instanceof Player) {
                if (args.length < 2) {
                    sender.sendMessage("§c Coloca el nombre de la raza");
                    return true;
                }
                Races race = mapUtils.getRace(args[1]);
                if (race != null) {
                    PlayerData playerData = mapUtils.getPlayerData((OfflinePlayer) sender);
                    if(playerData != null){
                        if(playerData.getList_races_bought().contains(race)){
                            playerData.setRace(race);
                            sender.sendMessage("§7Raza establecida " + race.getRaceName());
                            return true;
                        } else {
                            sender.sendMessage("§aNo tienes esa raza capo");
                        }
                    }
                } else {
                    sender.sendMessage("§cLa raza no existe");
                    return true;
                }
            }
        }



        if(args[0].equalsIgnoreCase("buyRace")){
            if(sender instanceof Player) {
                if (args.length < 2) {
                    sender.sendMessage("§c Coloca el nombre de la raza");
                    return true;
                }
                Races race = mapUtils.getRace(args[1]);
                if (race != null) {
                    PlayerData playerData = mapUtils.getPlayerData((OfflinePlayer) sender);
                    if(playerData != null){
                        if(!playerData.getList_races_bought().contains(race)) {
                            if (race.getLevelRequirement() < playerData.getLevel()) {
                                if (race.getPriceGCoins() < playerData.getGCoins()) {
                                    playerData.getList_races_bought().add(race);
                                    playerData.setGCoins(playerData.getGCoins() - race.getPriceGCoins());
                                    sender.sendMessage("§c§l(!) §7Comprado con Exito!");
                                } else {
                                    sender.sendMessage("§c§l(!) §7No tienes suficiente Coins " + race.getPriceGCoins());
                                }
                            } else {
                                sender.sendMessage("§cNivel Requerido para la clase " + race.getLevelRequirement());
                            }
                        } else {
                            sender.sendMessage("§c§l(!) §7Ya cuentas con esa raza");
                        }
                    }
                } else {
                    sender.sendMessage("§cLa raza no existe");
                    return true;
                }
            }
        }
        if(args[0].equalsIgnoreCase("giveCoins")){
            if(!sender.hasPermission("simplecorepvp.*")) return true;
            if(args.length < 3){
                sender.sendMessage("§c§l(!) §7Debes incluir al jugador y el monto de coins");
                return true;
            }
            String name_player = args[1];
            Player player = Bukkit.getPlayer(name_player);
            if (player != null){
                int coins;
                try{
                    coins= Integer.parseInt(args[2]);
                } catch (NumberFormatException e){
                    sender.sendMessage("§c§l(!) §7Coloca un numero valido");
                    return true;
                }
                PlayerData playerData = mapUtils.playerDataMap.get(player.getUniqueId());
                if(playerData != null){
                    playerData.incrementGCoins(coins);
                    MessageUtils.sendMessage((Player) sender, MessageUtils.MessageType.GIVE_COIN_COMMAND, "%player%", plugin.getName(), "%coins%", String.valueOf(coins));
                    MessageUtils.sendMessage(player, MessageUtils.MessageType.GIVE_COIN, "%coins%", String.valueOf(coins));
                }
            }
        }

        if(args[0].equalsIgnoreCase("giveXP")){
            if(!sender.hasPermission("simplecorepvp.*")) return true;
            if(args.length < 3){
                sender.sendMessage("§c§l(!) §7Debes incluir al jugador y el monto de XP");
                return true;
            }
            String name_player = args[1];
            Player player = Bukkit.getPlayer(name_player);
            if (player != null){
                int coins;
                try{
                    coins= Integer.parseInt(args[2]);
                } catch (NumberFormatException e){
                    sender.sendMessage("§c§l(!) §7Coloca un numero valido");
                    return true;
                }
                PlayerData playerData = mapUtils.playerDataMap.get(player.getUniqueId());
                if(playerData != null){
                    ((Main) plugin).getLevelUtils().incrementXP(playerData, coins);
                    MessageUtils.sendMessage((Player) sender, MessageUtils.MessageType.GIVE_XP_COMMAND, "%player%", plugin.getName(), "%xp%", String.valueOf(coins));
                    MessageUtils.sendMessage(player, MessageUtils.MessageType.GIVE_XP, "%xp%", String.valueOf(coins));
                }
            }
        }

        if(args[0].equalsIgnoreCase("prestige")){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                PlayerData playerData = mapUtils.playerDataMap.get(player.getUniqueId());
                if(playerData != null){
                    int level = playerData.getLevel();
                    int prestige = Math.max(playerData.getPrestige(), 1); // Aseguramos que el valor mínimo sea 1
                    int level_base_min = plugin.getConfig().getInt("max-level");
                    double multi_level_prestige = plugin.getConfig().getDouble("multi-level-prestige");
                    int calculate = (int) (prestige * level_base_min * multi_level_prestige);
                    if(level > calculate || level == calculate){
                        player.sendMessage("§7Prestigio subido");
                        playerData.setLevel(0);
                        playerData.incrementPrestige();
                    } else {
                        player.sendMessage("§cNo tienes el nivel suficiente para subir de prestigio, necesitas ser superior a " + (calculate - level));
                    }

                }
            }
        }

        return false;
    }
}
