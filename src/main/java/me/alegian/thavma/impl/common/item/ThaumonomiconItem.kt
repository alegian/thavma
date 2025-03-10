package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.client.gui.thaumonomicon.ThaumonomiconScreen
import me.alegian.thavma.impl.client.setClientScreen
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ThaumonomiconItem : Item(Properties().stacksTo(1)) {
  override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
    if (level.isClientSide()) setClientScreen(ThaumonomiconScreen())

    return InteractionResultHolder.consume(player.getItemInHand(hand))
  }
}
