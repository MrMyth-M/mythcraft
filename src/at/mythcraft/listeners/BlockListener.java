package at.mythcraft.listeners;

import at.mythcraft.enchantments.MyEnchants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockListener implements Listener {

    private final Plugin plugin;
    private final List<Block> blockChain = new ArrayList<>();
    private final Map<Material, ItemStack> heatedBlocks = new HashMap<>() {{
        put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        put(Material.ANCIENT_DEBRIS, new ItemStack(Material.NETHERITE_SCRAP));
        put(Material.COBBLESTONE, new ItemStack(Material.STONE));
        put(Material.SAND, new ItemStack(Material.GLASS));
        put(Material.NETHERRACK, new ItemStack(Material.BLACKSTONE));
        put(Material.CLAY, new ItemStack(Material.WHITE_TERRACOTTA));
    }};

    public BlockListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(itemInHand.hasItemMeta()) {
            if(itemInHand.getItemMeta().hasEnchant(MyEnchants.HEATED)) {
                Block block = e.getBlock();
                if(heatedBlocks.containsKey(block.getType())) {
                    e.setDropItems(false);
                    Location loc = block.getLocation();
                    loc.add(0.5, 0.5, 0.5);
                    player.getWorld().spawnParticle(Particle.FLAME, loc, 0, 0, 0, 0, 10);
                    player.getWorld().playSound(loc, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 1, 1);
                    player.getWorld().dropItem(loc, heatedBlocks.get(block.getType()));
                }
            }
            else if(itemInHand.getItemMeta().hasEnchant(MyEnchants.LUMBER)) {
                Block block = e.getBlock();
                String blockName = block.getType().name();
                if(blockName.contains("LOG") || blockName.contains("STEM")) {
                    blockChain.add(block);
                    checkNeighbours(block.getLocation(), block.getType());
                    updateDurability(itemInHand, 1, blockChain.size());
                    blockChain.forEach(b -> {
                        b.breakNaturally(itemInHand);
                        Location loc = b.getLocation();
                        loc.add(0.5, 0.5, 0.5);
                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc,1, 1, 0.1, 0.1, 0.1);
                    });
                    blockChain.clear();
                }
            }
        }
    }

    private void checkNeighbours(Location loc, Material blockType) {
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                for(int z = 0; z < 3; z++) {
                    if(x != 1 || y != 1 || z != 1) {
                        Block curBlock = getBlockNeighbour(loc, x, y, z);
                        if(curBlock.getType().equals(blockType)) {
                            if(!blockChain.contains(curBlock)) {
                                blockChain.add(curBlock);
                                checkNeighbours(curBlock.getLocation(), blockType);
                            }
                        }
                    }
                }
            }
        }
    }

    private Block getBlockNeighbour(Location loc, int x, int y, int z) {
        return loc.getWorld().getBlockAt((int)(loc.getX() + x - 1), (int)(loc.getY() + y - 1), (int)(loc.getZ() + z - 1));
    }

    private void updateDurability(ItemStack itemInHand, int resistance, int amountOfBlocks) {
        int unbreaking = itemInHand.getEnchantments().getOrDefault(Enchantment.DURABILITY, 0) + 1;
        ItemMeta meta = itemInHand.getItemMeta();
        int existingDamage = ((Damageable) meta).getDamage();
        ((Damageable) meta).setDamage((itemInHand.getType().getMaxDurability() - (itemInHand.getType().getMaxDurability() - existingDamage) + (amountOfBlocks / (resistance * unbreaking))));
        itemInHand.setItemMeta(meta);
    }
}
