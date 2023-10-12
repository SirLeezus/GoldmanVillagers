package lee.code.villagers.listeners;

import lee.code.villagers.Villagers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractListener implements Listener {

  private final Villagers villagers;

  public InteractListener(Villagers villagers) {
    this.villagers = villagers;
  }

  @EventHandler
  public void onInteractAtVillager(PlayerInteractAtEntityEvent e) {
    if (!(e.getRightClicked() instanceof Villager)) return;
    final int id = villagers.getVillagerManager().getVillagerID(e.getRightClicked());
    if (id == 0) return;
    e.setCancelled(true);
  }

  @EventHandler
  public void onInteractWithVillager(PlayerInteractEntityEvent e) {
    if (!(e.getRightClicked() instanceof Villager)) return;
    final int id = villagers.getVillagerManager().getVillagerID(e.getRightClicked());
    if (id == 0) return;
    e.setCancelled(true);
    final Player player = e.getPlayer();
    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
    villagers.getVillagerManager().runVillagerCommand(player, id);
  }
}
