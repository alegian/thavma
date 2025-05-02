package me.alegian.thavma.impl.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

private val SHAPE = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0)

class ItemHatch : Block(Properties.ofFullCopy(Blocks.IRON_TRAPDOOR)) {
  override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    if (context is EntityCollisionContext && context.entity is ItemEntity) return Shapes.empty()
    return super.getCollisionShape(state, level, pos, context)
  }

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = SHAPE
}