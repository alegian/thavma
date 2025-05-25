package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.EternalFlameBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.PushReaction

class EternalFlame : Block(
  Properties.of()
    .noCollission()
    .instabreak()
    .sound(SoundType.WOOL)
    .lightLevel { 15 }
    .pushReaction(PushReaction.DESTROY)
) , EntityBlock{
  override fun newBlockEntity(pos: BlockPos, state: BlockState) = EternalFlameBE(pos, state)

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
    return if (type === T7BlockEntities.ETERNAL_FLAME.get()) BlockEntityTicker(EternalFlameBE::tick) else null
  }

  override fun getRenderShape(state: BlockState) = RenderShape.ENTITYBLOCK_ANIMATED
}