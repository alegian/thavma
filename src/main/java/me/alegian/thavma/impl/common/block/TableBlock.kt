package me.alegian.thavma.impl.common.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

private val FACING = BlockStateProperties.HORIZONTAL_FACING

class TableBlock : Block(Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()) {
  init {
    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    return defaultBlockState().setValue(FACING,  context.horizontalDirection)
  }

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
    return true
  }
}