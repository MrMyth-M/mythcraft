package at.mythcraft.commands;

import at.mythcraft.chestlock.CustomKey;
import at.mythcraft.chestlock.KeyInfo;
import at.mythcraft.main.PluginMain;
import at.mythcraft.player.CustomPlayer;
import at.mythcraft.main.Model;
import at.mythcraft.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandKit implements CommandExecutor {

    private Model model;
    private String message = "";
    private final String ACTIVATED = ChatColor.GREEN + "PVP wurde aktivert!";
    private final String DEACTIVATED = ChatColor.DARK_RED + "PVP wurde deaktiviert!";

    public CommandKit(Model model) {
        this.model = model;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if(sender.isOp()) {
                if(s.equals("broadcast")) {
                    if(strings.length > 0) {
                        String bracket = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "BROADCAST" + ChatColor.GOLD + "] ";
                        String broadcastMessage = "";
                        StringBuffer sb = new StringBuffer();
                        for(int i = 0; i < strings.length; i++) {
                            sb.append(strings[i] + " ");
                        }
                        broadcastMessage = sb.toString();
                        Bukkit.broadcastMessage(bracket + broadcastMessage);
                    }
                    return true;
                }
                if(s.equals("pvp")) {
                    String param = strings.length > 0 ? strings[0] : "";
                    togglePvP(param, sender);
                    return true;
                }
                if(s.equals("nick")) {
                    CustomPlayer player = model.getCustomPlayer(sender);
                    PlayerInfo info = player.getPlayerInfo();
                    String name = strings.length > 0 ? strings[0] : sender.getDisplayName();
                    String oldName = player.getPlayer().getDisplayName();
                    if(name.length() > 2 && !name.equals(info.getNickname())) {
                        info.setNickname(name);
                        model.savePlayerInfo(info);
                        String infoBracket = ChatColor.BLUE + "[NICK] ";
                        message = infoBracket + String.format(info.getChatColor() + oldName + ChatColor.AQUA + " nennt sich nun " + info.getChatColor() + name);
                    }
                    else {
                        message = "ERROR: Ungültiger Name: " + name;
                    }
                    if(!message.contains("ERROR")) {
                        Bukkit.broadcastMessage(message);
                    }
                    else {
                        sender.sendMessage(message);
                    }
                    return true;
                }
                if(s.equals("debug")) {
                    CustomPlayer customPlayer = model.getCustomPlayer(sender);
                    KeyInfo keyInfo = new KeyInfo(customPlayer.getPlayerInfo().incrementKeyCount(), sender.getUniqueId().toString());
                    model.savePlayerInfo(customPlayer.getPlayerInfo());
                    CustomKey customKey = new CustomKey(keyInfo, new ItemStack(Material.TRIPWIRE_HOOK));
                    sender.getInventory().addItem(customKey.getItem());
                    sender.sendMessage("You received a key");
                    model.addKey(customKey);
                    // sender.sendMessage(ChatColor.RED + "This command currently has no usage.");
                    return true;
                }
            }
            if(s.equals("destroykey")) {
                ItemStack item = sender.getInventory().getItemInMainHand();
                if(item.getType() == Material.TRIPWIRE_HOOK) {
                    CustomKey key = model.getIfKey(item, sender);
                    if(key != null) {
                        model.removeKey(key);
                    }
                    sender.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    sender.playSound(sender.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    sender.sendMessage(ChatColor.YELLOW + String.format("Du hast %s zerstört.", key.getKeyInfo().isDuplicate() ? "ein Schlüssel-Duplikat" : "einen Original-Schlüssel"));
                }
            }
            if(s.equals("patch")) {
                sender.sendMessage(ChatColor.DARK_GREEN + PluginMain.latestPatchMessage);
                return true;
            }
            if(s.equals("passive")) {
                if(model.getPassivePlayers().contains(sender)) {
                    model.getPassivePlayers().remove(sender);
                    message = ChatColor.RED + "Du hast den Passiv-Modus deaktiviert.";
                }
                else {
                    model.getPassivePlayers().add(sender);
                    message = ChatColor.GREEN + "Du hast den Passiv-Modus aktiviert.";
                }
                sender.sendMessage(message);
                return true;
            }
        }
        return true;
    }

    private void togglePvP(String param, Player sender) {
        switch(param) {
            case "off":
                sender.getWorld().setPVP(false);
                message = DEACTIVATED;
                break;
            case "on":
                sender.getWorld().setPVP(true);
                message = ACTIVATED;
                break;
            case "":
                sender.getWorld().setPVP(!sender.getWorld().getPVP());
                message = sender.getWorld().getPVP() ? ACTIVATED : DEACTIVATED;
                break;
            default:
                message = ChatColor.RED + "ERROR: Invalid parameter: " + param;
        }
        if(!message.contains("ERROR")) {
            Bukkit.broadcastMessage(message);
        }
        else {
            sender.sendMessage(message);
        }
    }
}
