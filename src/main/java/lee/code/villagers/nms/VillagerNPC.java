package lee.code.villagers.nms;

import lee.code.villagers.utils.CoreUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

public class VillagerNPC extends Villager {

  public VillagerNPC(Location loc, VillagerType villagertype, VillagerProfession villagerProfession, String name, int level) {
    super(EntityType.VILLAGER, ((CraftWorld)loc.getWorld()).getHandle(), villagertype);
    setPos(loc.getX(), loc.getY(), loc.getZ());
    setNoGravity(true);
    setInvulnerable(true);
    setCustomName(Component.Serializer.fromJson(CoreUtil.serializeColorComponentJson(name)));
    setCustomNameVisible(true);
    setVillagerData(getVillagerData().setProfession(villagerProfession));
    setVillagerData(getVillagerData().setType(villagertype));
    setVillagerData(getVillagerData().setLevel(level));
    setSilent(true);
    collides = false;
    ageLocked = true;
    targetSelector.getAvailableGoals().clear();
    getBrain().removeAllBehaviors();
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 5, 100));
    goalSelector.addGoal(1, new RandomLookAroundGoal(this));
  }

  @Override
  public boolean save(CompoundTag compoundTag) {
    return false;
  }

  @Override
  public void load(CompoundTag compoundTag) {
  }
}
