package at.mythcraft.enchantments;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TelepathyEnchantment extends CustomEnchantment {

    public TelepathyEnchantment() {
        super("telepathy", "Telepathy", 1);
    }

    @Override
    public int getLevel(int xp) {
        return 0;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        if(enchantment.getKey().equals(MyEnchants.HEAT_WALKER.getKey())) {
            return true;
        }
        if(enchantment.getKey().equals(Enchantment.FROST_WALKER.getKey())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material type = itemStack.getType();
        if(type == Material.LEATHER_BOOTS)
            return true;
        if(type == Material.IRON_BOOTS)
            return true;
        if(type == Material.GOLDEN_BOOTS)
            return true;
        if(type == Material.DIAMOND_BOOTS)
            return true;
        if(type == Material.NETHERITE_BOOTS)
            return true;
        return false;
    }

    @Override
    public int getEnchantmentChance(int xp) {
        return xp;
    }
}
