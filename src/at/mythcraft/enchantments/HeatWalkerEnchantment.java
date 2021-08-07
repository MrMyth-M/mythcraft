package at.mythcraft.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class HeatWalkerEnchantment extends CustomEnchantment {

    public HeatWalkerEnchantment() {
        super("heatwalker", "Heat Walker", 3);
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        if(enchantment.getKey().equals(Enchantment.FROST_WALKER.getKey())) {
            return true;
        }
        if(enchantment.getKey().equals(Enchantment.DEPTH_STRIDER.getKey())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material type = itemStack.getType();
        if(type == Material.NETHERITE_BOOTS)
            return true;
        if(type == Material.IRON_BOOTS)
            return true;
        if(type == Material.GOLDEN_BOOTS)
            return true;
        if(type == Material.DIAMOND_BOOTS)
            return true;
        return false;
    }

    @Override
    public int getEnchantmentChance(int xp) {
        return 100;
    }
}
