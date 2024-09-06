package com.venned.simplecorepvp.manager;

import com.venned.simplecorepvp.interfaces.Races;
import com.venned.simplecorepvp.races.Alquimista;
import com.venned.simplecorepvp.races.Hercules;
import com.venned.simplecorepvp.races.Herrero;
import com.venned.simplecorepvp.races.Vampiro;
import com.venned.simplecorepvp.utils.MapUtils;
import com.venned.simplecorepvp.utils.config.ConfigLoader;
import org.bukkit.plugin.Plugin;

public class LoadRace {

    private final MapUtils mapUtils;
    private final ConfigLoader configLoader;
    private final Plugin plugin;

    public LoadRace(MapUtils mapUtils, Plugin plugin) {
        this.mapUtils = mapUtils;
        this.plugin = plugin;
        this.configLoader = new ConfigLoader(plugin.getLogger());
        loadRaces();
    }

    public void loadRaces(){
        Vampiro vampiro = new Vampiro("vampiro");
        Herrero herrero = new Herrero("herrero");
        Hercules hercules = new Hercules("hercules");
        Alquimista alquimista = new Alquimista("alquimista");

        configLoader.loadConfig(hercules, plugin);
        configLoader.loadConfig(herrero, plugin);
        configLoader.loadConfig(vampiro, plugin);
        configLoader.loadConfig(alquimista, plugin);
        mapUtils.races.add(herrero);
        mapUtils.races.add(vampiro);
        mapUtils.races.add(hercules);
        mapUtils.races.add(alquimista);

        for(Races races : mapUtils.races){
            plugin.getLogger().info("Raza " + races.getRaceName() + " Cargada!");
        }
    }

}
