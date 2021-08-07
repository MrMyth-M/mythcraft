package at.mythcraft.listeners;

import at.mythcraft.chestlock.CustomKey;
import at.mythcraft.chestlock.CustomLocation;
import at.mythcraft.chestlock.LockedChest;
import at.mythcraft.main.Model;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ChestListener implements Listener {

    private Model model;

    public ChestListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e) {
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            LockedChest chest = model.getChestByLocation(e.getClickedBlock().getLocation());
            CustomKey key = model.getIfKey(item, player);
            Action action = e.getAction();
            if(chest == null && key == null) {
                e.setCancelled(false);
            }
            else if (chest != null && key != null && action == Action.RIGHT_CLICK_BLOCK) {
                if(!chest.getKeyInfo().matches(key.getKeyInfo())) {
                    e.setCancelled(true);
                }
            }
            else if(key != null) {
                if(player.isSneaking() && action == Action.LEFT_CLICK_BLOCK) {
                    Location chestLocation = e.getClickedBlock().getLocation();
                    if(key.getKeyInfo().getLocation() == null) {
                        if(model.getChestByLocation(chestLocation) == null) {
                            // SAVE KEY INFO AFTER LOCATION CHANGE
                            key.getKeyInfo().setLocation(chestLocation);
                            model.saveKeyInfos(model.getKeys(), model.KEY_FILE);
                            // SAVE CHEST INFO AFTER LOCATION CHANGE
                            LockedChest lockedChest = new LockedChest(key.getKeyInfo());
                            model.addLockedChest(lockedChest);
                            player.sendMessage(ChatColor.YELLOW + "Du hast diese Truhe abgeschlossen.");
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "Diese Truhe wurde schon abgeschlossen.");
                        }
                    }
                    else if(key.getKeyInfo().getLocation().matches(new CustomLocation(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ()))){
                        player.sendMessage(ChatColor.RED + "Du hast diese Truhe bereits abgeschlossen.");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Das abschließen von mehreren Truhen muss noch implementiert werden.");
                    }
                    e.setCancelled(true);
                }
            }
            else {
                e.setCancelled(true);
            }
        }
    }
}
