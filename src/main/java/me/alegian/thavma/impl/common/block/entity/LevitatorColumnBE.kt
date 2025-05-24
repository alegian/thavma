package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.LEVITATOR_COLUMN
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class LevitatorColumnBE(pos: BlockPos, blockState: BlockState) :
  BlockEntity(LEVITATOR_COLUMN.get(), pos, blockState) {
  var levitatorY = pos.y
}