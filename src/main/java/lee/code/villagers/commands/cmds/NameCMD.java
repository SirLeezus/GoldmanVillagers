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

public class NameCMD extends SubCommand {

  private final Villagers villagers;

  public NameCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "name";
  }

  @Override
  public String getDescription() {
    return "Rename a villager.";
  }

  @Override
  public String getSyntax() {
    return "/villager name &f<name>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.name";
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
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NAME_NOT_SELECTED.getComponent(null)));
      return;
    }
    final String name = CoreUtil.buildStringFromArgs(args, 1);
    villagerManager.setVillagerName(villagerManager.getSelectedVillager(uuid), name);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_NAME_SUCCESSFUL.getComponent(new String[] { name })));
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
