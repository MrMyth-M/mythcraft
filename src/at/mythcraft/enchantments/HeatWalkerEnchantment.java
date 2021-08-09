package at.mythcraft.enchantments;

import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class HeatWalkerEnchantment extends CustomEnchantment {

    public HeatWalkerEnchantment() {
        super("heatwalker", "Heat Walker", 3);
    }

    @Override
    public int getLevel(int xp) {
        if(xp > 20) {
            int random = ThreadLocalRandom.current().nextInt(0, 100);
            if(random > 90) {
                return 1;
            }
            else if(random < 65) {
                return 2;
            }
            else {
                return 3;
            }
        }
        return 1;
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
        return (int) (xp * 1.5);
    }

    public static Pair<Integer, Integer> getBlockBonus(int level) {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        if(level == 1) {
            return new Pair<>(0, 1);
        }
        else if(level == 2) {
            return new Pair<>(-1, 1);
        }
        return new Pair<>(-1, 2);
    }
}
