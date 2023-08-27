package lee.code.villagers.database.cache;

import lee.code.villagers.database.DatabaseManager;
import lee.code.villagers.database.handlers.DatabaseHandler;
import lee.code.villagers.database.tables.VillagerTable;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.enums.NPCType;
import lee.code.villagers.utils.CoreUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheVillagers extends DatabaseHandler {

  @Getter private final AtomicInteger nextID = new AtomicInteger(1);
  private final ConcurrentHashMap<Integer, VillagerTable> villagersCache = new ConcurrentHashMap<>();

  public CacheVillagers(DatabaseManager databaseManager) {
    super(databaseManager);
  }

  public VillagerTable getVillagerTable(int id) {
    return villagersCache.get(id);
  }

  public void setVillagerTable(VillagerTable playerTable) {
    villagersCache.put(playerTable.getId(), playerTable);
  }

  private void deleteVillagerData(VillagerTable villagerTable) {
    deleteVillagerDatabase(villagerTable);
    villagersCache.remove(villagerTable.getId());
  }

  public int createVillager(String name, Location location, NPCType npcType, NPCProfession npcProfession) {
    nextID.addAndGet(1);
    final VillagerTable villagerTable = new VillagerTable(nextID.get(), CoreUtil.serializeLocation(location), name, npcType.name(), npcProfession.name());
    setVillagerTable(villagerTable);
    createVillagerDatabase(villagerTable);
    return nextID.get();
  }

  public void deleteVillager(int id) {
    deleteVillagerData(getVillagerTable(id));
  }

  public Location getLocation(int id) {
    return CoreUtil.parseLocation(getVillagerTable(id).getLocation());
  }

  public String getName(int id) {
    return getVillagerTable(id).getName();
  }

  public NPCProfession getProfession(int id) {
    return NPCProfession.valueOf(getVillagerTable(id).getProfession());
  }

  public NPCType getType(int id) {
    return NPCType.valueOf(getVillagerTable(id).getType());
  }

}
