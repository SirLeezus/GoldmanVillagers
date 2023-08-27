package lee.code.villagers;

import lee.code.villagers.enums.NPCCommandType;
import lee.code.villagers.enums.NPCProfession;
import lee.code.villagers.enums.NPCType;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

  @Getter private final Set<String> villagerProfessions = ConcurrentHashMap.newKeySet();
  @Getter private final Set<String> villagerTypes = ConcurrentHashMap.newKeySet();
  @Getter private final Set<String> commandTypes = ConcurrentHashMap.newKeySet();

  public Data() {
    loadData();
  }

  private void loadData() {
    for (NPCProfession profession : NPCProfession.values()) villagerProfessions.add(profession.name());
    for (NPCType type : NPCType.values()) villagerTypes.add(type.name());
    for (NPCCommandType commandType : NPCCommandType.values()) commandTypes.add(commandType.name());
  }
}
