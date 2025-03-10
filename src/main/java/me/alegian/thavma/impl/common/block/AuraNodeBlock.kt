package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.AuraNodeBE
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.AURA_NODE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState


class AuraNodeBlock : TransparentBlock(
  Properties.of()
    .noTerrainParticles()
    .strength(2f)
    .sound(SoundType.WOOL)
    .noCollission()
    .noOcclusion()
), EntityBlock {
  public override fun getRenderShape(blockState: BlockState): RenderShape {
    return RenderShape.ENTITYBLOCK_ANIMATED
  }

  override fun newBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity {
    return AuraNodeBE(pos, blockState)
  }

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
    return BaseEntityBlock.createTickerHelper(type, AURA_NODE.get(), AuraNodeBE::tick)
  }

  override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
    val be = level.getBE(pos, AURA_NODE.get())
    if (state != newState.block && be != null)
      be.dropItems()

    super.onRemove(state, level, pos, newState, isMoving)
  }
}
