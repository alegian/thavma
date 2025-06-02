package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.block.PillarBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.data.capability.IAspectContainer
import me.alegian.thavma.impl.common.infusion.ArrivingAspectStack
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.multiblock.MultiblockRequiredState
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
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
import kotlin.jvm.optionals.getOrNull

private const val ABSORB_SPEED = 1

/**
 * Default values used for rendering Item form
 */
class MatrixBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.MATRIX.get().defaultBlockState(),
  val hasRing: Boolean = false
) : BlockEntity(T7BlockEntities.MATRIX.get(), pos, blockState), GeoBlockEntity {
  private val cache = GeckoLibUtil.createInstanceCache(this)
  private val flyingAspects = ArrayDeque<ArrivingAspectStack?>()
  private var currSourcePos: BlockPos? = null
  private var currSource: IAspectContainer? = null
  private var currRequiredAspect: Aspect? = null
  private var requiredAspects = AspectMap.builder().add(IGNIS.get(), 20).add(TERRA.get(), 20).build()
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

  fun tick() {
    val level = level ?: return
    if (level.isClientSide) return

    absorb(flyingAspects.removeFirstOrNull())

    if (requiredAspects.isEmpty) return
    updateCurrAspect()
    // this line is expected to update currSource && currSourcePos as a side effect
    if (!validateAndUpdateSource()) findNewSource()

    currSourcePos?.let {
      if (flyingAspects.size < trajectoryLength(it.center, blockPos.center))
        flyingAspects.addLast(null)

      flyingAspects.addLast(ArrivingAspectStack(it, extractAspect()))
    }
  }

  /**
   * calculate the aspect that the next infusion tick wants to absorb.
   * tries to continue with the same aspect if possible
   */
  private fun updateCurrAspect() {
    val lastAspect = flyingAspects.getOrNull(flyingAspects.size - 1)
    currRequiredAspect =
      lastAspect?.aspectStack?.aspect ?: requiredAspects.firstOrNull()?.aspect
  }

  private fun extractAspect(): AspectStack? {
    val aspect = currRequiredAspect ?: return null
    val source = currSource ?: return null

    val amount = source.extract(aspect, ABSORB_SPEED, false)
    return AspectStack(aspect, amount)
  }

  /**
   * returns false if the source is no longer valid
   */
  private fun validateAndUpdateSource(): Boolean {
    val aspect = currRequiredAspect ?: return false
    val sourcePos = currSourcePos ?: return false

    currSource = AspectContainer.at(level, sourcePos)?.also { source ->
      return source.aspects[aspect] > 0
    }
    return false
  }

  private fun findNewSource() {
    val level = level ?: return
    val sourcePos = BlockPos.findClosestMatch(blockPos, 7, 7) {
      level.getBlockState(it) === T7Blocks.SEALING_JAR.get().defaultBlockState()
    }.getOrNull()
    TODO("Not yet implemented")
  }

  private fun absorb(arriving: ArrivingAspectStack?) {
    if (arriving == null) return
    requiredAspects.subtract(arriving.aspectStack.aspect, arriving.aspectStack.amount)
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

