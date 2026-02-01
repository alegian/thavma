package me.alegian.thavma.impl.common.level

import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

object Excavation {
  const val RANGE = 10.0
  private val instances = mutableMapOf<Int, ExcavationProgress>()

  fun excavate(level: Level, player: Player, hitResult: BlockHitResult, speed: Int) {
    if (level.isClientSide || player !is ServerPlayer) return
    if (hitResult.type == HitResult.Type.MISS) return

    val blockPos = hitResult.blockPos
    val blockState = level.getBlockState(blockPos)

    val progressObject = instances.compute(player.id) { _, v ->
      if (v == null || v.blockPos != blockPos || v.blockState != blockState)
        ExcavationProgress(blockPos, blockState, speed)
      else
        ExcavationProgress(blockPos, blockState, (v.progress + speed).coerceIn(0, 10))
    } ?: return

    level.destroyBlockProgress(-player.id, blockPos, progressObject.progress)

    if (progressObject.progress < 10) return
    player.gameMode.destroyBlock(blockPos)
    // due to the internals of the previous function, we need to separately send sound to the breaking player
    player.connection.send(ClientboundLevelEventPacket(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState), false))
  }

  fun stopExcavation(level: Level, player: Player) {
    if (level.isClientSide) return
    instances.remove(player.id)
  }
}

data class ExcavationProgress(
  val blockPos: BlockPos,
  val blockState: BlockState,
  val progress: Int
)