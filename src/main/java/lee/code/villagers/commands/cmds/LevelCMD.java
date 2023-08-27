package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.managers.VillagerManager;
import lee.code.villagers.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LevelCMD extends SubCommand {

  private final Villagers villagers;

  public LevelCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "level";
  }

  @Override
  public String getDescription() {
    return "Set your selected villager's level.";
  }

  @Override
  public String getSyntax() {
    return "/villager level &f<level>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.level";
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
    if (args.length < 2) {
      player.sendMessage(Lang.USAGE.getComponent(new String[] { getSyntax() }));
      return;
    }
    final VillagerManager villagerManager = villagers.getVillagerManager();
    final UUID uuid = player.getUniqueId();
    if (!villagerManager.hasSelectedVillager(uuid)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_VILLAGER.getComponent(null)));
      return;
    }
    final String levelString = args[1];
    if (!CoreUtil.isPositiveIntNumber(levelString)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_VALUE_INVALID.getComponent(new String[] { levelString } )));
      return;
    }
    int level = Integer.parseInt(levelString);
    if (level > 5) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LEVEL_MAX.getComponent(new String[] { levelString } )));
      return;
    }
    villagerManager.setVillagerLevel(villagerManager.getSelectedVillager(uuid), level);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_LEVEL_SUCCESSFUL.getComponent(new String[] { levelString })));
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
