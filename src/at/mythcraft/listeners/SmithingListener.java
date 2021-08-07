package at.mythcraft.listeners;

import at.mythcraft.chestlock.CustomKey;
import at.mythcraft.chestlock.KeyInfo;
import at.mythcraft.main.Model;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class SmithingListener implements Listener {

    private Model model;

    public SmithingListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onSmithingTableUse(PrepareSmithingEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getInventory().getViewers().get(0);
        if(inv.getItem(0) != null && inv.getItem(1) != null) {
            ItemStack first = inv.getItem(0);
            ItemStack second = inv.getItem(1);
            CustomKey key = model.getIfKey(first, player);
            if(key != null && second.getType() == Material.GOLD_INGOT) {
                ItemStack result = first.clone();
                ItemMeta resultMeta = result.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.addAll(result.getItemMeta().getLore());
                lore.add(ChatColor.DARK_AQUA + "Duplicate");
                resultMeta.setLore(lore);
                result.setItemMeta(resultMeta);
                e.setResult(result);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if(e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.SMITHING) {
            SmithingInventory inv = (SmithingInventory) e.getClickedInventory();
            Player player = (Player) inv.getViewers().get(0);
            if(e.getSlot() == 2 && inv.getItem(e.getSlot()) != null && inv.getItem(1).getType() == Material.GOLD_INGOT) {
                // Create copy of original key and mark as duplicate
                ItemStack originalKey = inv.getItem(0).clone();
                CustomKey key = model.getIfKey(originalKey, player);
                CustomKey clone = key.clone();
                model.addKey(clone);
                // Process smithing event with key
                ItemStack duplicateKey = inv.getItem(e.getSlot());
                ItemStack goldIngots = inv.getItem(1);
                if(e.getClick().isShiftClick()) {
                    duplicateKey.setAmount(goldIngots.getAmount());
                    goldIngots.setAmount(0);
                }
                else {
                    goldIngots.setAmount(goldIngots.getAmount() - 1);
                }
                inv.setItem(1, goldIngots);
                player.setItemOnCursor(duplicateKey);
                player.playSound(player.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, 1, 1);
            }
        }
    }

}
