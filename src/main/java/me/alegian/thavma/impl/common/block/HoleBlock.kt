package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.HoleBE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class HoleBlock : Block(
  Properties.of()
    .noCollission()
    .noOcclusion()
    .noTerrainParticles()
    .strength(-1.0F, 3600000.0F)
    .lightLevel { 7 }
    .isValidSpawn(Blocks::never)
    .noLootTable()
), EntityBlock {
  override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HoleBE(pos, state)

  override fun getRenderShape(state: BlockState) = RenderShape.INVISIBLE
}
