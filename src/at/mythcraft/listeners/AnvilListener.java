package at.mythcraft.listeners;

import at.mythcraft.enchantments.CustomEnchantment;
import at.mythcraft.main.Model;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AnvilListener implements Listener {

    private Model model;

    public AnvilListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent e) {
        ItemStack itemInSlot = e.getInventory().getItem(0);
        ItemStack itemInSecondSlot = e.getInventory().getItem(1);
        ItemStack result = e.getResult();

        result = checkItemForResult(itemInSlot, itemInSecondSlot, result);
        if(result != null) {
            result = checkItemForResult(itemInSecondSlot, itemInSlot, result);
            if(result == null) {
                e.getInventory().setRepairCost(9999);
            }
            e.setResult(result);
            // SET REPAIR COST?
        } else {
            e.getInventory().setRepairCost(9999);
            e.setResult(null);
        }
    }

    public ItemStack checkItemForResult(ItemStack first, ItemStack second, ItemStack result) {
        if(first != null && first.hasItemMeta()) {
            for(CustomEnchantment enchantment : model.getAllEnchants()) {
                if(first.getItemMeta().hasEnchant(enchantment)) {
                    result = applyIfPossible(enchantment, first, second, result);

                }
            }
        }
        return result;
    }

    private ItemStack applyIfPossible(CustomEnchantment enchantment, ItemStack firstItem, ItemStack secondItem, ItemStack result) {
        if(secondItem != null && secondItem.hasItemMeta()) {
            if(secondItem.getItemMeta().hasEnchants()) {
                for(Enchantment e : secondItem.getItemMeta().getEnchants().keySet()) {
                    if(enchantment.conflictsWith(e)) {
                        return null;
                    }
                }
            }
            if(secondItem.getType() == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItem.getItemMeta();
                for(Enchantment e : meta.getStoredEnchants().keySet()) {
                    if(enchantment.conflictsWith(e)) {
                        return null;
                    }
                }
            }
            result.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel());
        }
        if(result.getItemMeta() != null) {
            ItemMeta meta = result.getItemMeta();
            if(!meta.hasLore()) {
                System.out.println("no lore :(");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + enchantment.getName());
                meta.setLore(lore);
                result.setItemMeta(meta);
            }
        }
        return result;
    }
}
