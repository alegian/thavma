package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.client.gui.thaumonomicon.BookEntryScreen
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ResearchScrollItem : Item(Properties().stacksTo(1)){
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if(level.isClientSide) Minecraft.getInstance().setScreen(BookEntryScreen())
        return super.use(level, player, usedHand)
    }
}