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

public class TeleportCMD extends SubCommand {

  private final Villagers villagers;

  public TeleportCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "teleport";
  }

  @Override
  public String getDescription() {
    return "Teleport to selected villager.";
  }

  @Override
  public String getSyntax() {
    return "/villager teleport";
  }

  @Override
  public String getPermission() {
    return "villagers.command.teleport";
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
    final UUID uuid = player.getUniqueId();
    final int id;
    if (args.length > 1) {
      final String idString = args[1];
      if (!CoreUtil.isPositiveIntNumber(idString)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_VALUE_INVALID.getComponent(new String[] { idString } )));
        return;
      }
      final int targetID = Integer.parseInt(idString);
      if (!villagerManager.getAllVillagers().contains(targetID)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_ID_INVALID.getComponent(new String[] { idString })));
        return;
      }
      id = targetID;
    } else {
      if (!villagerManager.hasSelectedVillager(uuid)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_SELECTED_VILLAGER.getComponent(null)));
        return;
      }
      id = villagerManager.getSelectedVillager(uuid);
    }

    player.teleportAsync(villagerManager.getVillagerLocation(id)).thenAccept(result -> {
      if (result) player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_TELEPORT_SUCCESSFUL.getComponent(new String[] { villagerManager.getVillagerName(id) })));
      else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_TELEPORT_FAILED.getComponent(null)));
    });
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
