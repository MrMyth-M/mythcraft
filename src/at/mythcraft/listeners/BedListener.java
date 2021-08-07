package at.mythcraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedListener implements Listener {

    private int playersSleeping = 0;
    private int limit = 2;
    private boolean slept = true;

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        World world = e.getPlayer().getWorld();
        if(e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            slept = false;
            playersSleeping++;
            limit = world.getPlayers().size() > 1 ? 2 : 1;
            Bukkit.broadcastMessage(ChatColor.AQUA + String.format("%s ist schlafen gegangen! - (%d/%d)", e.getPlayer().getDisplayName(), playersSleeping, limit));
            if(playersSleeping == limit) {
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Gute Nacht!");
                int currentDaytime = (int) world.getTime() % 24000;
                int timeRemaining = (int) world.getTime() + (24000 - currentDaytime);
                world.setTime(timeRemaining);
                slept = true;
                for(Player p : e.getPlayer().getWorld().getPlayers()) {
                    p.setStatistic(Statistic.TIME_SINCE_REST, 0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent e) {
        playersSleeping--;
        if(!slept) {
            Bukkit.broadcastMessage(ChatColor.AQUA + String.format("%s hat sein Bett verlassen! - (%d/2)", e.getPlayer().getDisplayName(), playersSleeping));
        }
    }
}
