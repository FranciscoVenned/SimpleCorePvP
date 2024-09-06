package com.venned.simplecorepvp.races;

import com.venned.simplecorepvp.Main;
import com.venned.simplecorepvp.abstracts.AbstractRace;
import com.venned.simplecorepvp.interfaces.ConfigRace;
import com.venned.simplecorepvp.interfaces.OnInteractRace;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@ConfigRace(fileName = "hercules.yml")
public class Hercules extends AbstractRace implements OnInteractRace {

    public Hercules(String race_name) {
        super(race_name);
    }

    @Override
    public void OnInteractRace(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType().name().contains("SWORD")) {

                    if (event.getClickedBlock().getType() == Material.COBBLESTONE) {
                        if(Main.getPlugin(Main.class).getMapUtils().blockPlaced.contains(event.getClickedBlock())) {
                            Main.getPlugin(Main.class).getMapUtils().blockPlaced.remove(event.getClickedBlock());
                            event.getClickedBlock().setType(Material.AIR);
                        }
                    }

            }
        }
    }
}
