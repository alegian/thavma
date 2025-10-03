package me.alegian.thavma.impl.common.level

import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

class TreeFelling(
  val player: Player,
  val level: Level,
  val blockState: BlockState
) {
  val queue = ArrayDeque<BlockPos>()

  companion object {
    val instances = mutableListOf<TreeFelling>()

    fun blockBreak(event: BlockEvent.BreakEvent) {
      val player = event.player
      if (player !is ServerPlayer) return
      if (player.isShiftKeyDown) return
      if (!event.player.mainHandItem.`is`(T7Tags.Items.TREE_FELLING)) return
      if (!event.level.getBlockState(event.pos).`is`(BlockTags.LOGS)) return
    }

    fun levelTick(event: LevelTickEvent.Pre) {
      if (event.level.isClientSide) return
      for (instance in instances) {
        if (instance.level != event.level) return

      }
    }
  }
}