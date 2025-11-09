package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.level.Exchanging
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class ExchangeFocus : Item(
  Properties().stacksTo(1)
) {
  override fun useOn(context: UseOnContext): InteractionResult {
    val player = context.player ?: return InteractionResult.PASS
    if (context.itemInHand.item !is WandItem) return InteractionResult.PASS
    val level = context.level
    val pos = context.clickedPos
    val oldBlock = level.getBlockState(pos).block

    if (level.getBlockEntity(pos) != null) return InteractionResult.FAIL

    if (player.isShiftKeyDown) {
      if (level.isClientSide) player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP)
      context.itemInHand.set(T7DataComponents.EXCHANGE_BLOCK, oldBlock)
      return InteractionResult.SUCCESS
    }

    val newBlock = player.mainHandItem.get(T7DataComponents.EXCHANGE_BLOCK) ?: return InteractionResult.FAIL
    if (oldBlock == newBlock) return InteractionResult.FAIL

    if (player is ServerPlayer) Exchanging.exchange(context.level, player, pos, oldBlock, newBlock)
    return InteractionResult.SUCCESS
  }
}