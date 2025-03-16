package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.PedestalBE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Block.box
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.shapes.CollisionContext

private val SHAPE = box(5.0, 0.0, 5.0, 11.0, 13.0, 11.0)

class PedestalBlock : Block(Properties.ofFullCopy(Blocks.STONE).noOcclusion().pushReaction(PushReaction.BLOCK)), EntityBlock {
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE

    override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED

    override fun newBlockEntity(pos: BlockPos, blockState: BlockState) = PedestalBE(pos, blockState)

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos) = true
}
