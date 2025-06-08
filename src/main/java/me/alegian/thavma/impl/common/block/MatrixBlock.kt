package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.INFUSION_STATE
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
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
      val infusionState = get(INFUSION_STATE) ?: return InteractionResult.PASS

      attemptConvertPillars()
      if (!infusionState.active) {
        checkActivation()
        return InteractionResult.SUCCESS
      }
      if (!infusionState.isOpen) {
        open()
        return InteractionResult.SUCCESS
      }
      attemptInfusion()
    }
    return InteractionResult.SUCCESS
  }

  override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    super.tick(state, level, pos, random)
    level.getBE(pos, MATRIX.get())?.checkActivation()
  }

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
    return BaseEntityBlock.createTickerHelper(type, MATRIX.get(), MatrixBE::tick)
  }

  override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = MatrixBE(pos, blockState, true)

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true
}
