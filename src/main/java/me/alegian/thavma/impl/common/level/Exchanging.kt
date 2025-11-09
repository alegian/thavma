package me.alegian.thavma.impl.common.level

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.util.*

class Exchanging(
  val player: ServerPlayer,
  val stack: ItemStack,
  val oldBlock: Block,
  val newBlock: Block,
  val positions: Queue<BlockPos>
) {
  companion object {
    private val instances = mutableListOf<Exchanging>()

    fun exchange(level: Level, player: ServerPlayer, pos: BlockPos, oldBlock: Block, newBlock: Block) {
      instances.add(Exchanging(player, player.mainHandItem, oldBlock, newBlock, exchangingPositions(level, pos, oldBlock)))
    }

    fun exchangingPositions(level: LevelAccessor, origin: BlockPos, block: Block): Queue<BlockPos> {
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
              if (block != level.getBlockState(mutablePos).block) continue
              val immutable = mutablePos.immutable()
              if (checking.contains(immutable)) continue
              checking.add(immutable)
            }
      }

      return ArrayDeque(visited)
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      val level = event.level
      if (level !is ServerLevel) return
      if (event.level.gameTime % 3 != 0L) return

      val instanceIterator = instances.iterator()
      while (instanceIterator.hasNext()) {
        val instance = instanceIterator.next()
        if (instance.player.level() != level) continue
        if (
          instance.player.isRemoved ||
          instance.positions.isEmpty() ||
          instance.stack != instance.player.mainHandItem
        ) {
          instanceIterator.remove()
          continue
        }
        val pos = instance.positions.remove()
        val oldState = level.getBlockState(pos)
        if (oldState.block != instance.oldBlock) continue
        if (level.getBlockEntity(pos) != null) continue
        val newState = instance.newBlock.defaultBlockState()
        event.level.setBlockAndUpdate(pos, newState)
        Block.getDrops(oldState, level, pos, null).forEach {
          ItemHandlerHelper.giveItemToPlayer(instance.player, it)
        }
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