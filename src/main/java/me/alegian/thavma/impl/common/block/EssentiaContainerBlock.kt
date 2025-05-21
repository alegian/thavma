package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.EssentiaContainerBE
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.item.ShardItem
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext

private val SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0)

class EssentiaContainerBlock : Block(Properties.ofFullCopy(Blocks.GLASS)), EntityBlock {
  override fun newBlockEntity(pos: BlockPos, state: BlockState) = EssentiaContainerBE(pos, state)

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

  override fun useItemOn(stack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult {
    val shard = stack.item
    if (shard !is ShardItem) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

    val transferred = AspectContainer.at(level, pos)?.insert(shard.aspect.get(), 8, level.isClientSide)
    if (transferred == null || transferred <= 0) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    if (!level.isClientSide && level is ServerLevel) level.updateBlockEntityS2C(pos)

    return ItemInteractionResult.SUCCESS
  }
}