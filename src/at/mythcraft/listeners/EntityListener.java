package at.mythcraft.listeners;

import at.mythcraft.main.Model;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListener implements Listener {

    private final Model model;

    public EntityListener(Model model) {
        this.model = model;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if(model.getPassivePlayers().contains(e.getEntity()) || model.getPassivePlayers().contains(e.getDamager())) {
                e.setCancelled(true);
            }
        }
        else if(e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            if(((Arrow) e.getDamager()).getShooter() instanceof Player) {
                Player shooter = (Player) ((Arrow) e.getDamager()).getShooter();
                if(model.getPassivePlayers().contains(victim) || model.getPassivePlayers().contains(shooter)) {
                    e.setCancelled(true);
                }
            }
        }
        else if(e.getDamager() instanceof Trident && e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            if(((Trident) e.getDamager()).getShooter() instanceof Player) {
                Player shooter = (Player) ((Trident) e.getDamager()).getShooter();
                if(model.getPassivePlayers().contains(victim) || model.getPassivePlayers().contains(shooter)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
