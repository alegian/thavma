package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.MATRIX
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult

class MatrixBlock : Block(Properties.ofFullCopy(Blocks.STONE).noOcclusion().pushReaction(PushReaction.BLOCK)), EntityBlock {
  override fun useWithoutItem(
    state: BlockState,
    level: Level,
    pos: BlockPos,
    player: Player,
    hitResult: BlockHitResult
  ): InteractionResult {
    level.getBE(pos, MATRIX.get())?.run {
      attemptConvertPillars()
      checkActivation()
    }
    return InteractionResult.SUCCESS
  }

  override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    super.tick(state, level, pos, random)
    level.getBE(pos, MATRIX.get())?.checkActivation()
  }

  override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = MatrixBE(pos, blockState, true)

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true
}
