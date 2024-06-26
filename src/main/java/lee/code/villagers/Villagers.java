package lee.code.villagers;

import com.mojang.brigadier.tree.LiteralCommandNode;
import lee.code.villagers.commands.CommandManager;
import lee.code.villagers.commands.TabCompletion;
import lee.code.villagers.database.CacheManager;
import lee.code.villagers.database.DatabaseManager;
import lee.code.villagers.listeners.DamageListener;
import lee.code.villagers.listeners.InteractListener;
import lee.code.villagers.listeners.ServerLoadListener;
import lee.code.villagers.managers.VillagerManager;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Villagers extends JavaPlugin {

  @Getter private CommandManager commandManager;
  @Getter private VillagerManager villagerManager;
  @Getter private CacheManager cacheManager;
  @Getter private Data data;
  private DatabaseManager databaseManager;

  @Override
  public void onEnable() {
    this.data = new Data();
    this.databaseManager = new DatabaseManager(this);
    this.cacheManager = new CacheManager(this, databaseManager);
    this.villagerManager = new VillagerManager(this);
    this.commandManager = new CommandManager(this);
    databaseManager.initialize(false);
    registerCommands();
    registerListeners();
  }

  @Override
  public void onDisable() {
    databaseManager.closeConnection();
    villagerManager.removeAllVillagers();
  }

  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    getServer().getPluginManager().registerEvents(new InteractListener(this), this);
    getServer().getPluginManager().registerEvents(new ServerLoadListener(this), this);
  }

  private void registerCommands() {
    getCommand("villager").setExecutor(commandManager);
    getCommand("villager").setTabCompleter(new TabCompletion(commandManager));
    loadCommodoreData();
  }

  private void loadCommodoreData() {
    try {
      final LiteralCommandNode<?> towns = CommodoreFileReader.INSTANCE.parse(getResource("villager.commodore"));
      CommodoreProvider.getCommodore(this).register(getCommand("villager"), towns);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
