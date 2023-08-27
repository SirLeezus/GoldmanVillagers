package lee.code.villagers.listeners;

import lee.code.villagers.Villagers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener {

  private final Villagers villagers;

  public ServerLoadListener(Villagers villagers) {
    this.villagers = villagers;
  }

  @EventHandler
  public void onServerLoad(ServerLoadEvent e) {
    villagers.getVillagerManager().spawnAllVillagers();
  }

}
