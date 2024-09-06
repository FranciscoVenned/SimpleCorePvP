package com.venned.simplecorepvp.races;

import com.venned.simplecorepvp.abstracts.AbstractRace;
import com.venned.simplecorepvp.interfaces.ConfigRace;
import com.venned.simplecorepvp.interfaces.OnDeathRace;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@ConfigRace(fileName = "alquimista.yml")
public class Alquimista extends AbstractRace implements OnDeathRace {

    public Alquimista(String race_name) {
        super(race_name);
    }

    @Override
    public void OnDeath(PlayerDeathEvent event, Player killer) {

            PlayerInventory inventory = killer.getInventory();

            // Crear pociones de curación II lanzables
            ItemStack potion = new ItemStack(Material.POTION, 3);
            Potion potionEffect = new Potion(PotionType.INSTANT_HEAL, 2); // Nivel 2 para curación II
            potionEffect.setSplash(true); // Configura la poción como lanzable

            // Aplicar el efecto a la poción
            potionEffect.apply(potion);

            // Agregar las pociones al inventario del jugador
            inventory.addItem(potion);

            // Notificar al jugador si es necesario
            killer.sendMessage("§a¡Has recibido 3 pociones de curación II lanzables por tu victoria!");
        }

}
