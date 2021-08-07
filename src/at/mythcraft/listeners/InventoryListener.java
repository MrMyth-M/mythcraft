package at.mythcraft.listeners;

import at.mythcraft.main.Model;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class InventoryListener implements Listener {

    private Model model;

    public InventoryListener(Model model) {
        this.model = model;
    }
    private List<Integer> slotsToFill = Arrays.asList(27, 28, 29, 30, 32, 33, 34, 35);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory clicked = e.getClickedInventory();
        if(clicked != null && clicked.getViewers().size() > 0) {
            String senderName = clicked.getViewers().get(0).getName();
            for (Player p : model.getInvseePlayers().keySet()) {
                if (p.getDisplayName().equals(senderName)) {
                    Player sender = p;
                    Player victim = model.getInvseePlayers().get(p);
                    if(!model.isInShulker(sender) && clicked.getItem(e.getSlot()) != null && clicked.getItem(e.getSlot()).getType().name().contains("SHULKER")) {
                        model.addPlayerInShulker(sender);
                        ItemStack shulkerBoxItem = clicked.getItem(e.getSlot());
                        BlockStateMeta meta = (BlockStateMeta) shulkerBoxItem.getItemMeta();
                        ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                        clicked.setContents(shulker.getInventory().getContents());
                        sender.playSound(sender.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1, 1);
                        clicked.setItem(31, shulkerBoxItem);
                        // BACK BUTTON
                        ItemStack back = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                        ItemMeta backMeta = back.getItemMeta();
                        backMeta.setDisplayName("Zurück");
                        back.setItemMeta(backMeta);
                        clicked.setItem(40, back);

                        for(Integer i : slotsToFill) {
                            clicked.setItem(i, new ItemStack(Material.BLUE_STAINED_GLASS_PANE));
                        }
                    }
                    if(model.isInShulker(sender)) {
                        if(e.getSlot() == 40) {
                            sender.playSound(sender.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 1, 1);
                            model.removePlayerInShulker(sender);
                            clicked.setContents(victim.getInventory().getContents());
                            clicked.setItem(41, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                            clicked.setItem(42, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                            clicked.setItem(43, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                            clicked.setItem(44, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player sender = (Player) e.getPlayer();
        if(model.getInvseePlayers().keySet().contains(sender)) {
            model.removeInvseePlayer(sender);
        }
    }
}
