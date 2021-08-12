package at.mythcraft.listeners;

import at.mythcraft.enchantments.HeatWalkerEnchantment;
import at.mythcraft.enchantments.MyEnchants;
import com.mojang.datafixers.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class HeatWalkerListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if(player.getInventory().getItem(EquipmentSlot.FEET) != null) {
            if(player.getInventory().getItem(EquipmentSlot.FEET).getItemMeta().hasEnchant(MyEnchants.HEAT_WALKER)) {
                Block standingOn = world.getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
                if(standingOn.getType() == Material.LAVA) {
                    int enchantLevel = player.getInventory().getItem(EquipmentSlot.FEET).getItemMeta().getEnchantLevel(MyEnchants.HEAT_WALKER);
                    Pair<Integer, Integer> bonus = HeatWalkerEnchantment.getBlockBonus(enchantLevel);
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.05f, 1f);
                    for(int x = standingOn.getX() + bonus.getFirst(); x < standingOn.getX() + bonus.getSecond(); x++) {
                        for (int z = standingOn.getZ() + bonus.getFirst(); z < standingOn.getZ() + bonus.getSecond(); z++) {
                            Location loc = new Location(world, x, standingOn.getY(), z);
                            if (world.getBlockAt(loc).getType() == Material.LAVA) {
                                world.getBlockAt(loc).setType(Material.BLACKSTONE);
                                world.spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 0, 0, 0, 0, 10);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e) {
        if(e.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            if(player.getInventory().getItem(EquipmentSlot.FEET) != null) {
                if(player.getInventory().getItem((EquipmentSlot.FEET)).getItemMeta().hasEnchant(MyEnchants.HEAT_WALKER)) {
                    // IF DAMAGE BY MAGMA BLOCK -> CANCEL
                    if(e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
                        e.setCancelled(true);
                    }
                    // if damage by fire -> extinguish
                    else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                        Location loc = player.getLocation();
                        Block block = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                        if(block.getType() != Material.SOUL_FIRE) {
                            block.setType(Material.AIR);
                            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 0, 0, 0, 0, 10);
                            player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.1f, 1f);
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
