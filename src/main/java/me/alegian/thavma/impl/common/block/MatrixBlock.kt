package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.block.entity.itemHandler
import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.INFUSION_STATE
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.items.ItemHandlerHelper.giveItemToPlayer

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
    }
    return InteractionResult.PASS
  }

  override fun useItemOn(handStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult {
    val be = level.getBE(pos, MATRIX.get())
    val itemHandler = be?.itemHandler
    val infusionState = be?.get(INFUSION_STATE)
    if (itemHandler == null || infusionState == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    val matrixStack = itemHandler.getStackInSlot(0)

    if (handStack.item is WandItem && infusionState.isOpen && !matrixStack.isEmpty) {
      return if (be.attemptInfusion()) ItemInteractionResult.SUCCESS
      else ItemInteractionResult.FAIL
    }

    if (!infusionState.isOpen) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

    if (matrixStack.isEmpty && !handStack.isEmpty) {
      itemHandler.insertItem(0, handStack.copyWithCount(1), false)
      handStack.shrink(1)
      level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1f, 1f)
    } else if (!matrixStack.isEmpty) {
      val stackFromMatrix = itemHandler.extractItem(0, 1, false)
      giveItemToPlayer(player, stackFromMatrix)
      level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1f, 1f)
    } else return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    if (!level.isClientSide && level is ServerLevel) level.updateBlockEntityS2C(pos)

    return ItemInteractionResult.SUCCESS
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
