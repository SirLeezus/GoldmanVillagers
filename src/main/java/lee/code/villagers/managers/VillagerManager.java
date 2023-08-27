package lee.code.villagers.managers;

import lee.code.villagers.Villagers;
import lee.code.villagers.database.cache.CacheVillagers;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.enums.NPCType;
import lee.code.villagers.nms.VillagerNPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VillagerManager {

  private final Villagers villagers;
  private final ConcurrentHashMap<UUID, Integer> selectedVillager = new ConcurrentHashMap<>();

  public VillagerManager(Villagers villagers) {
    this.villagers = villagers;
  }

  public void setSelectedVillager(UUID uuid, int id) {
    selectedVillager.put(uuid, id);
  }

  public boolean hasSelectedVillager(UUID uuid) {
    return selectedVillager.containsKey(uuid);
  }

  public int getSelectedVillager(UUID uuid) {
    return selectedVillager.get(uuid);
  }

  public void createVillager(UUID uuid, String name, Location location) {
    final int id = villagers.getCacheManager().getCacheVillagers().createVillager(name, location, NPCType.PLAINS, NPCProfession.NONE);
    setSelectedVillager(uuid, id);
    spawnVillager(id);
  }

  private void spawnVillager(int id) {
    final CacheVillagers cacheVillagers = villagers.getCacheManager().getCacheVillagers();
    final Location location = cacheVillagers.getLocation(id);
    location.getWorld().getChunkAtAsync(location, true).thenAccept(chunk -> {
      final VillagerNPC villager = new VillagerNPC(location, cacheVillagers.getType(id).getType(), cacheVillagers.getProfession(id).getProfession(), cacheVillagers.getName(id));
      final CraftEntity entity = villager.getBukkitEntity();
      storeVillagerMetaData(entity, id);
      entity.spawnAt(location, CreatureSpawnEvent.SpawnReason.CUSTOM);
    });
  }

  public void spawnAllVillagers() {
    for (int id : villagers.getCacheManager().getCacheVillagers().getAllVillagers()) spawnVillager(id);
  }

  private void storeVillagerMetaData(CraftEntity entity, int id) {
    final NamespacedKey key = new NamespacedKey(villagers, "npc");
    final PersistentDataContainer container = entity.getPersistentDataContainer();
    container.set(key, PersistentDataType.INTEGER, id);
  }

  public int getVillagerID(Entity entity) {
    final PersistentDataContainer container = entity.getPersistentDataContainer();
    final NamespacedKey key = new NamespacedKey(villagers, "npc");
    return container.getOrDefault(key, PersistentDataType.INTEGER, 0);
  }

  public void runVillagerCommand(Player player, int id) {
    final CacheVillagers cacheVillagers = villagers.getCacheManager().getCacheVillagers();
    if (!cacheVillagers.hasCommand(id)) return;
    final String command = cacheVillagers.getCommand(id);
    switch (cacheVillagers.getCommandType(id)) {
      case PLAYER -> player.chat(command);
      case CONSOLE -> {
        final String run = command.replaceAll("%player%", player.getName());
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), run);
      }
    }
  }
}
