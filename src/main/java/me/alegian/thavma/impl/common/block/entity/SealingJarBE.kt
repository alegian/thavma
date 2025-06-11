package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.SEALING_JAR
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class SealingJarBE(pos: BlockPos, blockState: BlockState) : DataComponentBE(SEALING_JAR.get(), pos, blockState)