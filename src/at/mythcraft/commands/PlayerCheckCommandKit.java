package at.mythcraft.commands;

import at.mythcraft.player.CustomPlayer;
import at.mythcraft.main.Model;
import at.mythcraft.player.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerCheckCommandKit implements CommandExecutor {

    private Model model;

    public PlayerCheckCommandKit(Model model) {
        this.model = model;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        String message = "";
        if(commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            // OPERATOR ONLY COMMANDS
            if(sender.isOp()) {
                if(s.equals("role")) {
                    if(strings.length == 2) {
                        message = setPlayerRole(strings, sender);
                    }
                    sender.sendMessage(message);
                }
                if(s.equals("invsee")) {
                    if(strings.length == 1) {
                        Player player = getUserIfExists(sender, strings[0]);
                        if(player != null) {
                            viewInventory(sender, player, player.getInventory(), 45);
                        }
                    }
                }
                if(s.equals("enderchest")) {
                    if(strings.length == 1) {
                        Player player = getUserIfExists(sender, strings[0]);
                        if(player != null) {
                            viewInventory(sender, player, player.getEnderChest(), 27);
                        }
                    }
                }
            }
            // PUBLIC COMMANDS
            if(s.equals("overworld")) {
                message = listPlayersInWorld(sender,0);
                sender.sendMessage(message);
            }
            if(s.equals("nether")) {
                message = listPlayersInWorld(sender,1);
                sender.sendMessage(message);
            }
            if(s.equals("end")) {
                message = listPlayersInWorld(sender,2);
                sender.sendMessage(message);
            }
        }
        return true;
    }

    private void viewInventory(Player sender, Player player, Inventory i, int size) {
        Inventory inv = Bukkit.createInventory(null, 45, String.format("%s's %s", player.getDisplayName(), size == 27 ? "Enderchest" : "Inventar"));
        inv.setContents(i.getContents());
        if(size == 45) {
            inv.setItem(41, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(42, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(43, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            inv.setItem(44, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        }
        else {
            for(int j = 27; j < 45; j++) {
                inv.setItem(j, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            }
        }
        sender.openInventory(inv);
        model.addInvseePlayer(sender, player);
    }

    private String listPlayersInWorld(Player sender, int index) {
        String msg = null;
        int playerCount = 0;
        String worldName = index == 0 ? "world" : index == 1 ? "world_nether" : "world_the_end";
        StringBuffer pString = new StringBuffer();
        List<Player> players = sender.getServer().getWorld(worldName).getPlayers();
        for(Player p : players) {
            CustomPlayer customPlayer = model.getCustomPlayer(p);
            pString.append(customPlayer.getPlayerInfo().getChatColor() + p.getDisplayName() + (playerCount++ == players.size() - 1 ? "" : ", "));
        }
        return ChatColor.AQUA + String.format("Es befinden sich %d Spieler %s!\n%s", playerCount, index == 0 ? "in der Overworld" : index == 1 ? "im Nether" : "im End", pString.toString());
    }

    private String setPlayerRole(String[] strings, Player sender) {
        String msg;
        String username = strings[0];
        String role = strings[1];
        Player player = getUserIfExists(sender, username);
        if(player != null) {
            CustomPlayer customPlayer = model.getCustomPlayer(player);
            Role newRole = null;
            if(role.equalsIgnoreCase(Role.DONATOR.toString())) {
                newRole = Role.DONATOR;
            }
            else if(role.equalsIgnoreCase(Role.ADMIN.toString())) {
                newRole = Role.ADMIN;
            }
            else if(role.equalsIgnoreCase(Role.VIP.toString())) {
                newRole = Role.VIP;
            }
            else if(role.equalsIgnoreCase(Role.MEMBER.toString())) {
                newRole = Role.MEMBER;
            }
            if(newRole != null) {
                customPlayer.getPlayerInfo().setRole(newRole);
                model.savePlayerInfo(customPlayer.getPlayerInfo());
                msg = ChatColor.GREEN + String.format("%s wurde erfolgreich auf %s gesetzt!", username, role.toUpperCase());
                player.sendMessage(ChatColor.AQUA + "Du bist nun " + customPlayer.getPlayerInfo().getChatColor() + role.toUpperCase());
            }
            else {
                msg = ChatColor.RED + String.format("ERROR: %s ist keine gültige Rolle!\nVersuche MEMBER, ADMIN, VIP oder DONATOR!", role.toUpperCase());
            }
        }
        else {
            msg = ChatColor.RED + String.format("ERROR: Nutzer \"%s\" nicht gefunden", username);
        }
        return msg;
    }

    private Player getUserIfExists(Player sender, String username) {
        for(World world : sender.getServer().getWorlds()) {
            for(Player p : world.getPlayers()) {
                if(p.getDisplayName().equals(username)) {
                    return p;
                }
            }
        }
        return null;
    }
}
