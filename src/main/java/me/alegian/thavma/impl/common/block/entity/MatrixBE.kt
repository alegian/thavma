package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.block.PillarBlock
import me.alegian.thavma.impl.common.multiblock.MultiblockRequiredState
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block.UPDATE_CLIENTS
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.SingletonGeoAnimatable
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil

/**
 * Default values used for rendering Item form
 */
class MatrixBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.MATRIX.get().defaultBlockState(),
  val hasRing: Boolean = false
) : BlockEntity(T7BlockEntities.MATRIX.get(), pos, blockState), GeoBlockEntity {
  private val cache = GeckoLibUtil.createInstanceCache(this)
  private val ANIM_CONTROLLER = AnimationController(
    this, "cycle", 20
  ) { _ -> PlayState.CONTINUE }
    .triggerableAnim("closed", RawAnimation.begin().thenLoop("closed"))
    .triggerableAnim("open", RawAnimation.begin().thenLoop("open"))
    .triggerableAnim("spin_closed", RawAnimation.begin().thenLoop("spin_closed"))
    .triggerableAnim("spin_closed_fast", RawAnimation.begin().thenLoop("spin_closed_fast"))
    .triggerableAnim("spin_open", RawAnimation.begin().thenLoop("spin_open"))

  init {
    SingletonGeoAnimatable.registerSyncedAnimatable(this)
  }

  override fun registerControllers(controllers: ControllerRegistrar) {
    controllers.add(ANIM_CONTROLLER)
  }

  override fun getAnimatableInstanceCache() = cache

  fun attemptConvertPillars() {
    if (level?.isClientSide ?: true) return

    for (requiredPillar in requiredPillars()) {
      val masterPos = requiredPillar.blockPos
      val facing = requiredPillar.blockState.getValue(HORIZONTAL_FACING)
      if (PillarBlock.multiblockRequiredLayout(masterPos, facing).any { level?.getBlockState(it.blockPos) !== it.blockState }) continue

      level?.setBlock(requiredPillar.blockPos, requiredPillar.blockState, UPDATE_CLIENTS)
      level?.getBE(requiredPillar.blockPos, PILLAR.get())?.placeMultiblockSlaves(true)
    }
  }

  fun checkActivation() {
    if (level?.isClientSide ?: true) return

    if (requiredPillars().any { level?.getBlockState(it.blockPos) !== it.blockState }) {
      triggerAnim("cycle", "closed")
    } else {
      triggerAnim("cycle", "spin_closed")
    }
  }

  private fun requiredPillars(): List<MultiblockRequiredState> {
    val down2 = blockPos.below(2)
    return directions.map {
      MultiblockRequiredState(
        down2.relative(it, 2),
        T7Blocks.PILLAR.get().defaultBlockState().setValue(HORIZONTAL_FACING, it.opposite)
      )
    }
  }
}

private val directions = listOf(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

