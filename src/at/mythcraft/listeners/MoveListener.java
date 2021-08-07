package at.mythcraft.listeners;

import at.mythcraft.enchantments.MyEnchants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class MoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        if(player.getInventory().getItem(EquipmentSlot.FEET) != null) {
            if(player.getInventory().getItem(EquipmentSlot.FEET).getItemMeta().hasEnchant(MyEnchants.HEAT_WALKER)) {
                Block standingOn = player.getWorld().getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());

                if(standingOn.getType() == Material.LAVA) {
                    for(int x = standingOn.getX() - 1; x < standingOn.getX() + 1; x++) {
                        for(int z = standingOn.getZ() - 1; z < standingOn.getZ() + 1; z++) {
                            Location loc = new Location(world, x, standingOn.getY(), z);
                            if(world.getBlockAt(loc).getType() == Material.LAVA) {
                                world.getBlockAt(loc).setType(Material.BLACKSTONE);
                                world.spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 0, 0, 0, 0, 10);
                                player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH,0.05f, 1f);
                            }
                        }
                    }
                }
            }
        }
    }
}
