package at.mythcraft.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment extends Enchantment {

    private String name;
    private int maxLvl;

    public CustomEnchantment(String namespace, String name, int maxLvl) {
        super(NamespacedKey.minecraft(namespace));
        this.name = name;
        this.maxLvl = maxLvl;
    }

    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public abstract boolean conflictsWith(Enchantment enchantment);

    @Override
    public abstract boolean canEnchantItem(ItemStack itemStack);

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLvl;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

}
