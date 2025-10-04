package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.level.Exchanging
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class ExchangeFocus : Item(
  Properties().stacksTo(1)
) {
  override fun useOn(context: UseOnContext): InteractionResult {
    val player = context.player ?: return InteractionResult.PASS
    if (context.itemInHand.item !is WandItem) return InteractionResult.PASS

    if(player !is ServerPlayer) return InteractionResult.SUCCESS
    Exchanging.exchange(context.level, player, context.clickedPos)

    return InteractionResult.CONSUME
  }
}