package lee.code.villagers.database.cache;

import lee.code.villagers.database.DatabaseManager;
import lee.code.villagers.database.handlers.DatabaseHandler;
import lee.code.villagers.database.tables.VillagerTable;
import lee.code.villagers.enums.NPCCommandType;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.enums.NPCType;
import lee.code.villagers.utils.CoreUtil;
import lombok.Getter;
import org.bukkit.Location;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheVillagers extends DatabaseHandler {

  @Getter private final AtomicInteger nextID = new AtomicInteger(0);
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

  public void setLocation(int id, Location location) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setLocation(CoreUtil.serializeLocation(location));
    updateVillagerDatabase(villagerTable);
  }

  public int getLevel(int id) {
    return getVillagerTable(id).getLevel();
  }

  public void setLevel(int id, int level) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setLevel(level);
    updateVillagerDatabase(villagerTable);
  }

  public String getName(int id) {
    return getVillagerTable(id).getName();
  }

  public void setName(int id, String name) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setName(name);
    updateVillagerDatabase(villagerTable);
  }

  public NPCProfession getProfession(int id) {
    return NPCProfession.valueOf(getVillagerTable(id).getProfession());
  }

  public void setProfession(int id, NPCProfession profession) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setProfession(profession.name());
    updateVillagerDatabase(villagerTable);
  }

  public void setType(int id, NPCType type) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setType(type.name());
    updateVillagerDatabase(villagerTable);
  }

  public NPCType getType(int id) {
    return NPCType.valueOf(getVillagerTable(id).getType());
  }

  public String getCommand(int id) {
    return getVillagerTable(id).getCommand();
  }

  public void setCommand(int id, NPCCommandType npcCommandType, String command) {
    final VillagerTable villagerTable = getVillagerTable(id);
    villagerTable.setCommandType(npcCommandType.name());
    villagerTable.setCommand(command);
    updateVillagerDatabase(villagerTable);
  }

  public boolean hasCommand(int id) {
    return getVillagerTable(id).getCommand() != null;
  }

  public NPCCommandType getCommandType(int id) {
    return NPCCommandType.valueOf(getVillagerTable(id).getCommandType());
  }

  public Set<Integer> getAllVillagers() {
    return villagersCache.keySet();
  }

}
