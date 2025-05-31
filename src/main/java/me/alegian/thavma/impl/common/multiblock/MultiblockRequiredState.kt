package me.alegian.thavma.impl.common.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

data class MultiblockRequiredState(val blockPos: BlockPos, val blockState: BlockState)