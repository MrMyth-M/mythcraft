package at.mythcraft.listeners;

import at.mythcraft.enchantments.CustomEnchantment;
import at.mythcraft.enchantments.MyEnchants;
import at.mythcraft.main.Model;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
                        item = enchantItem(item, enchantment, enchantment.getLevel(e.getExpLevelCost()));
                        item.addEnchantments(e.getEnchantsToAdd());
                    }
                }
            }
        }
    }

    private ItemStack enchantItem(ItemStack item, Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + enchantment.getName() + getLevelString(level));
        if(meta.hasLore()) {
            for(String st : meta.getLore()) {
                lore.add(st);
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment, level == 0 ? 1 : level);
        return item;
    }

    private String getLevelString(int level) {
        switch(level) {
            case 1:
                return " I";
            case 2:
                return " II";
            case 3:
                return " III";
            case 4:
                return " IV";
            case 5:
                return " V";
            default: return "";
        }
    }
}
