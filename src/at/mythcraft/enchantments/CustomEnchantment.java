package at.mythcraft.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class CustomEnchantment extends Enchantment {

    private final String name;
    private final int maxLvl;

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

    public boolean conflictsWithAnyOf(Set<Enchantment> enchantments) {
        boolean conflicts = false;
        for(Enchantment enchantment : enchantments) {
            if(this.conflictsWith(enchantment)) {
                conflicts = true;
            }
        }
        return conflicts;
    }

    public abstract int getLevel(int xp);

    @Override
    public abstract boolean conflictsWith(Enchantment enchantment);

    @Override
    public abstract boolean canEnchantItem(ItemStack itemStack);

    public abstract int getEnchantmentChance(int xp);

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
