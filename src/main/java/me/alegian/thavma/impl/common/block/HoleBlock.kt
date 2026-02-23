package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.HoleBE
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.common.util.TriState

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

  override fun canSustainPlant(state: BlockState, level: BlockGetter, soilPosition: BlockPos, facing: Direction, plant: BlockState) = TriState.TRUE

  override fun getCloneItemStack(state: BlockState, target: HitResult, level: LevelReader, pos: BlockPos, player: Player) = ItemStack.EMPTY
}
