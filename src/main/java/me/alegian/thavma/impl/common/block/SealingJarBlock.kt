package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.SealingJarBE
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
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

class SealingJarBlock : Block(Properties.ofFullCopy(Blocks.GLASS)), EntityBlock {
  override fun newBlockEntity(pos: BlockPos, state: BlockState) = SealingJarBE(pos, state)

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

  override fun useItemOn(stack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult {
    if (stack.item == T7Blocks.SEALING_JAR.asItem()) {
      var result = AspectContainer.itemSourceBlockSink(level, pos, stack)?.transferAnyAspect()
      if (result != null) {
        player.playSound(SoundEvents.BUCKET_EMPTY)
        return ItemInteractionResult.SUCCESS
      }
      result = AspectContainer.blockSourceItemSink(level, pos, stack)?.transferAnyAspect()
      if (result != null) {
        player.playSound(SoundEvents.BUCKET_FILL)
        return ItemInteractionResult.SUCCESS
      }
    }

    return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
  }
}