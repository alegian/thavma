package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.client.gui.book.BookScreen
import me.alegian.thavma.impl.client.setScreen
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class T7BookItem : Item(Properties().stacksTo(1)) {
  override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
    if (level.isClientSide()) setScreen(BookScreen())

    return InteractionResultHolder.consume(player.getItemInHand(hand))
  }
}
