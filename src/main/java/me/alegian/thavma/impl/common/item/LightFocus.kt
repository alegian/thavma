package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext

class LightFocus : Item(
  Properties().stacksTo(1)
) {
  override fun useOn(context: UseOnContext): InteractionResult {
    if (context.itemInHand.item !is WandItem) return InteractionResult.PASS

    val blockItem = T7Blocks.ETERNAL_FLAME.get().asItem()
    if (blockItem !is BlockItem) return InteractionResult.PASS

    return blockItem.place(BlockPlaceContext(context))
  }
}