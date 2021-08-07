package at.mythcraft.main;

import at.mythcraft.enchantments.CustomEnchantment;
import at.mythcraft.player.CustomPlayer;
import at.mythcraft.player.PlayerInfo;
import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    public static final String PLAYERINFO_PATH = "plugins/MythCraft/PlayerInfo/";
    public static final String KEY_FILE = "keys.json";
    private final List<CustomPlayer> customPlayers = new ArrayList<>();
    private final List<Player> passivePlayers = new ArrayList<>();
    private final List<CustomEnchantment> allEnchants = new ArrayList<>();

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
}
