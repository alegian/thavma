package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class HoleFocus : Item(Properties().stacksTo(1)) {
  override fun useOn(context: UseOnContext): InteractionResult {
    makeHole(context.level, context.clickedPos)
    return InteractionResult.SUCCESS
  }

  fun makeHole(level: Level, blockPos: BlockPos) {
    if (level.getBlockEntity(blockPos) != null) return

    val state = level.getBlockState(blockPos)
    level.setBlock(blockPos, T7Blocks.HOLE.get().defaultBlockState(), Block.UPDATE_CLIENTS)
    level.getBE(blockPos, T7BlockEntities.HOLE.get())?.originalState = state
  }
}
