package at.mythcraft.listeners;

import at.mythcraft.enchantments.MyEnchants;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SlimeyListener implements Listener {

    @EventHandler
    public void onPlayerFallDamage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            ItemStack boots = player.getInventory().getItem(EquipmentSlot.FEET);
            if(boots != null) {
                if(boots.hasItemMeta() && boots.getItemMeta().hasEnchant(MyEnchants.SLIMEY)) {
                    player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0,0.5,0), 20, 0, 0, 0, 0, Material.SLIME_BLOCK.createBlockData());
                    player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 0.3f, 1);
                    player.damage(decreaseDurability(player, boots, (int) player.getFallDistance() / 4));
                    e.setCancelled(true);
                }
            }
        }
    }

    private int decreaseDurability(Player player, ItemStack item, int amount) {
        ItemMeta meta = item.getItemMeta();
        int remainingDamage = 0;
        if (meta instanceof Damageable){
            int itemMaxHp = item.getType().getMaxDurability();
            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + amount);
            if(((Damageable) meta).getDamage() >= itemMaxHp) {
                remainingDamage = ((Damageable) meta).getDamage() - itemMaxHp;
                player.getInventory().setItem(EquipmentSlot.FEET, null);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            }
        }
        item.setItemMeta(meta);
        return remainingDamage * 4;
    }

}
