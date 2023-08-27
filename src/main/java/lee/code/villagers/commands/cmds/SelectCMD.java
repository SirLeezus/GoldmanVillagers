package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectCMD extends SubCommand {

  private final Villagers villagers;

  public SelectCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "select";
  }

  @Override
  public String getDescription() {
    return "Select the villager you're looking at.";
  }

  @Override
  public String getSyntax() {
    return "/villager select";
  }

  @Override
  public String getPermission() {
    return "villagers.command.select";
  }

  @Override
  public boolean performAsync() {
    return false;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return false;
  }

  @Override
  public void perform(Player player, String[] args) {
    final int id = villagers.getVillagerManager().selectVillager(player);
    if (id == 0) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SELECT_NO_VILLAGER.getComponent(null)));
      return;
    }
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_SELECT_SUCCESSFUL.getComponent(new String[] { villagers.getCacheManager().getCacheVillagers().getName(id) })));
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
