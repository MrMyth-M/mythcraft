package at.mythcraft.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class HeatedEnchantment extends CustomEnchantment {

    public HeatedEnchantment() {
        super("heated", "Heated", 1);
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        if(enchantment.getName().equals(Enchantment.SILK_TOUCH.getName()))
            return true;
        if(enchantment.getName().equals(Enchantment.LOOT_BONUS_BLOCKS.getName()))
            return true;
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material type = itemStack.getType();
        if(type == Material.STONE_PICKAXE)
            return true;
        if(type == Material.GOLDEN_PICKAXE)
            return true;
        if(type == Material.IRON_PICKAXE)
            return true;
        if(type == Material.DIAMOND_PICKAXE)
            return true;
        if(type == Material.NETHERITE_PICKAXE)
            return true;
        return false;
    }
}
