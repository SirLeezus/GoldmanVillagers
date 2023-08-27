package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCMD extends SubCommand {

  private final Villagers villagers;

  public HelpCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "All villager commands.";
  }

  @Override
  public String getSyntax() {
    return "/villager help";
  }

  @Override
  public String getPermission() {
    return "villagers.command.help";
  }

  @Override
  public boolean performAsync() {
    return true;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return false;
  }

  @Override
  public void perform(Player player, String[] args) {
    performSender(player, args);
  }

  @Override
  public void performConsole(CommandSender console, String[] args) {
    performSender(console, args);
  }

  @Override
  public void performSender(CommandSender sender, String[] args) {
    villagers.getCommandManager().sendHelpMessage(sender);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}
