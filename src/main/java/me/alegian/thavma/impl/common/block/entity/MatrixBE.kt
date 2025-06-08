package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.block.PillarBlock
import me.alegian.thavma.impl.common.codec.listOfNullable
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
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
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

private const val MAX_ASPECT_DELAY = 4
private const val MAX_ITEM_DELAY = 40

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
  private var currPedestalPos: BlockPos? = null
  var active = false
  var isOpen = false
  private var itemDelay = MAX_ITEM_DELAY
  private var aspectDelay = MAX_ASPECT_DELAY
  private val ANIM_CONTROLLER = AnimationController(
    this, "cycle", 20
  ) { _ -> PlayState.CONTINUE }
    .triggerableAnim("closed", RawAnimation.begin().thenLoop("closed"))
    .triggerableAnim("open", RawAnimation.begin().thenLoop("open"))
    .triggerableAnim("spin_closed", RawAnimation.begin().thenLoop("spin_closed"))
    .triggerableAnim("spin_closed_fast", RawAnimation.begin().thenLoop("spin_closed_fast"))
    .triggerableAnim("spin_open", RawAnimation.begin().thenLoop("spin_open"))
  val drainPos = blockPos.center.add(0.0, 0.5, 0.0) // where the flying aspects go to visually
  val remainingInputs
    get() = get(REMAINING_INPUTS.get())

  init {
    SingletonGeoAnimatable.registerSyncedAnimatable(this)
  }

  private fun extractFromSource(aspect: Aspect, source: IAspectContainer, remainingInputs: RemainingInputs): AspectStack? {
    // some ticks extract 0 aspects, because otherwise the animation is too fast
    val amount =
      if (aspectDelay-- == 0) {
        aspectDelay = MAX_ASPECT_DELAY
        source.extract(aspect, 1, false)
      } else 0

    val newAspects = remainingInputs.aspects.subtract(aspect, amount)
    set(REMAINING_INPUTS.get(), remainingInputs.copy(aspects = newAspects))
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

  private fun pedestalOrNull(pos: BlockPos?, ingredient: Ingredient): PedestalBE? {
    if (pos == null) return null
    val pedestalBE = level?.getBE(pos, PEDESTAL.get())
    return if (ingredient.test(pedestalBE?.getItem())) pedestalBE else null
  }

  private fun pickPedestal(ingredient: Ingredient): PedestalBE? {
    var pedestal = pedestalOrNull(currPedestalPos, ingredient)
    if (pedestal != null) return pedestal

    currPedestalPos = BlockPos.findClosestMatch(blockPos.below(), 7, 3) {
      pedestal = pedestalOrNull(it, ingredient)
      pedestal != null
    }.getOrNull()
    return pedestal
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
      isOpen = false
    } else {
      triggerAnim("cycle", "spin_closed")
      active = true
    }
  }

  fun open() {
    if (level?.isClientSide ?: true) return

    triggerAnim("cycle", "open")
    isOpen = true
  }

  fun attemptInfusion() {
    set(REMAINING_INPUTS.get(), RemainingInputs())

    triggerAnim("cycle", "spin_closed")
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

    level.sendParticles(ItemParticleOption(T7ParticleTypes.INFUSION_ITEM.get(), stack), x, y, z, 0, velX, velY, velZ, 0.16)
  }

  /**
   * true -> continue to item phase
   * false -> dont
   */
  fun aspectPhaseTick(level: ServerLevel): Boolean {
    val remainingInputs = remainingInputs ?: return false
    val remainingAspects = remainingInputs.aspects
    val flyingAspects = getOrDefault(FLYING_ASPECTS.get(), ArrayDeque())
    if (remainingAspects.isEmpty && flyingAspects.isEmpty()) return true

    flyingAspects.removeFirstOrNull()
    set(FLYING_ASPECTS, flyingAspects)
    level.updateBlockEntityS2C(blockPos)

    val currAspect = remainingAspects.firstOrNull()?.aspect ?: return false

    // this line is expected to update currSourcePos as a side effect
    val source = pickSource(currAspect) ?: return false
    val sourcePos = currSourcePos ?: return false
    val extracted = extractFromSource(currAspect, source, remainingInputs) ?: return false
    level.updateBlockEntityS2C(sourcePos)

    val length = trajectoryLength(sourcePos.center, drainPos)
    while (flyingAspects.size < length)
      flyingAspects.addLast(null)

    flyingAspects.addLast(ArrivingAspectStack(sourcePos, extracted))
    set(FLYING_ASPECTS, flyingAspects)
    return false
  }

  /**
   * true -> finish infusion
   * false -> dont
   */
  fun itemPhaseTick(level: ServerLevel): Boolean {
    val remainingInputs = remainingInputs ?: return false
    val remainingIngredients = remainingInputs.ingredients
    if (remainingIngredients.isEmpty()) return true

    val currIngredient = remainingIngredients.first()
    // this line is expected to update currPedestalPos as a side effect
    val pedestalBE = pickPedestal(currIngredient) ?: return false

    sendItemParticles(level, pedestalBE.blockPos, pedestalBE.getItem())
    if (itemDelay-- == 0) {
      pedestalBE.inventory.extractItem(0, 1, false)
      itemDelay = MAX_ITEM_DELAY
      set(REMAINING_INPUTS.get(), remainingInputs.copy(ingredients = remainingIngredients.drop(1)))
    }
    return false
  }

  companion object {
    val FLYING_ASPECTS_CODEC = ArrivingAspectStack.CODEC.listOfNullable("arrivingAspectStack").xmap(
      ::ArrayDeque,
      { deque -> deque.toList() }
    )

    fun tick(level: Level, pos: BlockPos, state: BlockState, be: MatrixBE) {
      if (level.isClientSide || level !is ServerLevel) return
      if (!be.active) return // todo: reset

      var nextPhase = be.aspectPhaseTick(level)
      if (!nextPhase) return
      nextPhase = be.itemPhaseTick(level)
      if (!nextPhase) return
      level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS)
    }
  }
}

private val directions = listOf(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

