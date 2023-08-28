package lee.code.villagers.commands;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lee.code.villagers.Villagers;
import lee.code.villagers.commands.cmds.*;
import lee.code.villagers.lang.Lang;
import lee.code.villagers.utils.CoreUtil;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager implements CommandExecutor {

  private final ConcurrentHashMap<String, SubCommand> subCommands = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<UUID, ScheduledTask> asyncTasks = new ConcurrentHashMap<>();
  private final Object synchronizedThreadLock = new Object();
  private final Villagers villagers;

  public CommandManager(Villagers villagers) {
    this.villagers = villagers;
    storeSubCommands();
  }

  private void storeSubCommands() {
    storeSubCommand(new CreateCMD(villagers));
    storeSubCommand(new NameCMD(villagers));
    storeSubCommand(new SelectCMD(villagers));
    storeSubCommand(new ProfessionCMD(villagers));
    storeSubCommand(new TypeCMD(villagers));
    storeSubCommand(new CommandCMD(villagers));
    storeSubCommand(new LocationCMD(villagers));
    storeSubCommand(new TeleportCMD(villagers));
    storeSubCommand(new ListCMD(villagers));
    storeSubCommand(new LevelCMD(villagers));
    storeSubCommand(new DeleteCMD(villagers));
    storeSubCommand(new HelpCMD(villagers));
  }

  private void storeSubCommand(SubCommand subCommand) {
    subCommands.put(subCommand.getName(), subCommand);
  }

  public SubCommand getSubCommand(String command) {
    return subCommands.get(command);
  }

  public List<SubCommand> getSubCommands() {
    return new ArrayList<>(subCommands.values());
  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
    if (sender instanceof Player player) {
      if (args.length > 0) {
        if (subCommands.containsKey(args[0].toLowerCase())) {
          final SubCommand subCommand = getSubCommand(args[0].toLowerCase());
          if (!player.hasPermission(subCommand.getPermission())) {
            player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_PERMISSION.getComponent(null)));
            return true;
          }
          if (subCommand.performAsync()) performAsync(player, subCommand, args);
          else subCommand.perform(player, args);
          return true;
        }
      }
    } else if (args.length > 0) {
      if (subCommands.containsKey(args[0].toLowerCase())) {
        final SubCommand subCommand = getSubCommand(args[0].toLowerCase());
        if (subCommand.performAsync()) performAsync(sender, subCommand, args);
        else subCommand.performConsole(sender, args);
        return true;
      }
    }
    sendHelpMessage(sender);
    return true;
  }

  public void performAsync(CommandSender sender, SubCommand subCommand, String[] args) {
    if (sender instanceof Player player) {
      final UUID uuid = player.getUniqueId();
      if (asyncTasks.containsKey(uuid)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_ONE_COMMAND_AT_A_TIME.getComponent(null)));
        return;
      }
      if (subCommand.performAsyncSynchronized()) {
        synchronized (synchronizedThreadLock) {
          performSubCommandAsync(player, uuid, subCommand, args);
        }
      } else {
        performSubCommandAsync(player, uuid, subCommand, args);
      }
    } else if (sender instanceof ConsoleCommandSender console) {
      Bukkit.getAsyncScheduler().runNow(villagers, scheduledTask -> subCommand.performConsole(console, args));
    }
  }

  private void performSubCommandAsync(Player player, UUID uuid, SubCommand subCommand, String[] args) {
    asyncTasks.put(uuid, Bukkit.getAsyncScheduler().runNow(villagers, scheduledTask -> {
      try {
        subCommand.perform(player, args);
      } finally {
        asyncTasks.remove(uuid);
      }
    }));
  }

  public void sendHelpMessage(CommandSender sender) {
    int number = 1;
    final Map<SubCommand, String> commands = new HashMap<>();
    for (SubCommand subCommand : getSubCommands()) commands.put(subCommand, subCommand.getName());
    final Map<SubCommand, String> sortedCommands = CoreUtil.sortByValue(commands, Comparator.naturalOrder());
    final List<Component> lines = new ArrayList<>();
    lines.add(Lang.COMMAND_HELP_DIVIDER.getComponent(null));
    lines.add(Lang.COMMAND_HELP_TITLE.getComponent(null));
    lines.add(Component.text(""));

    for (SubCommand subCommand : sortedCommands.keySet()) {
      if (sender.hasPermission(subCommand.getPermission())) {
        final Component helpSubCommand = Lang.COMMAND_HELP_SUB_COMMAND.getComponent(new String[] { String.valueOf(number), subCommand.getSyntax() })
          .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, CoreUtil.getTextBeforeCharacter(subCommand.getSyntax(), '&')))
          .hoverEvent(Lang.COMMAND_HELP_SUB_COMMAND_HOVER.getComponent(new String[] { subCommand.getDescription() }));
        lines.add(helpSubCommand);
        number++;
      }
    }

    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_HELP_DIVIDER.getComponent(null));
    for (Component line : lines) sender.sendMessage(line);
  }
}
