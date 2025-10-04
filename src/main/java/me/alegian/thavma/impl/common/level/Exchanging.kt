package me.alegian.thavma.impl.common.level

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.tick.LevelTickEvent
import java.util.*

class Exchanging(
  val player: ServerPlayer,
  val stack: ItemStack,
  val blockState: BlockState,
  val positions: Queue<BlockPos>
) {
  companion object {
    val instances = mutableListOf<Exchanging>()

    fun exchange(level: Level, player: ServerPlayer, pos: BlockPos) {
      if (player.isShiftKeyDown) return

      val state = level.getBlockState(pos)
      instances.add(Exchanging(player, player.mainHandItem, state, exchangingPositions(level, pos, state)))
    }

    fun exchangingPositions(level: LevelAccessor, origin: BlockPos, state: BlockState): Queue<BlockPos> {
      val checking = mutableListOf<BlockPos>()
      checking.add(origin)
      val mutablePos = BlockPos.MutableBlockPos()
      val visited = mutableSetOf<BlockPos>()

      while (!checking.isEmpty() && visited.size < 64) {
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

      return ArrayDeque(visited)
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      if (event.level.isClientSide) return
      if (event.level.gameTime % 3 != 0L) return

      val instanceIterator = instances.iterator()
      while (instanceIterator.hasNext()) {
        val instance = instanceIterator.next()
        if (instance.player.level() != event.level) continue
        if (
          instance.player.isRemoved ||
          instance.positions.isEmpty() ||
          instance.stack != instance.player.mainHandItem
        ) {
          instanceIterator.remove()
          continue
        }
        val pos = instance.positions.remove()
        if (event.level.getBlockState(pos).block != instance.blockState.block) continue
        val newState = Blocks.QUARTZ_BLOCK.defaultBlockState()
        event.level.setBlockAndUpdate(pos, newState)
        val soundType = newState.getSoundType(event.level, pos, instance.player)
        event.level.playSound(
          null,
          pos,
          soundType.placeSound,
          SoundSource.BLOCKS,
          (soundType.getVolume() + 1.0f) / 2.0f,
          soundType.getPitch() * 0.8f
        )
      }
    }
  }
}