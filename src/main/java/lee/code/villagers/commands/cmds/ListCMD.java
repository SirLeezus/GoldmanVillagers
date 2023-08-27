package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.managers.VillagerManager;
import lee.code.villagers.utils.CoreUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCMD extends SubCommand {

  private final Villagers villagers;

  public ListCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String getDescription() {
    return "List of all villagers.";
  }

  @Override
  public String getSyntax() {
    return "/villager list &f<page>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.list";
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
    final VillagerManager villagerManager = villagers.getVillagerManager();
    final List<Integer> villagers = new ArrayList<>(villagerManager.getAllVillagers());
    if (villagers.isEmpty()) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LIST_NONE.getComponent(null)));
      return;
    }
    int index;
    final int maxDisplayed = 10;
    int page = 0;
    if (args.length > 1) {
      if (CoreUtil.isPositiveIntNumber(args[1])) page = Integer.parseInt(args[1]);
    }
    int position = page * maxDisplayed + 1;
    final ArrayList<Component> lines = new ArrayList<>();
    lines.add(Lang.COMMAND_LIST_TITLE.getComponent(null));
    lines.add(Component.text(" "));

    for (int i = 0; i < maxDisplayed; i++) {
      index = maxDisplayed * page + i;
      if (index >= villagers.size()) break;
      final int id = villagers.get(index);
      lines.add(Lang.COMMAND_LIST_LINE.getComponent(new String[] { String.valueOf(position), villagerManager.getVillagerName(id), String.valueOf(id) }));
      position++;
    }

    if (lines.size() == 2) return;
    lines.add(Component.text(" "));
    lines.add(CoreUtil.createPageSelectionComponent("/villager list", page));
    for (Component line : lines) player.sendMessage(line);
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
