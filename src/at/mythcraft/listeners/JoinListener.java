package at.mythcraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.concurrent.ThreadLocalRandom;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        Server server = player.getServer();
        if(e.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            System.out.println("----------------TRIGGERED KICK EVENT----------------");
            if(player.getName().equals("MythicalAZ") || player.getName().equals("ThaaDodo")) {
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Einer der Admins möchte joinen, doch der Server ist voll!");
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Ein zufälliger Spieler wird ausgelost und gekickt, sorry!");
                kickRandomPlayer(server);
                e.allow();
            }
        }
    }

    private void kickRandomPlayer(Server server) {
        int random = server.getOnlinePlayers().size() > 0 ? ThreadLocalRandom.current().nextInt(0, server.getOnlinePlayers().size() + 1) : 0;
        int count = 0;
        for(Player p : server.getOnlinePlayers()) {
            if(count++ == random) {
                p.kickPlayer(ChatColor.RED + "Sorry! Du wurdest zufällig gekickt um Platz für einen Admin zu machen!");
            }
        }
    }
}
