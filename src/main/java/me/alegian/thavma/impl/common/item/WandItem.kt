package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.client.renderer.geo.WandRenderer
import me.alegian.thavma.impl.common.block.AuraNodeBlock
import me.alegian.thavma.impl.common.block.TableBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.entity.FancyBookEntity
import me.alegian.thavma.impl.common.entity.VisEntity
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.rl
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import software.bernie.geckolib.GeckoLibServices
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.network.packet.SingletonAnimTriggerPacket
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

private val CAST_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("casting")
private val IDLE_ANIMATION: RawAnimation = RawAnimation.begin().thenPlay("idle")

open class WandItem(props: Properties, val handleMaterial: WandHandleMaterial, val coreMaterial: WandCoreMaterial) :
  Item(props.stacksTo(1).rarity(Rarity.UNCOMMON)), GeoItem {

  private val cache = GeckoLibUtil.createInstanceCache(this)

  init {
    GeckoLibUtil.SYNCED_ANIMATABLES[this.syncableId()] = this
  }

  /**
   * Use Wand on a Block. Has 3 main uses:
   *
   *
   * 1. Receiving Vis from an Aura Node, by spawning a VisEntity<br></br>
   * 2. Turning Cauldrons into Crucibles<br></br>
   * 3. Creating "Elements of Thavma" books from Bookcases
   */
  override fun useOn(context: UseOnContext): InteractionResult {
    val level = context.level
    val blockPos = context.clickedPos
    val blockState = level.getBlockState(blockPos)
    val block = blockState.block

    if (block is AuraNodeBlock) {
      val player = context.player

      val transferPair = AspectContainer.blockSourceItemSink(level, blockPos, context.itemInHand)
      val canTransfer = transferPair?.canTransferPrimals() ?: false
      if (player != null && canTransfer) {
        player.startUsingItem(context.hand)
        if (!level.isClientSide() && level is ServerLevel) {
          level.addFreshEntity(VisEntity(level, player, blockPos))
          this.animateCircle(true, player, context.itemInHand, level)
        }
        return InteractionResult.CONSUME
      }
    }
    if (blockState.`is`(Blocks.CAULDRON)) {
      if (!level.isClientSide()) level.setBlockAndUpdate(blockPos, T7Blocks.CRUCIBLE.get().stateWithBoiling(level, blockPos))
      level.playSound(context.player, blockPos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f)
      return InteractionResult.SUCCESS
    }
    if (blockState.`is`(Blocks.CRAFTING_TABLE)) {
      if (!level.isClientSide()) level.setBlockAndUpdate(blockPos, T7Blocks.ARCANE_WORKBENCH.get().defaultBlockState())
      level.playSound(context.player, blockPos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f)
      return InteractionResult.SUCCESS
    }
    if (
      block is TableBlock &&
      !level.isClientSide() &&
      level is ServerLevel &&
      block.tryConvertToResearchTable(level, blockPos, blockState)
    ) {
      level.playSound(context.player, blockPos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f)
      return InteractionResult.SUCCESS
    }
    if (blockState.`is`(Blocks.BOOKSHELF)) {
      if (!level.isClientSide() && level.removeBlock(blockPos, false)) level.addFreshEntity(
        FancyBookEntity(level, blockPos)
      )
      level.playSound(context.player, blockPos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f)
      return InteractionResult.SUCCESS
    }
    if (blockState.`is`(Tags.Blocks.GLASS_BLOCKS)) {
      val direction: Direction = context.clickedFace.opposite
      val behindPos: BlockPos = blockPos.relative(direction, 1)
      return level.getBE(behindPos, T7BlockEntities.AURA_NODE.get())
        ?.let { be ->
          if (be.jarInteraction()) InteractionResult.SUCCESS
          else InteractionResult.FAIL
        } ?: InteractionResult.PASS
    }
    return InteractionResult.PASS
  }

  override fun getUseAnimation(itemStack: ItemStack): UseAnim {
    return UseAnim.CUSTOM
  }

  override fun getUseDuration(pStack: ItemStack, pEntity: LivingEntity): Int {
    return 72000
  }

  override fun onStopUsing(itemStack: ItemStack, entity: LivingEntity, count: Int) {
    this.animateCircle(false, entity, itemStack, entity.level())
    super.onStopUsing(itemStack, entity, count)
  }

  /**
   * The normal implementation causes flickering in the wand animation
   * when aspects are synced from server. Therefore, we have to use a
   * less strict variant.
   */
  override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean {
    return oldStack.item != newStack.item
  }

  /**
   * Only does things on the server
   */
  protected fun animateCircle(isCasting: Boolean, entity: Entity, itemStack: ItemStack, level: Level) {
    if (!level.isClientSide() && level is ServerLevel) this.animateCircle(isCasting, entity, itemStack, level)
  }

  /**
   * Faster variant, if we already have the serverLevel.
   * Sends a gecko internal packet with custom syncableId, to avoid non-singleton bugs
   */
  protected fun animateCircle(isCasting: Boolean, entity: Entity, itemStack: ItemStack, level: ServerLevel) {
    val animationName = if (isCasting) "casting" else "idle"
    GeckoLibServices.NETWORK.sendToAllPlayersTrackingEntity(
      SingletonAnimTriggerPacket(
        this.syncableId(),
        GeoItem.getOrAssignId(itemStack, level),
        "Casting",
        animationName
      ), entity
    )
  }

  override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    controllers.add(
      AnimationController(
        this, "Casting", 0
      ) { state ->
        val controller = state.controller
        if (controller.currentAnimation == null) controller.setAnimation(IDLE_ANIMATION)
        PlayState.CONTINUE
      }
        .triggerableAnim("casting", CAST_ANIMATION)
        .triggerableAnim("idle", IDLE_ANIMATION)
    )
  }

  override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
    return this.cache
  }

  override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
    consumer.accept(object : GeoRenderProvider {
      private val renderer by lazy {
        WandRenderer(
          handleMaterial,
          coreMaterial
        )
      }

      override fun getGeoItemRenderer() = renderer
    })
  }

  open fun capacity(): Int {
    return coreMaterial.capacity
  }

  open val name: String
    get() = name(this.handleMaterial, this.coreMaterial)

  /**
   * Custom Animatable Syncable ID, for Gecko. By default, Item classes
   * in gecko are considered singletons, and therefore use classnames
   * as identifiers, but this doesn't work for Wands, as the same class
   * has multiple instances.
   */
  open fun syncableId(): String {
    val location = rl(this.name)
    return location.toString()
  }

  companion object {
    fun name(handleMaterial: WandHandleMaterial, coreMaterial: WandCoreMaterial): String {
      return handleMaterial.registeredName + "_" + coreMaterial.registeredName + "_wand"
    }
  }
}
