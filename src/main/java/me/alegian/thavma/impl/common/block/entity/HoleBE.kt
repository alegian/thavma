package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HoleBE(pos: BlockPos, state: BlockState) : BlockEntity(T7BlockEntities.HOLE.get(), pos, state) {
  private var lifetimeTicks = 120
  var originalState: BlockState? = null
  var direction = Direction.NORTH

  fun serverTick() {
    if (lifetimeTicks <= 0) {
      val state = originalState
      if (state != null) {
        level?.setBlock(blockPos, state, Block.UPDATE_CLIENTS)
      } else {
        level?.removeBlock(blockPos, false)
      }
    }
    lifetimeTicks--
  }
}
