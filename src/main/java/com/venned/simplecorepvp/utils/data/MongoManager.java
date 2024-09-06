package com.venned.simplecorepvp.utils.data;

import com.mongodb.client.model.ReplaceOptions;
import com.venned.simplecorepvp.build.PlayerData;
import com.venned.simplecorepvp.interfaces.Races;
import com.venned.simplecorepvp.utils.MapUtils;
import org.bson.Document;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class MongoManager {

    private final MapUtils mapUtils;
    private final MongoConnection database;

    public MongoManager(MapUtils mapUtils, MongoConnection mongoConnection) {
        this.mapUtils = mapUtils;
        this.database = mongoConnection;
    }

    public void loadPlayerData() {
        for (Document doc : database.getCollection().find()) {
            UUID uuid = UUID.fromString(doc.getString("uuid"));
            int kills = doc.getInteger("kills");
            int deaths = doc.getInteger("deaths");
            int experience = doc.getInteger("experience");
            int experience_required = doc.getInteger("experience_required");
            int level = doc.getInteger("level");
            int GCoins = doc.getInteger("GCoins");
            int remainder = doc.getInteger("remainder");
            int assist = doc.getInteger("assist");
            int apple_consumed = doc.getInteger("apple_consumed");
            int time_played = doc.getInteger("time_played");

            String raceName = doc.getString("race");
            Races race = raceName != null ? loadRaceFromName(raceName) : null;
            int prestige = doc.getInteger("prestige");

            List<Races> racesBought = doc.getList("list_races_bought", String.class).stream()
                    .map(this::loadRaceFromName)
                    .collect(Collectors.toList());

            mapUtils.playerDataMap.put(uuid, new PlayerData(uuid, kills, deaths,
                    experience, experience_required, level, GCoins, remainder, assist, apple_consumed, time_played, race, prestige, System.currentTimeMillis(),
                    racesBought));
        }
    }

    public void savePlayerData() {
        for (UUID uuid : mapUtils.playerDataMap.keySet()) {
            PlayerData data = mapUtils.playerDataMap.get(uuid);

            List<String> races_bought = data.getList_races_bought().stream()
                    .map(Races::getRaceName)
                    .collect(Collectors.toList());

            Document doc = new Document("uuid", uuid.toString())
                    .append("kills", data.getKills())
                    .append("deaths", data.getDeaths())
                    .append("experience", data.getExperience())
                    .append("experience_required", data.getExperience_required())
                    .append("level", data.getLevel())
                    .append("GCoins", data.getGCoins())
                    .append("remainder", data.getRemainder())
                    .append("assist", data.getAssist())
                    .append("apple_consumed", data.getApple_consumed())
                    .append("time_played", data.getTime_played())
                    .append("race", data.getRace() != null ? data.getRace().getRaceName() : null)
                    .append("prestige", data.getPrestige())
                    .append("last_movement", data.getLast_movement())
                    .append("list_races_bought", races_bought);

            database.getCollection().replaceOne(eq("uuid", uuid.toString()), doc, new ReplaceOptions().upsert(true));
        }
    }
    private Races loadRaceFromName(String raceName) {
        for(Races races : mapUtils.races){
            if(races.getRaceName().equals(raceName)){
                return races;
            }
        }
        return null;
    }

}
