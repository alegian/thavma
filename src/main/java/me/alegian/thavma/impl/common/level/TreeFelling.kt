package me.alegian.thavma.impl.common.level

import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import java.util.*

class TreeFelling(
  val player: ServerPlayer,
  val blockState: BlockState,
  val positions: Queue<BlockPos>
) {
  companion object {
    val instances = mutableListOf<TreeFelling>()
    var disableEvents = false

    fun blockBreak(event: BlockEvent.BreakEvent) {
      if (disableEvents) return
      val player = event.player
      if (player !is ServerPlayer) return
      if (player.isShiftKeyDown) return
      if (!player.mainHandItem.`is`(T7Tags.Items.TREE_FELLING)) return
      if (!event.state.`is`(BlockTags.LOGS)) return

      instances.add(TreeFelling(player, event.state, fellingPositions(event.level, event.pos, event.state)))
    }

    fun fellingPositions(level: LevelAccessor, origin: BlockPos, state: BlockState): Queue<BlockPos> {
      val checking = mutableListOf<BlockPos>()
      checking.add(origin)
      val mutablePos = BlockPos.MutableBlockPos()
      val visited = mutableSetOf<BlockPos>()

      while (!checking.isEmpty() && visited.size < 512) {
        val currPos = checking.removeFirst()
        visited.add(currPos)
        for (i in -1..1)
          for (j in -1..1)
            for (k in -1..1) {
              if (i == 0 && j == 0 && k == 0) continue
              mutablePos.set(currPos.x + i, currPos.y + j, currPos.z + k)
              if (visited.contains(mutablePos)) continue
              if (state.block != level.getBlockState(mutablePos).block) continue
              val immutable = mutablePos.immutable()
              if (checking.contains(immutable)) continue
              checking.add(immutable)
            }
      }

      return ArrayDeque(visited.sortedBy { v -> v.y })
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      if (event.level.isClientSide) return
      if (event.level.gameTime % 2 != 0L) return

      disableEvents = true
      val instanceIterator = instances.iterator()
      while (instanceIterator.hasNext()) {
        val instance = instanceIterator.next()
        if (instance.player.level() != event.level) continue
        if (
          instance.player.isRemoved ||
          instance.positions.isEmpty()
        ) {
          instanceIterator.remove()
          continue
        }
        val pos = instance.positions.remove()
        if (event.level.getBlockState(pos).block != instance.blockState.block) continue
        instance.player.gameMode.destroyBlock(pos)
      }
      disableEvents = false
    }
  }
}