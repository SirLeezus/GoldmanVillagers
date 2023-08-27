package lee.code.villagers.lang;

import lee.code.villagers.utils.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&d&lVillagers &6➔ "),
  USAGE("&6&lUsage: &e{0}"),
  COMMAND_CREATE_SUCCESSFUL("&aYou successfully created the villager &f{0}&a!"),
  COMMAND_SELECT_SUCCESSFUL("&aYou successfully selected the villager &f{0}&a!"),
  COMMAND_NAME_SUCCESSFUL("&aYou successfully updated your selected villager's name to &f{0}&a!"),
  COMMAND_PROFESSION_SUCCESSFUL("&aYou successfully updated your selected villager's profession to &3{0}&a!"),
  COMMAND_TYPE_SUCCESSFUL("&aYou successfully updated your selected villager's type to &3{0}&a!"),
  COMMAND_COMMAND_SUCCESSFUL("&aYou successfully updated your selected villager's command to &3{0}&a!"),
  COMMAND_HELP_DIVIDER("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
  COMMAND_HELP_TITLE("                      &2-== &6&l&nVillager Help&r &2==-"),
  COMMAND_HELP_SUB_COMMAND("&3{0}&b. &e{1}"),
  COMMAND_HELP_SUB_COMMAND_HOVER("&6{0}"),
  ERROR_NOT_CONSOLE_COMMAND("&cThis command does not work in console."),
  ERROR_NO_PERMISSION("&cYou sadly do not have permission for this."),
  ERROR_SELECT_NO_VILLAGER("&cCould not find a villager."),
  ERROR_NOT_SELECTED("&cYou currently don't have a selected villager."),
  ERROR_PROFESSION_INVALID("&cThe input &3{0} &cis not a valid villager profession."),
  ERROR_COMMAND_INVALID("&cThe input &3{0} &cis not a valid villager command."),
  ERROR_COMMAND_TYPE_INVALID("&cThe input &3{0} &cis not a valid villager type."),
  ERROR_TYPE_INVALID("&cThe input &3{0} &cis not a valid villager type."),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ;
  @Getter private final String string;

  public String getString(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return value;
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return value;
  }

  public Component getComponent(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return CoreUtil.parseColorComponent(value);
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return CoreUtil.parseColorComponent(value);
  }
}
