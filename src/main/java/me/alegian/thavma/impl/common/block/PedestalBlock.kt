package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.PedestalBE
import me.alegian.thavma.impl.common.block.entity.itemHandler
import me.alegian.thavma.impl.common.data.capability.firstStack
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PEDESTAL
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Block.box
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.neoforged.neoforge.items.ItemHandlerHelper.giveItemToPlayer

private val SHAPE = box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0)

class PedestalBlock : Block(Properties.ofFullCopy(Blocks.STONE).noOcclusion().pushReaction(PushReaction.BLOCK)), EntityBlock {
  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

  override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = PedestalBE(pos, blockState)

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true

  override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
    val be = level.getBE(pos, PEDESTAL.get())
    if (state != newState.block && be != null) {
      val stack = be.itemHandler?.firstStack ?: return
      Containers.dropContents(level, pos, NonNullList.copyOf(listOf(stack)))
    }

    super.onRemove(state, level, pos, newState, isMoving)
  }

  protected override fun useItemOn(handStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult {
    val be = level.getBE(pos, PEDESTAL.get())
    val itemHandler = be?.itemHandler ?: return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    val pedestalStack = itemHandler.firstStack

    if (pedestalStack.isEmpty && !handStack.isEmpty) {
      itemHandler.insertItem(0, handStack.copyWithCount(1), false)
      handStack.shrink(1)
      level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1f, 1f)
    } else if (!pedestalStack.isEmpty) {
      val stackFromPedestal = itemHandler.extractItem(0, 1, false)
      giveItemToPlayer(player, stackFromPedestal)
      level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1f, 1f)
    } else return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    if (!level.isClientSide && level is ServerLevel) level.updateBlockEntityS2C(pos)

    return ItemInteractionResult.SUCCESS
  }
}
