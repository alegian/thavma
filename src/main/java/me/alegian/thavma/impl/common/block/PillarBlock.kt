package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.PillarBE
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties.MASTER
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.level.material.PushReaction

class PillarBlock : Block(
  Properties.ofFullCopy(Blocks.STONE)
    .noOcclusion()
    .pushReaction(PushReaction.BLOCK)
), EntityBlock {
  init {
    registerDefaultState(
      stateDefinition.any()
        .setValue(HORIZONTAL_FACING, Direction.NORTH)
        .setValue(MASTER, true)
    )
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    val facing = context.horizontalDirection
    val stateForPlacement =
      defaultBlockState().setValue(HORIZONTAL_FACING, facing)
    val pos = context.clickedPos

    if (
      multiblockPositions(pos, facing)
        .map(context.level::getBlockState)
        .any { state -> !state.canBeReplaced() }
    ) return null

    return stateForPlacement
  }

  /**
   * create the multiblock blocks after placement
   */
  override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
    super.setPlacedBy(level, pos, state, placer, stack)
    if (level.isClientSide) return

    val newState = defaultBlockState().setValue(MASTER, false)
    val facing = state.getValue(HORIZONTAL_FACING)
    for (slavePos in multiblockPositions(pos, facing)) {
      level.setBlock(slavePos, newState, UPDATE_ALL)
      level.getBE(slavePos, PILLAR.get())?.run{
        masterPos = pos
      }
    }
  }

  override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
    level.getBE(pos, PILLAR.get())?.breakMultiblock()
    super.onRemove(state, level, pos, newState, movedByPiston)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(HORIZONTAL_FACING).add(MASTER)
  }

  override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = PillarBE(pos, blockState)

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true

  companion object {
    fun multiblockPositions(masterPos: BlockPos, direction: Direction) =
      listOf(
        masterPos.relative(direction),
        masterPos.relative(direction).above(),
      )
  }
}
