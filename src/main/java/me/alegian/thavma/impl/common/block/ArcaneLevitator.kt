package me.alegian.thavma.impl.common.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class ArcaneLevitator : Block(Properties.ofFullCopy(Blocks.STONE)) {
  /**
   * try place columns on place
   */
  override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
    level.scheduleTick(pos, this, 20)
  }

  override fun updateShape(state: BlockState, direction: Direction, neighborState: BlockState, level: LevelAccessor, pos: BlockPos, neighborPos: BlockPos): BlockState {
    if (direction == Direction.UP && LevitatorColumnBlock.canExistIn(neighborState))
      level.scheduleTick(pos, this, 20)

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
  }

  /**
   * columns are placed only by ticking
   */
  override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    LevitatorColumnBlock.setAndExpandUpwards(level, pos.above())
  }
}