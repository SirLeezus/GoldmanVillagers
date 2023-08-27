package lee.code.villagers.nms;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lee.code.villagers.utils.CoreUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

import java.lang.reflect.Field;

public class VillagerNPC extends Villager {

  public VillagerNPC(Location loc, VillagerType villagertype, VillagerProfession villagerProfession, String name) {
    super(EntityType.VILLAGER, ((CraftWorld)loc.getWorld()).getHandle(), villagertype);
    this.setPos(loc.getX(), loc.getY(), loc.getZ());
    this.removeAI();
    this.setNoGravity(true);
    this.setInvulnerable(true);
    this.setCustomName(Component.Serializer.fromJson(CoreUtil.serializeColorComponentJson(name)));
    this.setCustomNameVisible(true);
    this.setVillagerData(this.getVillagerData().setProfession(villagerProfession));
    this.setVillagerData(this.getVillagerData().setType(villagertype));
    this.setAge(1);
    this.setSilent(true);
    this.collides = false;
    this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 5, 100));
    this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
  }

  //stops the server from saving entity to chunk
  @Override
  public boolean save(CompoundTag compoundTag) {
    return false;
  }

  //stops the server from saving entity to chunk
  @Override
  public void load(CompoundTag compoundTag) {

  }

  private void removeAI() {
    try {
      Field availableGoalsField = GoalSelector.class.getDeclaredField("d");
      Field priorityBehaviorsField = Brain.class.getDeclaredField("f");
      Field coreActivityField = Brain.class.getDeclaredField("j");

      availableGoalsField.setAccessible(true);
      priorityBehaviorsField.setAccessible(true);
      coreActivityField.setAccessible(true);

      availableGoalsField.set(this.goalSelector, Sets.newLinkedHashSet());
      availableGoalsField.set(this.targetSelector, Sets.newLinkedHashSet());
      priorityBehaviorsField.set(this.getBrain(), Maps.newTreeMap());
      coreActivityField.set(this.getBrain(), Sets.newHashSet());
    } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException exception) {
      exception.printStackTrace();
    }
  }
}
