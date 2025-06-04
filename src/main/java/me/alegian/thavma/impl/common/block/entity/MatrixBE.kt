package me.alegian.thavma.impl.common.block.entity

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
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
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.FLYING_ASPECTS
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block.UPDATE_CLIENTS
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

private const val ABSORB_DELAY = 4

/**
 * Default values used for rendering Item form
 */
class MatrixBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.MATRIX.get().defaultBlockState(),
  val hasRing: Boolean = false
) : DataComponentBE(T7BlockEntities.MATRIX.get(), pos, blockState), GeoBlockEntity {
  private val cache = GeckoLibUtil.createInstanceCache(this)
  private var currSourcePos: BlockPos? = null
  private var currSource: IAspectContainer? = null
  private var currRequiredAspect: Aspect? = null
  private var requiredAspects = AspectMap.builder().add(IGNIS.get(), 40).add(TERRA.get(), 50).build()
  private var active = false
  private val ANIM_CONTROLLER = AnimationController(
    this, "cycle", 20
  ) { _ -> PlayState.CONTINUE }
    .triggerableAnim("closed", RawAnimation.begin().thenLoop("closed"))
    .triggerableAnim("open", RawAnimation.begin().thenLoop("open"))
    .triggerableAnim("spin_closed", RawAnimation.begin().thenLoop("spin_closed"))
    .triggerableAnim("spin_closed_fast", RawAnimation.begin().thenLoop("spin_closed_fast"))
    .triggerableAnim("spin_open", RawAnimation.begin().thenLoop("spin_open"))
  override val componentTypes: Array<DataComponentType<*>>
    get() = arrayOf(FLYING_ASPECTS.get())
  val drainPos = blockPos.center.add(0.0, 0.5, 0.0)

  init {
    SingletonGeoAnimatable.registerSyncedAnimatable(this)
  }

  /**
   * calculate the aspect that the next infusion tick wants to absorb.
   * tries to continue with the same aspect if possible
   */
  private fun updateCurrAspect() {
    val default = requiredAspects.firstOrNull()?.aspect
    val oldAspect = currRequiredAspect

    if (oldAspect == null || requiredAspects[oldAspect] == 0) {
      currRequiredAspect = default
      return
    }
  }

  private fun extractFromSource(): AspectStack? {
    val aspect = currRequiredAspect ?: return null
    val source = currSource ?: return null
    val level = level ?: return null

    // some ticks extract 0 aspects, because otherwise the animation is too fast
    val waiting = level.gameTime % ABSORB_DELAY != (ABSORB_DELAY - 1).toLong()
    val amount =
      if (!waiting) source.extract(aspect, 1, false)
      else 0

    requiredAspects = requiredAspects.subtract(aspect, amount)
    return AspectStack(aspect, amount)
  }

  /**
   * returns false if the source is no longer valid
   */
  private fun oldSourceValid(): Boolean {
    val sourcePos = currSourcePos ?: return false

    return attemptSetSource(sourcePos)
  }

  /**
   * returns false if the source is not valid
   */
  private fun attemptSetSource(pos: BlockPos): Boolean {
    val aspect = currRequiredAspect ?: return false

    val source = AspectContainer.at(level, pos) ?: return false
    val valid = source.aspects[aspect] > 0
    if (valid) currSource = source

    return valid
  }

  // todo: optimize this running every tick when no sources are nearby
  private fun findNewSource() {
    currSourcePos = BlockPos.findClosestMatch(blockPos.below(), 7, 3, ::attemptSetSource).getOrNull()
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
      active = false
    } else {
      triggerAnim("cycle", "spin_closed")
      active = true
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

  companion object {
    val FLYING_ASPECTS_CODEC = Codec.either(ArrivingAspectStack.CODEC, Codec.EMPTY.codec()).listOf().xmap(
      { list -> ArrayDeque(list.map { e -> e.map({ it }, { r -> null }) }) },
      { deque ->
        deque.toList().map {
          if (it == null) Either.right(Unit.INSTANCE)
          else Either.left(it)
        }
      }
    )
    val FLYING_ASPECTS_STREAM_CODEC = ByteBufCodecs.either(ArrivingAspectStack.STREAM_CODEC, StreamCodec.unit(Unit.INSTANCE)).apply(ByteBufCodecs.list()).map(
      { list -> ArrayDeque(list.map { e -> e.map({ it }, { r -> null }) }) },
      { deque ->
        deque.toList().map {
          if (it == null) Either.right(Unit.INSTANCE)
          else Either.left(it)
        }
      }
    )

    fun tick(level: Level, pos: BlockPos, state: BlockState, be: MatrixBE) {
      if (level.isClientSide || level !is ServerLevel) return

      val flyingAspects = be.getOrDefault(FLYING_ASPECTS.get(), ArrayDeque())
      be.run {
        flyingAspects.removeFirstOrNull()

        if (!active || requiredAspects.isEmpty) return@run
        updateCurrAspect()
        // this line is expected to update currSource && currSourcePos as a side effect
        if (!oldSourceValid()) findNewSource()

        val sourcePos = currSourcePos ?: return@run
        val extracted = extractFromSource() ?: return@run

        val length = trajectoryLength(sourcePos.center, drainPos)
        while (flyingAspects.size < length)
          flyingAspects.addLast(null)

        flyingAspects.addLast(ArrivingAspectStack(sourcePos, extracted))

        level.updateBlockEntityS2C(sourcePos)
      }
      // todo: optimize, we dont need to sync every tick
      be.set(FLYING_ASPECTS, flyingAspects)
      level.updateBlockEntityS2C(pos)
    }
  }
}

private val directions = listOf(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

