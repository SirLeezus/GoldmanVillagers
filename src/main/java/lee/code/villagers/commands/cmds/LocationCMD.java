package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.managers.VillagerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationCMD extends SubCommand {

  private final Villagers villagers;

  public LocationCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "location";
  }

  @Override
  public String getDescription() {
    return "Change selected villager's location.";
  }

  @Override
  public String getSyntax() {
    return "/villager location";
  }

  @Override
  public String getPermission() {
    return "villagers.command.location";
  }

  @Override
  public boolean performAsync() {
    return true;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return true;
  }

  @Override
  public void perform(Player player, String[] args) {
    final VillagerManager villagerManager = villagers.getVillagerManager();
    final UUID uuid = player.getUniqueId();
    if (!villagerManager.hasSelectedVillager(uuid)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_VILLAGER.getComponent(null)));
      return;
    }
    villagerManager.setVillagerLocation(villagerManager.getSelectedVillager(uuid), player.getLocation());
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_LOCATION_SUCCESSFUL.getComponent(null)));
  }

  @Override
  public void performConsole(CommandSender console, String[] args) {
    console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
  }

  @Override
  public void performSender(CommandSender sender, String[] args) { }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}
