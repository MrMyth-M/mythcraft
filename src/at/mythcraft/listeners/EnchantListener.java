package at.mythcraft.listeners;

import at.mythcraft.enchantments.MyEnchants;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantListener implements Listener {

    @EventHandler
    public void onPlayerItemEnchant(EnchantItemEvent e) {
        ItemStack item = e.getItem();
        String itemName = item.getType().name();
        if(itemName.contains("PICKAXE")) {
            if(!e.getEnchantsToAdd().containsKey(Enchantment.SILK_TOUCH) && !e.getEnchantsToAdd().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                int percentage = (int)((e.getExpLevelCost() - 10) * 1.8);
                int random = ThreadLocalRandom.current().nextInt(0, 100);
                if(random < percentage) {
                    item = enchantItem(item, MyEnchants.HEATED);
                    item.addEnchantments(e.getEnchantsToAdd());
                }
            }
        }
        else if(itemName.contains("AXE") && !itemName.contains("PICK")) {
            int percentage = (e.getExpLevelCost() - 10);
            int random = ThreadLocalRandom.current().nextInt(0, 100);
            if(random < percentage) {
                item = enchantItem(item, MyEnchants.LUMBER);
                item.addEnchantments(e.getEnchantsToAdd());
            }
        }
        /*else if(itemName.contains("BOOK")) {
            int percentage = 50;
            int random = ThreadLocalRandom.current().nextInt(0, 100);
            if(random < percentage) {
                item = enchantItem(item, CustomEnchants.LUMBER);
                item.addEnchantments(e.getEnchantsToAdd());
            }
        }*/
    }

    private ItemStack enchantItem(ItemStack item, Enchantment enchantment) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + enchantment.getName());
        if(meta.hasLore()) {
            for(String st : meta.getLore()) {
                lore.add(st);
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment, 1);
        return item;
    }
}
