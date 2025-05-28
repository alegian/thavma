package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes.VIS
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

/**
 * Helper Entity mainly used for spawning Entity Renderers.
 * Does not tick
 */
abstract class RendererEntity(pLevel: Level, pos: Vec3) : Entity(VIS.get(), pLevel) {
  // position is used to determine if the render section is active, otherwise the entity is culled
  init {
    setPos(pos)
  }

  override fun defineSynchedData(pBuilder: SynchedEntityData.Builder) {
  }

  override fun tick() {
  }
}
