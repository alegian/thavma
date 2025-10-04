package me.alegian.thavma.impl.common.level

import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import java.util.ArrayDeque
import java.util.Queue

class TreeFelling(
  val player: Player,
  val blockState: BlockState,
  val positions: Queue<BlockPos>
) {
  companion object {
    val instances = mutableListOf<TreeFelling>()

    fun blockBreak(event: BlockEvent.BreakEvent) {
      val player = event.player
      if (player !is ServerPlayer) return
      if (player.isShiftKeyDown) return
      if (!player.mainHandItem.`is`(T7Tags.Items.TREE_FELLING)) return
      if (!event.state.`is`(BlockTags.LOGS)) return

      instances.add(TreeFelling(player, event.state, fellingPositions()))
    }

    fun fellingPositions(){
      val queue = ArrayDeque<BlockPos>()
      val mutablePos = BlockPos.MutableBlockPos()

      for (i in -1..1)
        for (j in -1..1)
          for (k in -1..1) {
            mutablePos.set(i, j, k)
          }
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      if (event.level.isClientSide) return
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
      }
    }
  }
}