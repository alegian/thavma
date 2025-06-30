package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.LevitatorColumnBE
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.LEVITATOR_COLUMN
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import kotlin.math.max

class LevitatorColumnBlock : Block(
  Properties.of()
    .noCollission()
    .replaceable()
    .noLootTable()
    .pushReaction(PushReaction.DESTROY)
    .sound(SoundType.EMPTY)
), EntityBlock {
  override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
    val oldSpeed = entity.deltaMovement
    val newSpeedY = if (entity.isCrouching)
      max(-0.4, oldSpeed.y - 0.04)
    else
      max(0.4, oldSpeed.y + 0.04)

    entity.setDeltaMovement(oldSpeed.x, newSpeedY, oldSpeed.z)
    entity.setData(T7Attachments.LEVITATES, true)
  }

  override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    level.getBE(pos, LEVITATOR_COLUMN.get())?.levitatorY?.let{
      setAndExpand(level, pos, it)
    }
  }


  override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
    val belowBlock = level.getBlockState(pos.below()).block
    return belowBlock is ArcaneLevitatorBlock || belowBlock is LevitatorColumnBlock
  }

  /**
   * interesting cases:
   * 1. the column can no longer survive here (the block below changed)
   * 2. the block above caused the update, and it could now become a column block too
   */
  override fun updateShape(state: BlockState, direction: Direction, neighborState: BlockState, level: LevelAccessor, pos: BlockPos, neighborPos: BlockPos): BlockState {
    if (
      !state.canSurvive(level, pos) ||
      direction == Direction.UP && neighborState.block !is LevitatorColumnBlock && canExistIn(neighborState)
    ) {
      level.scheduleTick(pos, this, 5)
    }

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
  }

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) =
    Shapes.empty()

  override fun getRenderShape(state: BlockState) = RenderShape.INVISIBLE

  override fun newBlockEntity(pos: BlockPos, state: BlockState) = LevitatorColumnBE(pos, state)

  companion object {
    fun canExistIn(blockState: BlockState) =
      blockState.block is LevitatorColumnBlock || blockState.isAir

    fun setAndExpand(level: ServerLevel, pos: BlockPos, levitatorY: Int) {
      val currentState = level.getBlockState(pos)

      if (canExistIn(currentState)) {
        var newState = T7Blocks.LEVITATOR_COLUMN.get().defaultBlockState()
        if (!currentState.canSurvive(level, pos)) newState = Blocks.AIR.defaultBlockState()

        level.setBlock(pos, newState, UPDATE_CLIENTS)
        val newPos = pos.mutable().move(Direction.UP)

        while (canExistIn(level.getBlockState(newPos)) && newPos.y - levitatorY <= 16) {
          level.setBlock(newPos, newState, UPDATE_CLIENTS)

          level.getBE(newPos, LEVITATOR_COLUMN.get())?.let {
            it.levitatorY = levitatorY
          }

          newPos.move(Direction.UP)
        }
      }
    }
  }
}

