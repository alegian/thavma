package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.block.entity.WorkbenchBE
import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.WORKBENCH
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class WorkbenchBlock : Block(Properties.ofFullCopy(Blocks.STONE).noOcclusion()), EntityBlock {
  override fun useWithoutItem(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHitResult: BlockHitResult): InteractionResult {
    if (pLevel.isClientSide) return InteractionResult.SUCCESS
    else {
      pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos))
      return InteractionResult.CONSUME
    }
  }

  public override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

  override fun getMenuProvider(pState: BlockState, pLevel: Level, pPos: BlockPos): MenuProvider {
    return SimpleMenuProvider(
      { pContainerId, pPlayerInventory, player -> WorkbenchMenu(pContainerId, pPlayerInventory, ContainerLevelAccess.create(pLevel, pPos)) },
      Component.translatable(CONTAINER_TITLE)
    )
  }

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = WorkbenchBE(pos, blockState)

  override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
    return if (!level.isClientSide) BaseEntityBlock.createTickerHelper(blockEntityType, WORKBENCH.get(), WorkbenchBE::serverTick) else null
  }

  companion object {
    val CONTAINER_TITLE = "container." + Thavma.MODID + ".arcane_workbench"
  }
}
