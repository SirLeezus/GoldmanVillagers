package lee.code.villagers.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.npc.VillagerProfession;

@AllArgsConstructor
public enum NPCProfession {

  NONE(VillagerProfession.NONE),
  ARMORER(VillagerProfession.ARMORER),
  BUTCHER(VillagerProfession.BUTCHER),
  CARTOGRAPHER(VillagerProfession.CARTOGRAPHER),
  CLERIC(VillagerProfession.CLERIC),
  FARMER(VillagerProfession.FARMER),
  FISHERMAN(VillagerProfession.FISHERMAN),
  FLETCHER(VillagerProfession.FLETCHER),
  LEATHERWORKER(VillagerProfession.LEATHERWORKER),
  LIBRARIAN(VillagerProfession.LIBRARIAN),
  MASON(VillagerProfession.MASON),
  NITWIT(VillagerProfession.NITWIT),
  SHEPHERD(VillagerProfession.SHEPHERD),
  TOOLSMITH(VillagerProfession.TOOLSMITH),
  WEAPONSMITH(VillagerProfession.WEAPONSMITH),

  ;

  @Getter private final VillagerProfession profession;
}
