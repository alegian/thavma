package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.block.entity.ResearchTableBE
import me.alegian.thavma.impl.common.menu.ResearchMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult

private val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
private val PART: EnumProperty<BedPart> = BlockStateProperties.BED_PART

/**
 * 2-block system like bed. Loot table drops nothing for FOOT and drops self for HEAD
 */
class ResearchTableBlock : Block(Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion().pushReaction(PushReaction.BLOCK)), EntityBlock {
  init {
    this.registerDefaultState(stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(FACING, Direction.NORTH))
  }

  companion object{
    val CONTAINER_TITLE = "container." + Thavma.MODID + ".research_table"
  }

  override fun getMenuProvider(pState: BlockState, pLevel: Level, pPos: BlockPos): MenuProvider {
    return SimpleMenuProvider(
      { pContainerId, pPlayerInventory, player ->
        ResearchMenu(pContainerId, pPlayerInventory, ContainerLevelAccess.create(pLevel, pPos))
      },
      Component.translatable(CONTAINER_TITLE)
    )
  }

  override fun useWithoutItem(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHitResult: BlockHitResult): InteractionResult {
    if (pLevel.isClientSide) return InteractionResult.SUCCESS
    else {
      pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos))
      return InteractionResult.CONSUME
    }
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING, PART)
  }

  /**
   * only allow placement if the 2nd block can be placed aswell
   */
  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    val direction = context.horizontalDirection
    val secondPos = context.clickedPos.relative(direction)
    val level = context.level
    return if (level.getBlockState(secondPos).canBeReplaced(context) && level.worldBorder.isWithinBounds(secondPos))
      defaultBlockState().setValue(FACING, direction)
    else
      null
  }

  /**
   * When placing, create the 2nd block
   */
  override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
    super.setPlacedBy(level, pos, state, placer, stack)
    if (!level.isClientSide) {
      val secondPos = pos.relative(state.getValue(FACING))
      level.setBlock(secondPos, state.setValue(PART, BedPart.HEAD), UPDATE_ALL)
      level.blockUpdated(pos, Blocks.AIR)
      state.updateNeighbourShapes(level, pos, UPDATE_ALL)
    }
  }

  /**
   * for creative only, break the 2nd block before it has a chance to updateShape, to prevent item drops
   */
  override fun playerWillDestroy(level: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
    if (!level.isClientSide && player.isCreative) {
      val bedpart = state.getValue(PART)
      if (bedpart == BedPart.FOOT) {
        val secondPos = pos.relative(getNeighbourDirection(state))
        val secondState = level.getBlockState(secondPos)
        if (secondState.`is`(this)) {
          level.setBlock(secondPos, Blocks.AIR.defaultBlockState(), UPDATE_ALL or UPDATE_SUPPRESS_DROPS)
          level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, secondPos, getId(secondState))
        }
      }
    }

    return super.playerWillDestroy(level, pos, state, player)
  }

  /**
   * breaks foot after breaking head, and vice versa.
   * responsible for item drops when breaking the FOOT part
   */
  override fun updateShape(state: BlockState, facing: Direction, facingState: BlockState, level: LevelAccessor, currentPos: BlockPos, facingPos: BlockPos): BlockState {
    return if (facing == getNeighbourDirection(state)) {
      if (facingState.block == this && facingState.getValue(PART) != state.getValue(PART))
        state
      else
        Blocks.AIR.defaultBlockState()
    } else {
      super.updateShape(state, facing, facingState, level, currentPos, facingPos)
    }
  }

  override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
    return ResearchTableBE(pos, state)
  }

  override fun getRenderShape(state: BlockState): RenderShape {
    return RenderShape.ENTITYBLOCK_ANIMATED
  }

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
    return true
  }
}

fun getNeighbourDirection(state: BlockState): Direction {
  val direction = state.getValue(FACING)
  return if (state.getValue(PART) == BedPart.FOOT) direction else direction.opposite
}
