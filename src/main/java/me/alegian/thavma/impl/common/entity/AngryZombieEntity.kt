package me.alegian.thavma.impl.common.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.level.Level

open class AngryZombieEntity(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
  companion object {
    fun createAttributes(): AttributeSupplier {
      return createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 40.0)
        .add(Attributes.FOLLOW_RANGE, 35.0)
        .add(Attributes.MOVEMENT_SPEED, 0.23)
        .add(Attributes.ATTACK_DAMAGE, 3.5)
        .add(Attributes.ARMOR, 2.0)
        .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
        .build()
    }
  }

  override fun getBaseExperienceReward(): Int {
    return super.getBaseExperienceReward() * 2
  }
}
