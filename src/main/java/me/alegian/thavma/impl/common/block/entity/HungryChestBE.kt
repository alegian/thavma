package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.state.BlockState

// default values used in item renderer
class HungryChestBE(
  pos: BlockPos = BlockPos.ZERO,
  state: BlockState = T7Blocks.HUNGRY_CHEST.get().defaultBlockState()
): ChestBlockEntity(T7BlockEntities.HUNGRY_CHEST.get(), pos, state) {
}