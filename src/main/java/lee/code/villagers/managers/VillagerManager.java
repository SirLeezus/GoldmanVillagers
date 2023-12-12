package lee.code.villagers.managers;

import lee.code.villagers.Villagers;
import lee.code.villagers.database.cache.CacheVillagers;
import lee.code.villagers.enums.NPCCommandType;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.enums.NPCType;
import lee.code.villagers.nms.VillagerNPC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VillagerManager {
  private final Villagers villagers;
  private final ConcurrentHashMap<UUID, Integer> selectedVillager = new ConcurrentHashMap<>();

  public VillagerManager(Villagers villagers) {
    this.villagers = villagers;
  }

  private void removeSelectedVillager(UUID uuid) {
    selectedVillager.remove(uuid);
  }

  private void setSelectedVillager(UUID uuid, int id) {
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

  public Location getVillagerLocation(int id) {
    return villagers.getCacheManager().getCacheVillagers().getLocation(id);
  }

  public String getVillagerName(int id) {
    return villagers.getCacheManager().getCacheVillagers().getName(id);
  }

  public void setVillagerName(int id, String name) {
    villagers.getCacheManager().getCacheVillagers().setName(id, name);
    respawnVillager(id);
  }

  public void setVillagerProfession(int id, NPCProfession profession) {
    villagers.getCacheManager().getCacheVillagers().setProfession(id, profession);
    respawnVillager(id);
  }

  public void setVillagerType(int id, NPCType type) {
    villagers.getCacheManager().getCacheVillagers().setType(id, type);
    respawnVillager(id);
  }

  public void setVillagerCommand(int id, NPCCommandType npcCommandType, String command) {
    villagers.getCacheManager().getCacheVillagers().setCommand(id, npcCommandType, command);
    respawnVillager(id);
  }

  public void setVillagerLocation(int id, Location location) {
    removeVillager(id);
    villagers.getCacheManager().getCacheVillagers().setLocation(id, location);
    spawnVillager(id);
  }

  public void setVillagerLevel(int id, int level) {
    villagers.getCacheManager().getCacheVillagers().setLevel(id, level);
    respawnVillager(id);
  }

  public void deleteVillager(UUID uuid, int id) {
    removeSelectedVillager(uuid);
    removeVillager(id);
    villagers.getCacheManager().getCacheVillagers().deleteVillager(id);
  }

  private void respawnVillager(int id) {
    removeVillager(id);
    spawnVillager(id);
  }

  private void removeVillager(int id) {
    final Location location = villagers.getCacheManager().getCacheVillagers().getLocation(id);
    location.getWorld().getChunkAtAsync(location, true).thenAccept(chunk -> {
      for (Entity entity : chunk.getEntities()) {
        if (getVillagerID(entity) == id) {
          entity.getScheduler().run(villagers, task -> {
            removeForceChunkLoad(chunk, id);
            entity.remove();
          }, null);
          return;
        }
      }
    }).join();
  }

  private void spawnVillager(int id) {
    final CacheVillagers cacheVillagers = villagers.getCacheManager().getCacheVillagers();
    final Location location = cacheVillagers.getLocation(id);
    location.getWorld().getChunkAtAsync(location, true).thenAccept(chunk -> {
      final VillagerNPC villager = new VillagerNPC(location, cacheVillagers.getType(id).getType(), cacheVillagers.getProfession(id).getProfession(), cacheVillagers.getName(id), cacheVillagers.getLevel(id));
      final CraftEntity entity = villager.getBukkitEntity();
      storeVillagerMetaData(entity, id);
      entity.spawnAt(location, CreatureSpawnEvent.SpawnReason.CUSTOM);
      chunk.setForceLoaded(true);
    });
  }

  private void removeForceChunkLoad(Chunk chunk, int id) {
    for (Entity entity : chunk.getEntities()) {
      final int targetID = getVillagerID(entity);
      if (targetID != 0 && targetID != id) return;
    }
    chunk.setForceLoaded(false);
  }

  public Set<Integer> getAllVillagers() {
    return villagers.getCacheManager().getCacheVillagers().getAllVillagers();
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

  public void removeAllVillagers() {
    for (int id : villagers.getCacheManager().getCacheVillagers().getAllVillagers()) {
      final Chunk chunk = villagers.getCacheManager().getCacheVillagers().getLocation(id).getChunk();
      for (Entity entity : chunk.getEntities()) {
        if (getVillagerID(entity) == id) {
          entity.remove();
          return;
        }
      }
    }
  }

  public int selectVillager(Player player) {
    for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
      if (entity instanceof LivingEntity) {
        if (isLookingAtEntity(player, (LivingEntity) entity)) {
          final int id = getVillagerID(entity);
          if (id == 0) continue;
          setSelectedVillager(player.getUniqueId(), id);
          return id;
        }
      }
    }
    return 0;
  }

  private boolean isLookingAtEntity(Player player, LivingEntity entity) {
    final Location eye = player.getEyeLocation();
    final Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
    double dot = toEntity.normalize().dot(eye.getDirection());
    return dot > 0.99D;
  }
}
