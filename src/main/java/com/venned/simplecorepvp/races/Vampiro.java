package com.venned.simplecorepvp.races;

import com.venned.simplecorepvp.abstracts.AbstractRace;
import com.venned.simplecorepvp.interfaces.ConfigRace;
import com.venned.simplecorepvp.interfaces.OnHitRace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

@ConfigRace(fileName = "vampiro.yml")
public class Vampiro extends AbstractRace implements OnHitRace {

    double chance;
    public Vampiro(String race_name) {
        super(race_name);
    }

    @Override
    public void OnHit(EntityDamageByEntityEvent event, Player player) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (new Random().nextDouble() <= chance) {
                double newHealth = Math.min(attacker.getHealth() + 1.0, attacker.getMaxHealth());
                attacker.setHealth(newHealth);
                attacker.sendMessage("§a¡Has regenerado medio corazón!");
            }
        }
    }
}
