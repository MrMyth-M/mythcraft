package at.mythcraft.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LumberEnchantment extends CustomEnchantment {

    public LumberEnchantment() {
        super("lumber", "Lumber", 1);
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material type = itemStack.getType();
        if(type == Material.STONE_AXE)
            return true;
        if(type == Material.GOLDEN_AXE)
            return true;
        if(type == Material.IRON_AXE)
            return true;
        if(type == Material.DIAMOND_AXE)
            return true;
        if(type == Material.NETHERITE_AXE)
            return true;
        if(type == Material.WOODEN_AXE)
            return true;
        return false;
    }

    @Override
    public int getEnchantmentChance(int xp) {
        return xp - 10;
    }
}
