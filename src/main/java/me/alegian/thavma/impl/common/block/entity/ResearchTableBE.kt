package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.RESEARCH_TABLE
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class ResearchTableBE(pos: BlockPos, blockState: BlockState) :
  BlockEntity(RESEARCH_TABLE.get(), pos, blockState)
