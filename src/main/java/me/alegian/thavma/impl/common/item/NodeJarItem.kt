package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext

class NodeJarItem : Item(
  Properties().stacksTo(1)
) {
  override fun useOn(context: UseOnContext): InteractionResult {
    val blockItem = T7Blocks.AURA_NODE.get().asItem()
    if (blockItem !is BlockItem) return InteractionResult.PASS

    val result = blockItem.place(BlockPlaceContext(context))
    if (result == InteractionResult.SUCCESS) {
      AspectContainer.itemSourceBlockSink(context.level, context.clickedPos, context.itemInHand)?.transferAllAspects()
    }

    return result
  }
}