package lee.code.villagers.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LogBackendType;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lee.code.villagers.Villagers;
import lee.code.villagers.database.tables.VillagerTable;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.SQLException;

public class DatabaseManager {

  private final Villagers villagers;
  private Dao<VillagerTable, String> villagerDao;
  private ConnectionSource connectionSource;

  public DatabaseManager(Villagers villagers) {
        this.villagers = villagers;
  }

  private String getDatabaseURL() {
    //Setup MongoDB
    if (!villagers.getDataFolder().exists()) villagers.getDataFolder().mkdir();
    return "jdbc:sqlite:" + new File(villagers.getDataFolder(), "database.db");
  }

  public void initialize(boolean debug) {
    if (!debug) LoggerFactory.setLogBackendFactory(LogBackendType.NULL);
    try {
      final String databaseURL = getDatabaseURL();
      connectionSource = new JdbcConnectionSource(
        databaseURL,
        "test",
        "test",
        DatabaseTypeUtils.createDatabaseType(databaseURL));
      createOrCacheTables();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void closeConnection() {
    try {
      connectionSource.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createOrCacheTables() throws SQLException {
    final CacheManager cacheManager = villagers.getCacheManager();

    //Villager data
    TableUtils.createTableIfNotExists(connectionSource, VillagerTable.class);
    villagerDao = DaoManager.createDao(connectionSource, VillagerTable.class);

    int highestID = 1;
    for (VillagerTable villagerTable : villagerDao.queryForAll()) {
      cacheManager.getCacheVillagers().setVillagerTable(villagerTable);
      if (villagerTable.getId() > highestID) highestID = villagerTable.getId();
    }
    cacheManager.getCacheVillagers().getNextID().set(highestID);
  }

  public synchronized void createVillagerTable(VillagerTable villagerTable) {
    Bukkit.getAsyncScheduler().runNow(villagers, scheduledTask -> {
      try {
        villagerDao.createIfNotExists(villagerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public synchronized void updateVillagerTable(VillagerTable villagerTable) {
    Bukkit.getAsyncScheduler().runNow(villagers, scheduledTask -> {
      try {
        villagerDao.update(villagerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public synchronized void deleteVillagerTable(VillagerTable villagerTable) {
    Bukkit.getAsyncScheduler().runNow(villagers, scheduledTask -> {
      try {
        villagerDao.delete(villagerTable);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }
}
