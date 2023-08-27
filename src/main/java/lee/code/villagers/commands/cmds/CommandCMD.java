package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.enums.NPCCommandType;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.managers.VillagerManager;
import lee.code.villagers.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCMD extends SubCommand {

  private final Villagers villagers;

  public CommandCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "command";
  }

  @Override
  public String getDescription() {
    return "Change selected villager's command.";
  }

  @Override
  public String getSyntax() {
    return "/villager command &f<type> <command>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.command";
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
    if (args.length < 3) {
      player.sendMessage(Lang.USAGE.getComponent(new String[] { getSyntax() }));
      return;
    }
    final VillagerManager villagerManager = villagers.getVillagerManager();
    final UUID uuid = player.getUniqueId();
    if (!villagerManager.hasSelectedVillager(uuid)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_VILLAGER.getComponent(null)));
      return;
    }

    final String commandTypeString = args[1].toUpperCase();
    if (!villagers.getData().getCommandTypes().contains(commandTypeString)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_TYPE_INVALID.getComponent(new String[] { commandTypeString })));
      return;
    }
    final NPCCommandType commandType = NPCCommandType.valueOf(commandTypeString);

    final String commandString = CoreUtil.buildStringFromArgs(args, 2);
    if (commandString.length() < 2 || commandString.charAt(0) != '/') {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_COMMAND_INVALID.getComponent(new String[] { commandString })));
      return;
    }
    villagerManager.setVillagerCommand(villagerManager.getSelectedVillager(uuid), commandType, commandString);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_COMMAND_SUCCESSFUL.getComponent(new String[] { commandString })));
  }

  @Override
  public void performConsole(CommandSender console, String[] args) {
    console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
  }

  @Override
  public void performSender(CommandSender sender, String[] args) { }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 2) return StringUtil.copyPartialMatches(args[1], villagers.getData().getCommandTypes(), new ArrayList<>());
    return new ArrayList<>();
  }
}
