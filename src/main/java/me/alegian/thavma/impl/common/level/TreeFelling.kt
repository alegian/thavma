package me.alegian.thavma.impl.common.level

import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import java.util.ArrayDeque
import java.util.Deque

class TreeFelling(
  val player: ServerPlayer,
  val blockState: BlockState,
  val positions: Deque<BlockPos>
) {
  companion object {
    val instances = mutableListOf<TreeFelling>()

    fun blockBreak(event: BlockEvent.BreakEvent) {
      val player = event.player
      if (player !is ServerPlayer) return
      if (player.isShiftKeyDown) return
      if (!player.mainHandItem.`is`(T7Tags.Items.TREE_FELLING)) return
      if (!event.state.`is`(BlockTags.LOGS)) return

      instances.add(TreeFelling(player, event.state, fellingPositions(event.level, event.pos, event.state)))
    }

    fun fellingPositions(level: LevelAccessor, origin: BlockPos, state: BlockState): Deque<BlockPos> {
      val checking = ArrayDeque<BlockPos>()
      checking.add(origin)
      val mutablePos = BlockPos.MutableBlockPos()
      val visited = ArrayDeque<BlockPos>()

      while (!checking.isEmpty()) {
        val currPos = checking.remove()
        visited.add(currPos)
        for (i in -1..1)
          for (j in -1..1)
            for (k in -1..1) {
              if (i == 0 && j == 0 && k == 0) continue
              mutablePos.set(origin.x + i, origin.y + j, origin.z + k)
              if (visited.contains(mutablePos)) continue
              if (state.block != level.getBlockState(mutablePos).block) continue
              checking.add(mutablePos.immutable())
            }
      }

      return visited
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      if (event.level.isClientSide) return
      if (event.level.gameTime % 5 != 0L) return

      val instanceIterator = instances.iterator()
      while (instanceIterator.hasNext()) {
        val instance = instanceIterator.next()
        if (
          instance.player.isRemoved ||
          instance.player.level() != event.level ||
          instance.positions.isEmpty()
        ) {
          instanceIterator.remove()
          continue
        }
        instance.player.gameMode.destroyBlock(instance.positions.removeLast())
      }
    }
  }
}