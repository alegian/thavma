package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.entity.setKnowledge
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ResearchScrollItem : Item(Properties().stacksTo(1)) {
  override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stack = player.getItemInHand(usedHand)
    val state = stack.get(T7DataComponents.RESEARCH_STATE) ?: return InteractionResultHolder.pass(stack)
    if (!state.completed) return InteractionResultHolder.pass(stack)

    if (player is ServerPlayer) {
      player.setItemInHand(usedHand, ItemStack.EMPTY)
      player.setKnowledge(state.researchEntry)
    }

    if (level.isClientSide)
      level.playSound(player, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4f, 1f)

    return InteractionResultHolder.success(stack)
  }

  override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
    val state = stack.get(T7DataComponents.RESEARCH_STATE) ?: return
    tooltipComponents.add(
      Component.translatable(
        ResearchEntry.translationId(state.researchEntry)
      ).withStyle(Rarity.UNCOMMON.styleModifier)
    )
  }

  override fun getName(stack: ItemStack): Component {
    val state = stack.get(T7DataComponents.RESEARCH_STATE)
    if (state?.completed == true) return Component.translatable(completedTranslation())
    return super.getName(stack)
  }

  fun completedTranslation(): String {
    return getOrCreateDescriptionId() + ".completed"
  }
}