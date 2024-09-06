package com.venned.simplecorepvp.races;

import com.venned.simplecorepvp.abstracts.AbstractRace;
import com.venned.simplecorepvp.interfaces.ConfigRace;
import com.venned.simplecorepvp.interfaces.OnDeathRace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

@ConfigRace(fileName = "herrero.yml")
public class Herrero extends AbstractRace implements OnDeathRace {

    public Herrero(String race_name) {
        super(race_name);
    }

    @Override
    public void OnDeath(PlayerDeathEvent event, Player killer) {
        if (killer != null) {
            for (ItemStack armorPiece : killer.getInventory().getArmorContents()) {
                if (armorPiece != null && armorPiece.getType().getMaxDurability() > 0) {
                    short currentDurability = armorPiece.getDurability();
                    short maxDurability = armorPiece.getType().getMaxDurability();

                    short durabilityToRegenerate = (short) ((maxDurability - currentDurability) / 2);

                    short newDurability = (short) Math.max(0, currentDurability - durabilityToRegenerate);
                    armorPiece.setDurability(newDurability);
                }
            }
            killer.sendMessage("§a¡Tu armadura se ha regenerado un 50% tras derrotar a " + event.getEntity().getName() + "!");
        }
    }
}
