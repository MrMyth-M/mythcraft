package at.mythcraft.listeners;

import at.mythcraft.enchantments.CustomEnchantment;
import at.mythcraft.enchantments.MyEnchants;
import at.mythcraft.main.Model;
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

    private Model model;

    public EnchantListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onPlayerItemEnchant(EnchantItemEvent e) {
        ItemStack item = e.getItem();
        for(CustomEnchantment enchantment : model.getAllEnchants()) {
            // Check if enchantment is applicable for item
            if(enchantment.canEnchantItem(item)) {
                // Check if enchantment conflicts with enchantments already added
                if(!enchantment.conflictsWithAnyOf(e.getEnchantsToAdd().keySet())) {
                    int random = ThreadLocalRandom.current().nextInt(0, 100);
                    if(random < enchantment.getEnchantmentChance(e.getExpLevelCost())) {
                        item = enchantItem(item, enchantment);
                        item.addEnchantments(e.getEnchantsToAdd());
                    }
                }
            }
        }
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
