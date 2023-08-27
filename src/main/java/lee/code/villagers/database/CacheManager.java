package lee.code.villagers.database;

import lee.code.villagers.Villagers;
import lee.code.villagers.database.cache.CacheVillagers;
import lombok.Getter;

public class CacheManager {
  private final Villagers villagers;
  @Getter private final CacheVillagers cacheVillagers;

  public CacheManager(Villagers villagers, DatabaseManager databaseManager) {
    this.villagers = villagers;
    this.cacheVillagers = new CacheVillagers(databaseManager);
  }
}
