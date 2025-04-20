package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
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

private val animKeys = listOf("closed", "open", "spin_open", "spin_closed", "spin_closed_fast")

class MatrixBlock : Block(Properties.ofFullCopy(Blocks.STONE).noOcclusion().pushReaction(PushReaction.BLOCK)), EntityBlock {
    companion object {
        var debugCycle = 0
    }

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {
        if (!pLevel.isClientSide()) pLevel.getBlockEntity(pPos, T7BlockEntities.MATRIX.get()).ifPresent {
            it.triggerAnim(
                "cycle",
                animKeys[debugCycle++ % animKeys.size]
            )
        }
        return InteractionResult.SUCCESS
    }

    override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

    override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = MatrixBE(pos, blockState, true)

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true
}
