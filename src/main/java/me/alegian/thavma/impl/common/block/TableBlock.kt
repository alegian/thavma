package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BlockStateProperties.BED_PART
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING

private val FACING = BlockStateProperties.HORIZONTAL_FACING

class TableBlock : Block(Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()) {
  init {
    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    return defaultBlockState().setValue(FACING, context.horizontalDirection)
  }

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
    return true
  }

  /**
   * converts this table and a neighboring one into a research table.
   * returns true if successful
   */
  fun tryConvertToResearchTable(level: ServerLevel, blockPos: BlockPos, blockState: BlockState): Boolean {
    val direction = blockState.getValue(HORIZONTAL_FACING)
    val secondPos = blockPos.relative(direction)
    if (level.getBlockState(secondPos).block == this) {
      level.setBlock(blockPos, T7Blocks.RESEARCH_TABLE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction), 0)
      level.setBlockAndUpdate(secondPos, T7Blocks.RESEARCH_TABLE.get().defaultBlockState().setValue(HORIZONTAL_FACING, direction).setValue(BED_PART, BedPart.HEAD))

      return true
    }

    return false
  }
}