package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.SealingJarBE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext

private val SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0)

class SealingJarBlock : Block(Properties.ofFullCopy(Blocks.GLASS)), EntityBlock {
  override fun newBlockEntity(pos: BlockPos, state: BlockState) = SealingJarBE(pos, state)

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE
}