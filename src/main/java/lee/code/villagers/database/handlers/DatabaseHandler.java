package lee.code.villagers.database.handlers;

import lee.code.villagers.database.DatabaseManager;
import lee.code.villagers.database.tables.VillagerTable;

public class DatabaseHandler {

  private final DatabaseManager databaseManager;

  public DatabaseHandler(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public void createVillagerDatabase(VillagerTable villagerTable) {
    databaseManager.createVillagerTable(villagerTable);
  }

  public void updateVillagerDatabase(VillagerTable villagerTable) {
    databaseManager.updateVillagerTable(villagerTable);
  }

  public void deleteVillagerDatabase(VillagerTable villagerTable) {
    databaseManager.deleteVillagerTable(villagerTable);
  }
}
