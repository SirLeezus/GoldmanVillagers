package lee.code.villagers.listeners;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import lee.code.villagers.Villagers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

  private final Villagers villagers;

  public DamageListener(Villagers villagers) {
    this.villagers = villagers;
  }

  @EventHandler
  public void onPlayerDamageVillager(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Villager) {
      final int id = villagers.getVillagerManager().getVillagerID(e.getEntity());
      if (id == 0) return;
      e.setCancelled(true);
      if (e.getDamager() instanceof Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1, 1);
      }
    }
  }

  @EventHandler
  public void onDamageVillager(EntityDamageEvent e) {
    if (e.getEntity() instanceof Villager) {
      final int id = villagers.getVillagerManager().getVillagerID(e.getEntity());
      if (id == 0) return;
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onZapVillager(EntityZapEvent e) {
    if (e.getEntity() instanceof Villager) {
      final int id = villagers.getVillagerManager().getVillagerID(e.getEntity());
      if (id == 0) return;
      e.setCancelled(true);
    }
  }
}
