package lee.code.villagers.commands.cmds;

import lee.code.villagers.Villagers;
import lee.code.villagers.commands.SubCommand;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.managers.VillagerManager;
import lee.code.villagers.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfessionCMD extends SubCommand {

  private final Villagers villagers;

  public ProfessionCMD(Villagers villagers) {
    this.villagers = villagers;
  }

  @Override
  public String getName() {
    return "profession";
  }

  @Override
  public String getDescription() {
    return "Change selected villager's profession.";
  }

  @Override
  public String getSyntax() {
    return "/villager profession &f<profession>";
  }

  @Override
  public String getPermission() {
    return "villagers.command.profession";
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
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_SELECTED.getComponent(null)));
      return;
    }
    final String professionString = args[1].toUpperCase();
    if (!villagers.getData().getVillagerProfessions().contains(professionString)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_PROFESSION_INVALID.getComponent(new String[] { professionString })));
      return;
    }
    final NPCProfession profession = NPCProfession.valueOf(professionString);
    villagerManager.setVillagerProfession(villagerManager.getSelectedVillager(uuid), profession);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_PROFESSION_SUCCESSFUL.getComponent(new String[] { CoreUtil.capitalize(professionString) })));
  }

  @Override
  public void performConsole(CommandSender console, String[] args) {
    console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
  }

  @Override
  public void performSender(CommandSender sender, String[] args) { }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 2) return StringUtil.copyPartialMatches(args[1], villagers.getData().getVillagerProfessions(), new ArrayList<>());
    return new ArrayList<>();
  }
}
