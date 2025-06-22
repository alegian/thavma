package me.alegian.thavma.impl.common.feature

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class NodeFeature: Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {
  override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
    val blockPos = context.origin()
    val level = context.level()

    level.setBlock(blockPos.above(), T7Blocks.AURA_NODE.get().defaultBlockState(), Block.UPDATE_CLIENTS)

    return true
  }
}