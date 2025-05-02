package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.ESSENTIA_CONTAINER
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.ASPECTS
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.block.state.BlockState

class EssentiaContainerBE(pos: BlockPos, blockState: BlockState): DataComponentBE(ESSENTIA_CONTAINER.get(), pos, blockState) {
  override fun getComponentTypes(): Array<DataComponentType<*>> = arrayOf(ASPECTS.get())
}