package at.mythcraft.listeners;

import at.mythcraft.player.CustomPlayer;
import at.mythcraft.main.Model;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private Model model;

    public ChatListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onPlayerChatMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        // --------------------
        CustomPlayer player = model.getCustomPlayer(e.getPlayer());
        String name = player.getPlayerInfo().getNickname();
        ChatColor color = player.getPlayerInfo().getChatColor();
        String message = e.getMessage();
        Bukkit.broadcastMessage(color + String.format("%s: ", name) + ChatColor.WHITE + message);

        if(message.equals("debug")) {
            player.getPlayerInfo().setNickname("MrMyth");
            model.savePlayerInfo(player.getPlayerInfo());
        }
    }
}
