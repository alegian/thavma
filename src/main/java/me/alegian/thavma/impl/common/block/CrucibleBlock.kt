package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.aspect.AspectHelper
import me.alegian.thavma.impl.common.block.entity.CrucibleBE
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.FluidUtil

open class CrucibleBlock : Block(Properties.ofFullCopy(Blocks.CAULDRON)), EntityBlock {
  init {
    this.registerDefaultState(stateDefinition.any().setValue(BOILING, false))
  }

  override fun stepOn(pLevel: Level, pPos: BlockPos, pState: BlockState, pEntity: Entity) {
    if (pState.getValue(BOILING) && !pEntity.isSteppingCarefully && pEntity is LivingEntity)
      pEntity.hurt(pLevel.damageSources().hotFloor(), 1.0f)

    super.stepOn(pLevel, pPos, pState, pEntity)
  }

  override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
    return stateWithBoiling(pContext.level, pContext.clickedPos)
  }

  /**
   * Used by Wands to set BOILING state when converting cauldrons
   */
  open fun stateWithBoiling(level: LevelAccessor, blockPos: BlockPos): BlockState {
    val below = blockPos.below()

    return defaultBlockState().setValue(BOILING, isHeatSource(level, below))
  }

  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    pBuilder.add(BOILING)
  }

  protected open fun isEntityInsideContent(pPos: BlockPos, pEntity: Entity): Boolean {
    return pEntity.y < pPos.y.toDouble() + 15 / 16f
        && pEntity.boundingBox.maxY > pPos.y.toDouble() + 0.25
  }

  override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
    return CrucibleBE(pPos, pState)
  }

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
    return if (type === T7BlockEntities.CRUCIBLE.get()) BlockEntityTicker(CrucibleBE::tick) else null
  }

  override fun isPathfindable(pState: BlockState, pPathComputationType: PathComputationType): Boolean {
    return false
  }

  override fun updateShape(pState: BlockState, pDirection: Direction, pNeighborState: BlockState, pLevel: LevelAccessor, pPos: BlockPos, pNeighborPos: BlockPos): BlockState {
    return stateWithBoiling(pLevel, pPos)
  }

  override fun onPlace(pState: BlockState, pLevel: Level, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
    super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving)
    if (!pOldState.`is`(this)) pLevel.invalidateCapabilities(pPos)
  }

  override fun onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
    super.onRemove(pState, pLevel, pPos, pOldState, pIsMoving)
    if (!pState.`is`(pOldState.block)) pLevel.invalidateCapabilities(pPos)
  }

  override fun useItemOn(pStack: ItemStack, pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHitResult: BlockHitResult): ItemInteractionResult {
    // water buckets should be usable to top off, even if 1000 mB is too much
    if (pPlayer.getItemInHand(pHand).`is`(Items.WATER_BUCKET) && fillUp(pLevel, pPos)) {
      if (!pPlayer.isCreative) pPlayer.setItemInHand(pHand, ItemStack(Items.BUCKET))
      pLevel.playSound(null, pPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f)

      return ItemInteractionResult.SUCCESS
    }

    // generic fluid interaction
    if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHitResult.direction))
      return ItemInteractionResult.SUCCESS

    return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult)
  }

  override fun getInteractionShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos): VoxelShape {
    return CRUCIBLE_INSIDE
  }

  override fun getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape {
    return CRUCIBLE_SHAPE
  }

  override fun entityInside(pState: BlockState, level: Level, pPos: BlockPos, pEntity: Entity) {
    if (!level.isClientSide && level is ServerLevel
      && pState.getValue(BOILING)
      && pEntity is ItemEntity
      && this.isEntityInsideContent(pPos, pEntity)
      && pEntity.mayInteract(level, pPos)
    ) {
      meltItem(level, pPos, pEntity)
      level.sendBlockUpdated(
        pPos,
        pState,
        pState,
        UPDATE_CLIENTS
      )
    }
  }

  companion object {
    val CRUCIBLE_INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)
    val CRUCIBLE_SHAPE = Shapes.join(
      Shapes.block(),
      Shapes.or(
        box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
        box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
        box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
        CRUCIBLE_INSIDE
      ),
      BooleanOp.ONLY_FIRST
    )
    val BOILING = T7BlockStateProperties.BOILING

    protected fun meltItem(level: ServerLevel, pPos: BlockPos, itemEntity: ItemEntity) {
      val thrownStack = itemEntity.item

      // try to use as catalyst
      if (thrownStack.`is`(T7Tags.Items.CATALYSTS)) {
        val crucibleAspects = AspectContainer.at(level, pPos)?.aspects ?: return

        val input = SingleRecipeInput(thrownStack)
        val recipe = level.recipeManager.getRecipeFor(T7RecipeTypes.CRUCIBLE.get(), input, level).orElse(null)?.value()

        val success = recipe.let {
          if (it == null) return@let false
          if (!tryLowerFillLevel(level, pPos)) return@let false
          if (!crucibleAspects.contains(it.aspects)) return@let false
          waterSplash(level, pPos)

          AspectContainer.at(level, pPos)?.extract(it.aspects)

          val player = itemEntity.owner
          if (player is ServerPlayer) {
            player.drop(it.result, true, true)?.run {
              setNoPickUpDelay()
              target = player.getUUID()
              itemEntity.shrink()
            }
          }
          true
        }

        if (success) return  // if catalyst failed, try to melt item instead
      }

      val itemAspects = AspectHelper.getAspects(itemEntity)
      if (itemAspects == null || !hasWater(level, pPos)) return
      AspectContainer.at(level, pPos)?.insert(itemAspects)
      waterSplash(level, pPos)
      itemEntity.discard()
    }

    protected fun waterSplash(level: ServerLevel, pPos: BlockPos) {
      level.playSound(null, pPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1f, 1.0f)
    }

    // returns true if any water was drained
    protected fun tryLowerFillLevel(pLevel: Level, pPos: BlockPos): Boolean {
      return pLevel.getBlockEntity(pPos, T7BlockEntities.CRUCIBLE.get()).map { be -> be.fluidHandler.catalystDrain() }.orElse(false)
    }

    protected fun hasWater(pLevel: Level, pPos: BlockPos): Boolean {
      return pLevel.getBlockEntity(pPos, T7BlockEntities.CRUCIBLE.get()).map { be -> be.fluidHandler.fluidAmount > 0 }.orElse(false)
    }

    // returns true if any water was filled
    protected fun fillUp(pLevel: Level, pPos: BlockPos): Boolean {
      val be = pLevel.getBlockEntity(pPos)
      if (be is CrucibleBE) return be.fluidHandler.fillUp()
      return false
    }

    protected fun ItemEntity.shrink() {
      item.shrink(1)
      if (item.isEmpty) discard()
      else item = item.copy()
    }

    protected fun isHeatSource(level: LevelAccessor, pos: BlockPos): Boolean {
      val bs = level.getBlockState(pos)
      val bsHeat = bs.`is`(T7Tags.Blocks.CRUCIBLE_HEAT_SOURCES)
      val fs = level.getFluidState(pos)
      val fsHeat = fs.`is`(T7Tags.Fluids.CRUCIBLE_HEAT_SOURCES)
      return bsHeat || fsHeat
    }
  }
}
