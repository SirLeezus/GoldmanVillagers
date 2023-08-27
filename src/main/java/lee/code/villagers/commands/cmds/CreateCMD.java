package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateCMD extends SubCommand {

  private final Villagers villagers;

  public CreateCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "create";
  }

  @Override
  public String getDescription() {
    return "Create a new villager.";
  }

  @Override
  public String getSyntax() {
    return "/villager create &f<name>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.create";
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
    //v create <name>
    if (args.length < 2) {
      player.sendMessage(Lang.USAGE.getComponent(new String[] { getSyntax() }));
      return;
    }
    final String name = CoreUtil.buildStringFromArgs(args, 1);
    villagers.getVillagerManager().createVillager(player.getUniqueId(), name, player.getLocation());
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_CREATE_SUCCESSFUL.getComponent(new String[] { name })));
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
