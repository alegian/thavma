package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block

class HoleFocus : Item(Properties().stacksTo(1)) {
  override fun useOn(context: UseOnContext): InteractionResult {
    val level = context.level
    if (level.isClientSide) return InteractionResult.SUCCESS

    val direction = context.clickedFace.opposite
    val perpendicularAxes = Direction.Axis.entries.filter { it !== direction.axis }

    for (k in 0..8) {
      var noneReplaced = true
      for (i in -1..1)
        for (j in -1..1) {
          val blockPos = context.clickedPos
            .relative(direction, k)
            .relative(perpendicularAxes[0], i)
            .relative(perpendicularAxes[1], j)

          if (level.getBlockEntity(blockPos) != null) continue
          val state = level.getBlockState(blockPos)
          if (state.isAir) continue

          level.setBlock(blockPos, T7Blocks.HOLE.get().defaultBlockState(), Block.UPDATE_CLIENTS)
          level.getBE(blockPos, T7BlockEntities.HOLE.get())?.run {
            originalState = state
            this.direction = direction
          }
          noneReplaced = false
        }
      if (noneReplaced) break
    }
    return InteractionResult.SUCCESS
  }
}
