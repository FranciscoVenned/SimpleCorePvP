package com.venned.simplecorepvp.build;

import com.venned.simplecorepvp.interfaces.Races;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class PlayerData {
    private int kills;
    private int deaths;
    private final UUID uuid;
    int experience;
    int experience_required;
    int level;
    int GCoins;
    int remainder;
    int assist;
    int apple_consumed;
    int time_played;
    Races race;
    int prestige;
    long last_movement;
    Location last_location;
    List<Races> list_races_bought;

    public PlayerData(UUID uuid, int kills, int deaths, int experience, int experience_required, int level, int GCoins, int remainder, int assist, int apple_consumed, int time_played, Races race, int prestige, long last_movement
    , List<Races> list_races_bought) {
       this.uuid = uuid;
       this.kills = kills;
       this.deaths = deaths;
       this.experience = experience;
       this.experience_required = experience_required;
       this.level = level;
       this.GCoins = GCoins;
       this.remainder = remainder;
       this.assist = assist;
       this.apple_consumed = apple_consumed;
       this.time_played = time_played;
       this.race = race;
       this.prestige = prestige;
       this.last_movement = last_movement;
       this.list_races_bought = list_races_bought;
   }

    public List<Races> getList_races_bought() {
        return list_races_bought;
    }

    public void setLast_location(Location last_location) {
        this.last_location = last_location;
    }

    public Location getLast_location() {
        return last_location;
    }

    public void setLast_movement(long last_movement) {
        this.last_movement = last_movement;
    }

    public long getLast_movement() {
        return last_movement;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public int getPrestige() {
        return prestige;
    }

    public void decrementGCoins(int gCoins) {
        this.GCoins -= gCoins;
    }

    public void setGCoins(int GCoins) {
        this.GCoins = GCoins;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Races getRace() {
        return race;
    }

    public void setRace(Races race) {
        this.race = race;
    }

    public int getKills() {
        return kills;
    }

    public void incrementKills() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void incrementDeaths() {
        this.deaths++;
    }

    public void incrementLevel() {
        this.level++;
    }

    public void incrementPrestige() {
        this.prestige++;
    }

    public void incrementGApple(){
        this.apple_consumed++;
    }

    public void incrementLevel(int level){
        this.level += level;
    }

    public void incrementGCoins(int gcoins){
        this.GCoins += gcoins;
    }

    public void incrementExperience(int experience){
        this.experience += experience;
    }

    public void setExperience_required(int experience_required) {
        this.experience_required = experience_required;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getTime_played() {
        return time_played;
    }

    public int getRemainder() {
        return remainder;
    }

    public int getGCoins() {
        return GCoins;
    }

    public int getExperience_required() {
        return experience_required;
    }

    public int getExperience() {
        return experience;
    }

    public int getAssist() {
        return assist;
    }

    public int getApple_consumed() {
        return apple_consumed;
    }

    public int getLevel() {
        return level;
    }

    public UUID getUUID() {
        return uuid;
    }
}
