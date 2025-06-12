package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.block.PillarBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.data.capability.IAspectContainer
import me.alegian.thavma.impl.common.data.capability.firstStack
import me.alegian.thavma.impl.common.data.update
import me.alegian.thavma.impl.common.infusion.ArrivingAspectStack
import me.alegian.thavma.impl.common.infusion.InfusionState
import me.alegian.thavma.impl.common.infusion.RemainingInputs
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.multiblock.MultiblockRequiredState
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.util.getRecipe
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.INFUSION_STATE
import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput
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


/**
 * Default values used for rendering Item form
 */
class MatrixBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.MATRIX.get().defaultBlockState(),
  val hasRing: Boolean = false
) : DataComponentBE(T7BlockEntities.MATRIX.get(), pos, blockState), GeoBlockEntity {
  private val cache = GeckoLibUtil.createInstanceCache(this)
  private val ANIM_CONTROLLER = AnimationController(
    this, "cycle", 20
  ) { _ -> PlayState.CONTINUE }
    .triggerableAnim("closed", RawAnimation.begin().thenLoop("closed"))
    .triggerableAnim("open", RawAnimation.begin().thenLoop("open"))
    .triggerableAnim("spin_closed", RawAnimation.begin().thenLoop("spin_closed"))
    .triggerableAnim("spin_closed_fast", RawAnimation.begin().thenLoop("spin_closed_fast"))
    .triggerableAnim("spin_open", RawAnimation.begin().thenLoop("spin_open"))
  val drainPos = blockPos.center.add(0.0, 0.5, 0.0) // where the flying aspects go to visually

  init {
    SingletonGeoAnimatable.registerSyncedAnimatable(this)
    set(INFUSION_STATE, InfusionState())
  }

  override fun onLoad() {
    super.onLoad()
    val state = get(INFUSION_STATE) ?: return
    if (state.isOpen)
      triggerAnim("cycle", "open")
    else if (state.active)
      triggerAnim("cycle", "spin_closed")
  }

  private fun extractFromSource(aspect: Aspect, source: IAspectContainer, infusionState: InfusionState): AspectStack? {
    val remainingInputs = infusionState.remainingInputs
    val aspectDelay = infusionState.aspectDelay
    // some ticks extract 0 aspects, because otherwise the animation is too fast
    if (aspectDelay == 0) {
      val amount = source.extract(aspect, 1, false)
      val newAspects = remainingInputs.aspects.subtract(aspect, amount)
      update(INFUSION_STATE) {
        it.copy(
          aspectDelay = MAX_ASPECT_DELAY,
          remainingInputs = it.remainingInputs.copy(
            aspects = newAspects
          )
        )
      }
      return AspectStack(aspect, amount)
    } else {
      update(INFUSION_STATE) { it.copy(aspectDelay = aspectDelay - 1) }
      return AspectStack(aspect, 0)
    }
  }

  private fun sourceOrNull(pos: BlockPos?, aspect: Aspect): IAspectContainer? {
    if (pos == null) return null
    val source = AspectContainer.at(level, pos) ?: return null
    val valid = source.aspects[aspect] > 0
    return if (valid) source else null
  }

  // todo: optimize this running every tick when no sources are nearby
  private fun pickSource(aspect: Aspect, infusionState: InfusionState): IAspectContainer? {
    var source = sourceOrNull(infusionState.prevSourcePos, aspect)
    if (source != null) return source

    val newSourcePos = BlockPos.findClosestMatch(blockPos.below(), 7, 3) {
      source = sourceOrNull(it, aspect)
      source != null
    }.getOrNull()
    update(INFUSION_STATE) { it.copy(prevSourcePos = newSourcePos) }

    return source
  }

  private fun pedestalOrNull(pos: BlockPos?, ingredient: Ingredient): PedestalBE? {
    if (pos == null) return null
    val pedestalBE = level?.getBE(pos, PEDESTAL.get())
    return if (ingredient.test(pedestalBE?.itemHandler?.firstStack)) pedestalBE else null
  }

  // todo: optimize this running every tick when no sources are nearby
  private fun pickPedestal(ingredient: Ingredient, infusionState: InfusionState): PedestalBE? {
    var pedestal = pedestalOrNull(infusionState.prevPedestalPos, ingredient)
    if (pedestal != null) return pedestal

    val newPedestalPos = BlockPos.findClosestMatch(blockPos.below(), 7, 3) {
      pedestal = pedestalOrNull(it, ingredient)
      pedestal != null
    }.getOrNull()
    update(INFUSION_STATE) { it.copy(prevPedestalPos = newPedestalPos) }

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
      update(INFUSION_STATE) { it.copy(active = false, isOpen = false) }
    } else {
      triggerAnim("cycle", "spin_closed")
      update(INFUSION_STATE) { it.copy(active = true) }
    }
  }

  fun open() {
    if (level?.isClientSide ?: true) return

    triggerAnim("cycle", "open")
    update(INFUSION_STATE) { it.copy(isOpen = true) }
  }

  fun attemptInfusion(): Boolean {
    val baseStack = itemHandler?.firstStack ?: return false
    val recipe = level?.getRecipe(T7RecipeTypes.INFUSION, SingleRecipeInput(baseStack)) ?: return false

    update(INFUSION_STATE) {
      it.copy(
        remainingInputs = RemainingInputs.of(recipe),
        result = recipe.result,
        isOpen = false
      )
    }

    triggerAnim("cycle", "spin_closed")
    return true
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

  private fun sendRuneParticles(level: ServerLevel) {
    if (level.gameTime % 2 == 0L) return
    // random between 0.5 and 0.6, with random sign
    fun rand(): Double {
      val randScale = 2
      return (level.random.nextDouble() * randScale - randScale / 2)
    }

    val x = blockPos.center.x + rand()
    val y = blockPos.center.y + rand()
    val z = blockPos.center.z + rand()
    level.sendParticles(T7ParticleTypes.INFUSION_RUNE.get(), x, y, z, 0, .0, .0, .0, 0.16)
  }

  /**
   * true -> continue to item phase
   * false -> dont
   */
  fun aspectPhaseTick(level: ServerLevel): Boolean {
    val infusionState = get(INFUSION_STATE.get()) ?: return false
    val remainingAspects = infusionState.remainingInputs.aspects
    val flyingAspects = infusionState.flyingAspects
    if (remainingAspects.isEmpty && flyingAspects.isEmpty()) return true

    flyingAspects.removeFirstOrNull()
    update(INFUSION_STATE) { it.copy(flyingAspects = flyingAspects) }
    level.updateBlockEntityS2C(blockPos)

    val currAspect = remainingAspects.firstOrNull()?.aspect ?: return false

    // this line is expected to update currSourcePos as a side effect
    val source = pickSource(currAspect, infusionState) ?: return false
    val sourcePos = infusionState.prevSourcePos ?: return false
    val extracted = extractFromSource(currAspect, source, infusionState) ?: return false
    level.updateBlockEntityS2C(sourcePos)

    val length = trajectoryLength(sourcePos.center, drainPos)
    while (flyingAspects.size < length)
      flyingAspects.addLast(null)

    flyingAspects.addLast(ArrivingAspectStack(sourcePos, extracted))
    update(INFUSION_STATE) { it.copy(flyingAspects = flyingAspects) }
    return false
  }

  /**
   * true -> finish infusion
   * false -> dont
   */
  fun itemPhaseTick(level: ServerLevel): Boolean {
    val infusionState = get(INFUSION_STATE.get()) ?: return false
    val remainingIngredients = infusionState.remainingInputs.ingredients
    if (remainingIngredients.isEmpty()) return true

    val currIngredient = remainingIngredients.first()
    // this line is expected to update currPedestalPos as a side effect
    val pedestalBE = pickPedestal(currIngredient, infusionState) ?: return false
    val pedestalItemHandler = pedestalBE.itemHandler ?: return false

    val itemDelay = infusionState.itemDelay
    sendItemParticles(level, pedestalBE.blockPos, pedestalItemHandler.firstStack)
    if (itemDelay == 0) {
      pedestalItemHandler.extractItem(0, 1, false)
      update(INFUSION_STATE) {
        it.copy(
          itemDelay = MAX_ITEM_DELAY,
          remainingInputs = it.remainingInputs.copy(
            ingredients = remainingIngredients.drop(1)
          )
        )
      }
    } else {
      update(INFUSION_STATE) { it.copy(itemDelay = itemDelay - 1) }
    }
    return false
  }

  companion object {
    val MAX_ASPECT_DELAY = 4
    val MAX_ITEM_DELAY = 40

    fun tick(level: Level, pos: BlockPos, state: BlockState, be: MatrixBE) {
      if (level.isClientSide || level !is ServerLevel) return
      be.sendRuneParticles(level)
      val infusionState = be.get(INFUSION_STATE.get()) ?: return
      if (!infusionState.active || infusionState.result.isEmpty) return // todo: reset

      var nextPhase = be.aspectPhaseTick(level)
      if (!nextPhase) return
      nextPhase = be.itemPhaseTick(level)
      if (!nextPhase) return
      level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS)
      be.update(INFUSION_STATE) {
        be.itemHandler?.run {
          extractItem(0, 1, false)
          insertItem(0, it.result, false)
        }
        it.copy(result = ItemStack.EMPTY)
      }
      be.open()
      level.updateBlockEntityS2C(be.blockPos)
    }
  }
}

private val directions = listOf(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)

