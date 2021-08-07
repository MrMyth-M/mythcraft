package at.mythcraft.chestlock;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomKey implements KeyInfoApplicable {

    private final KeyInfo keyInfo;
    private final ItemStack item;

    public CustomKey(KeyInfo keyInfo, ItemStack item) {
        this.keyInfo = keyInfo;
        this.item = item;
        addLore();
    }

    private void addLore() {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Key - " + this.keyInfo.getKeyId());
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CustomKey{" +
                "keyInfo=" + keyInfo +
                ", item=" + item.getType() +
                '}';
    }

    public CustomKey clone() {
        CustomKey duplicate = new CustomKey(this.keyInfo.clone(), this.item.clone());
        duplicate.getKeyInfo().setDuplicate(true);
        return duplicate;
    }
}
