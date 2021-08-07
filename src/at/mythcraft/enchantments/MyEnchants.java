package at.mythcraft.enchantments;

import at.mythcraft.main.Model;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyEnchants {
    public static final CustomEnchantment HEATED = new HeatedEnchantment();
    public static final CustomEnchantment LUMBER = new LumberEnchantment();

    private Model model;

    public MyEnchants(Model model) {
        this.model = model;
    }

    public void register() {
        boolean registered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(HEATED) &&
                Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LUMBER);

        if(!registered) {
            registerCustomEnchantment(HEATED);
            registerCustomEnchantment(LUMBER);
        }
        if(model.getAllEnchants().isEmpty()) {
            model.addEnchant(HEATED);
            model.addEnchant(LUMBER);
        }
    }

    private void registerCustomEnchantment(CustomEnchantment enchantment) {
        boolean registered = true;
        try {
            Field fieldAcceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            fieldAcceptingNew.setAccessible(true);
            fieldAcceptingNew.set(null, true);
            System.out.println("Registering enchantment");
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        System.out.println("REGISTERED: " + registered);
    }
}
