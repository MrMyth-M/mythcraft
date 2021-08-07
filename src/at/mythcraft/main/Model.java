package at.mythcraft.main;

import at.mythcraft.chestlock.*;
import at.mythcraft.enchantments.CustomEnchantment;
import at.mythcraft.player.CustomPlayer;
import at.mythcraft.player.PlayerInfo;
import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

public class Model {

    public static final String PLAYERINFO_PATH = "plugins/MythCraft/PlayerInfo/";
    public static final String CHESTLOCK_PATH = "plugins/MythCraft/ChestLock/";
    public static final String CHEST_FILE = "chests.json";
    public static final String KEY_FILE = "keys.json";
    private final List<CustomPlayer> customPlayers = new ArrayList<>();
    private final List<Player> passivePlayers = new ArrayList<>();
    private final List<CustomEnchantment> allEnchants = new ArrayList<>();
    private final List<CustomKey> keys = new ArrayList<>();
    private final List<LockedChest> lockedChests = new ArrayList<>();

    private final List<Player> inShulker = new ArrayList<>();
    private final Map<Player, Player> invseePlayers = new HashMap<>();
    private boolean invseeEdit = false;

    private Gson gson = new Gson();

    public CustomPlayer getCustomPlayer(Player player) {
        for(CustomPlayer p : customPlayers) {
            if(p.getPlayer().getUniqueId() == player.getUniqueId()) {
                return p;
            }
        }
        CustomPlayer newCustom = new CustomPlayer(player, getPlayerInfo(player));
        customPlayers.add(newCustom);
        return newCustom;
    }

    public PlayerInfo getPlayerInfo(Player player) {
        File file = new File(PLAYERINFO_PATH + player.getUniqueId().toString() + ".json");
        PlayerInfo playerInfo = null;
        if(file.exists()) {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                playerInfo = gson.fromJson(in, PlayerInfo.class);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            playerInfo = createPlayerInfo(player);
        }
        System.out.println(file.getAbsolutePath());
        System.out.println(playerInfo);
        return playerInfo;
    }

    private PlayerInfo createPlayerInfo(Player player) {
        PlayerInfo info = new PlayerInfo(player.getUniqueId().toString(), player.getDisplayName());
        savePlayerInfo(info);
        return info;
    }

    public void loadKeys() {
        File file = new File(CHESTLOCK_PATH + KEY_FILE);
        List<KeyInfo> keyInfos = new ArrayList<>();
        if(file.exists() && file.length() != 0) {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                keyInfos = Arrays.asList(gson.fromJson(in, KeyInfo[].class));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        for(KeyInfo info : keyInfos) {
            CustomKey key = new CustomKey(info, new ItemStack(Material.TRIPWIRE_HOOK));
            keys.add(key);
        }
        System.out.println("Loaded KEYS from " + file.getAbsolutePath());
    }

    public void loadLockedChests() {
        File file = new File(CHESTLOCK_PATH + CHEST_FILE);
        List<KeyInfo> keyInfos = new ArrayList<>();
        List<LockedChest> chests = new ArrayList<>();
        if(file.exists() && file.length() != 0) {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                keyInfos = Arrays.asList(gson.fromJson(in, KeyInfo[].class));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        for(KeyInfo info : keyInfos) {
            lockedChests.add(new LockedChest(info));
        }
        System.out.println("Loaded CHESTS from " + file.getAbsolutePath());
    }

    public void saveKeyInfos(List list, String fileName) {
        File file = new File(CHESTLOCK_PATH + fileName);
        List<KeyInfo> keyInfos = new ArrayList<>();
        for(Object o : list) {
            KeyInfoApplicable keyInfo = (KeyInfoApplicable) o;
            System.out.println("SAVED: " + keyInfo);
            keyInfos.add(keyInfo.getKeyInfo());
        }
        try(FileWriter fw = new FileWriter(file)) {
            gson.toJson(keyInfos, fw);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayerInfo(PlayerInfo info) {
        File file = new File(PLAYERINFO_PATH + info.getUID() + ".json");
        System.out.println("-------------SAVING PLAYER INFO----------------");
        System.out.println(info);
        try(FileWriter fw = new FileWriter(file)) {
            gson.toJson(info, fw);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPlayerInShulker(Player player) {
        this.inShulker.add(player);
    }

    public void removePlayerInShulker(Player player) {
        this.inShulker.remove(player);
    }

    public List<Player> getInShulker() {
        return inShulker;
    }

    public void addEnchant(CustomEnchantment enchantment) {
        this.allEnchants.add(enchantment);
    }

    public List<CustomEnchantment> getAllEnchants() {
        return allEnchants;
    }

    public void removeInvseePlayer(Player player) {
        this.invseePlayers.remove(player);
    }

    public Map<Player, Player> getInvseePlayers() {
        return invseePlayers;
    }

    public void addPassivePlayer(Player player) {
        this.passivePlayers.add(player);
    }

    public void removePassivePlayer(Player player) {
        this.passivePlayers.remove(player);
    }

    public List<Player> getPassivePlayers() {
        return passivePlayers;
    }

    public boolean isInvseeEdit() {
        return invseeEdit;
    }

    public void setInvseeEdit(boolean invseeEdit) {
        this.invseeEdit = invseeEdit;
    }

    public void addInvseePlayer(Player sender, Player vicitm) {
        this.invseePlayers.put(sender, vicitm);
    }

    public boolean isInShulker(Player sender) {
        return this.inShulker.contains(sender);
    }

    public LockedChest getChestByLocation(Location loc) {
        for(LockedChest chest : lockedChests) {
            if(chest.getKeyInfo().getLocation().matches(new CustomLocation(loc.getX(), loc.getY(), loc.getZ()))) {
                return chest;
            }
        }
        return null;
    }

    public List<LockedChest> getLockedChests() {
        return this.lockedChests;
    }

    public void addLockedChest(LockedChest chest) {
        this.lockedChests.add(chest);
        this.saveKeyInfos(lockedChests, CHEST_FILE);
    }

    public void removeLockedChest(LockedChest chest) {
        this.lockedChests.remove(chest);
    }

    public CustomKey getKeyById(String playerUuid, int keyId, boolean duplicate) {
        for(CustomKey key : keys) {
            if(key.getKeyInfo().getOwnerUUID().equals(playerUuid) && key.getKeyInfo().getKeyId() == keyId && key.getKeyInfo().isDuplicate() == duplicate) {
                return key;
            }
        }
        return  null;
    }

    public void addKey(CustomKey key) {
        if(!keys.contains(key)) {
            this.keys.add(key);
            this.saveKeyInfos(keys, KEY_FILE);
        }
    }

    public List<CustomKey> getKeys() {
        return keys;
    }

    public CustomKey getIfKey(ItemStack item, Player player) {
        if(item.getType() == Material.TRIPWIRE_HOOK && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            String lore = item.getItemMeta().getLore().toString();
            int id = Integer.parseInt(lore.substring(9, 10));
            CustomKey key = getKeyById(player.getUniqueId().toString(), id, lore.contains("Duplicate"));
            return key;
        }
        return null;
    }

    public void removeKey(CustomKey key) {
        this.keys.remove(key);
        saveKeyInfos(keys, KEY_FILE);
    }
}
