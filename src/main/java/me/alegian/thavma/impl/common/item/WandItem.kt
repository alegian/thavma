package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.client.renderer.geo.WandRenderer
import me.alegian.thavma.impl.common.block.AuraNodeBlock
import me.alegian.thavma.impl.common.block.TableBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.entity.FancyBookEntity
import me.alegian.thavma.impl.common.entity.VisEntity
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandPlatingMaterial
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

open class WandItem(props: Properties, val platingMaterial: WandPlatingMaterial, val coreMaterial: WandCoreMaterial) :
  Item(props.stacksTo(1).rarity(Rarity.UNCOMMON)), GeoItem {

  private val cache = GeckoLibUtil.createInstanceCache(this)

  /**
   * Use Wand on a Block. Has 3 main uses:
   *
   * 1. delegate the action to a focus
   * 2. Receiving Vis from an Aura Node, by spawning a VisEntity<br></br>
   * 3. Turning Cauldrons into Crucibles<br></br>
   * 4. Creating "Elements of Thavma" books from Bookcases
   */
  override fun useOn(context: UseOnContext): InteractionResult {
    val focus = context.itemInHand.equippedFocus
    if (focus != null) return focus.item.useOn(context)

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

  override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val focus = player.getItemInHand(usedHand).equippedFocus
    if (focus != null) return focus.item.use(level, player, usedHand)

    return super.use(level, player, usedHand)
  }

  override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
    val focus = stack.equippedFocus
    if (focus != null) return focus.item.onUseTick(level, livingEntity, stack, remainingUseDuration)
    super.onUseTick(level, livingEntity, stack, remainingUseDuration)
  }

  override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int) {
    val focus = stack.equippedFocus
    if (focus != null) return focus.item.releaseUsing(stack, level, livingEntity, timeCharged)
    super.releaseUsing(stack, level, livingEntity, timeCharged)
  }

  override fun interactLivingEntity(stack: ItemStack, player: Player, interactionTarget: LivingEntity, usedHand: InteractionHand): InteractionResult {
    val focus = stack.equippedFocus
    if (focus != null) return focus.item.interactLivingEntity(stack, player, interactionTarget, usedHand)

    return super.interactLivingEntity(stack, player, interactionTarget, usedHand)
  }

  override fun getUseAnimation(itemStack: ItemStack): UseAnim {
    return UseAnim.CUSTOM
  }

  override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
    val focusDuration = stack.equippedFocus?.item?.getUseDuration(stack, entity)
    return focusDuration ?: 72000
  }

  /**
   * The normal implementation causes flickering in the wand animation
   * when aspects are synced from server. Therefore, we have to use a
   * less strict variant.
   */
  override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean {
    return oldStack.item != newStack.item
  }

  override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
  }

  override fun getAnimatableInstanceCache() = cache

  override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
    consumer.accept(object : GeoRenderProvider {
      private val renderer by lazy {
        WandRenderer(
          platingMaterial,
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
    get() = name(this.platingMaterial, this.coreMaterial)

  companion object {
    fun name(platingMaterial: WandPlatingMaterial, coreMaterial: WandCoreMaterial): String {
      return platingMaterial.registeredName + "_" + coreMaterial.registeredName + "_wand"
    }

    val ItemStack.equippedFocus
      get() = get(T7DataComponents.FOCUS)?.nonEmptyItems()?.firstOrNull()
  }
}
