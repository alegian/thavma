package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.ETERNAL_FLAME
import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.random.Random

class EternalFlameBE(pos: BlockPos, state: BlockState) : BlockEntity(ETERNAL_FLAME.get(), pos, state) {
  companion object {
    fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
      if (!level.isClientSide) return

      val center = pos.center
      val random = Random(pos.asLong() + level.gameTime)
      level.addAlwaysVisibleParticle(
        T7ParticleTypes.ETERNAL_FLAME.get(),
        center.x, center.y, center.z,
        random.nextDouble(-0.04, 0.04),
        random.nextDouble(-0.04, 0.04),
        random.nextDouble(-0.04, 0.04),
      )
    }
  }
}