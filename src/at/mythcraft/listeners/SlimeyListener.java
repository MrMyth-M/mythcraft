package at.mythcraft.listeners;

import at.mythcraft.enchantments.MyEnchants;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SlimeyListener implements Listener {

    @EventHandler
    public void onPlayerFallDamage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            ItemStack boots = player.getInventory().getItem(EquipmentSlot.FEET);
            if(boots != null) {
                if(boots.hasItemMeta() && boots.getItemMeta().hasEnchant(MyEnchants.SLIMEY)) {
                    e.setCancelled(true);
                    player.getWorld().spawnParticle(Particle.SLIME, 0, 1, 0, 10);
                    player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 0.3f, 1);
                }
            }
        }
    }

}
