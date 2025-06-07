package me.alegian.thavma.impl.common.block.entity

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.block.PillarBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.data.capability.IAspectContainer
import me.alegian.thavma.impl.common.infusion.ArrivingAspectStack
import me.alegian.thavma.impl.common.infusion.RemainingInputs
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.multiblock.MultiblockRequiredState
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.FLYING_ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.REMAINING_INPUTS
import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
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

private const val ABSORB_DELAY = 4L

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
    get() = arrayOf(FLYING_ASPECTS.get(), REMAINING_INPUTS.get())
  val drainPos = blockPos.center.add(0.0, 0.5, 0.0) // where the flying aspects go to visually
  var remainingAspects
    get() = get(REMAINING_INPUTS.get())?.aspects
    set(aspects) {
      if (aspects == null) return
      val oldRemaining = get(REMAINING_INPUTS.get()) ?: return
      set(REMAINING_INPUTS.get(), RemainingInputs(oldRemaining.ingredients, aspects))
    }
  var remainingIngredients
    get() = get(REMAINING_INPUTS.get())?.ingredients
    set(ingredients) {
      if (ingredients == null) return
      val oldRemaining = get(REMAINING_INPUTS.get()) ?: return
      set(REMAINING_INPUTS.get(), RemainingInputs(ingredients, oldRemaining.aspects))
    }


  init {
    SingletonGeoAnimatable.registerSyncedAnimatable(this)
    // todo: remove after testing
    set(REMAINING_INPUTS.get(), RemainingInputs(listOf(Ingredient.of(Items.DIAMOND))))
  }

  private fun extractFromSource(aspect: Aspect, source: IAspectContainer): AspectStack? {
    val level = level ?: return null

    // some ticks extract 0 aspects, because otherwise the animation is too fast
    val waiting = level.gameTime % ABSORB_DELAY != (ABSORB_DELAY - 1)
    val amount =
      if (!waiting) source.extract(aspect, 1, false)
      else 0

    remainingAspects = remainingAspects?.subtract(aspect, amount)
    return AspectStack(aspect, amount)
  }

  private fun sourceOrNull(pos: BlockPos?, aspect: Aspect): IAspectContainer? {
    if (pos == null) return null
    val source = AspectContainer.at(level, pos) ?: return null
    val valid = source.aspects[aspect] > 0
    return if (valid) source else null
  }

  // todo: optimize this running every tick when no sources are nearby
  private fun pickSource(aspect: Aspect): IAspectContainer? {
    var source = sourceOrNull(currSourcePos, aspect)
    if (source != null) return source

    currSourcePos = BlockPos.findClosestMatch(blockPos.below(), 7, 3) {
      source = sourceOrNull(it, aspect)
      source != null
    }.getOrNull()
    return source
  }

  private fun findPedestal(): PedestalBE? {
    val level = level ?: return null
    for (pos in BlockPos.withinManhattan(blockPos, 7, 3, 7)) {
      return level.getBE(pos, PEDESTAL.get()) ?: continue
    }
    return null
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

  private fun sendItemParticles(level: ServerLevel, pedestalPos: BlockPos, stack: ItemStack) {
    if (stack.isEmpty) return

    val randScale = 0.2
    val rand = (level.random.nextDouble() * randScale - randScale / 2)
    val x = pedestalPos.x + rand + 0.5
    val y = pedestalPos.y + rand + 1.1
    val z = pedestalPos.z + rand + 0.5

    val velX = blockPos.center.x - x
    val velY = blockPos.center.y - y
    val velZ = blockPos.center.z - z

    level.sendParticles(ItemParticleOption(T7ParticleTypes.INFUSION_ITEM.get(), stack), x, y, z, 0, velX.toDouble(), velY.toDouble(), velZ.toDouble(), 0.16)
  }

  /**
   * true -> continue to item phase
   * false -> dont
   */
  fun aspectPhaseTick(level: ServerLevel): Boolean {
    val remainingAspects = remainingAspects ?: return false
    val flyingAspects = getOrDefault(FLYING_ASPECTS.get(), ArrayDeque())
    if (remainingAspects.isEmpty && flyingAspects.isEmpty()) return true

    flyingAspects.removeFirstOrNull()
    set(FLYING_ASPECTS, flyingAspects)
    level.updateBlockEntityS2C(blockPos)

    val currAspect = remainingAspects.firstOrNull()?.aspect ?: return false

    // this line is expected to update currSourcePos as a side effect
    val source = pickSource(currAspect) ?: return false
    val sourcePos = currSourcePos ?: return false
    val extracted = extractFromSource(currAspect, source) ?: return false
    level.updateBlockEntityS2C(sourcePos)

    val length = trajectoryLength(sourcePos.center, drainPos)
    while (flyingAspects.size < length)
      flyingAspects.addLast(null)

    flyingAspects.addLast(ArrivingAspectStack(sourcePos, extracted))
    set(FLYING_ASPECTS, flyingAspects)
    return false
  }

  fun itemPhaseTick(level: ServerLevel) {
    val pedestalBE = findPedestal() ?: return
    sendItemParticles(level, pedestalBE.blockPos, pedestalBE.getItem())
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
      if (!be.active) return // todo: reset

      val proceed = be.aspectPhaseTick(level)
      if (proceed) be.itemPhaseTick(level)
    }
  }
}

private val directions = listOf(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

