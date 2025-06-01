package me.alegian.thavma.impl.common.infusion

import me.alegian.thavma.impl.common.aspect.AspectStack
import net.minecraft.core.BlockPos

/**
 * represents a tick of infusion essentia flying
 */
data class ArrivingAspectStack(val blockPos: BlockPos, val aspectStack: AspectStack)