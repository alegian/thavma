package me.alegian.thavma.impl.init.data.worldgen.tree

import me.alegian.thavma.impl.init.data.worldgen.tree.trunk.GreatwoodTrunkPlacer
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.rl
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.*
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.*

object GreatwoodTree {
  private const val NAME = "greatwood"
  private val LOCATION = rl("tree_$NAME")
  private val CONFIGURED_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, LOCATION)
  val GROWER = TreeGrower(NAME, Optional.of(CONFIGURED_FEATURE), Optional.empty(), Optional.empty())
  private val PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, LOCATION)
  private val BIOME_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, LOCATION)

  private fun createGreatwood(): TreeConfigurationBuilder {
    return TreeConfigurationBuilder(
      BlockStateProvider.simple(GREATWOOD_LOG.get()),
      GreatwoodTrunkPlacer(24, 2, 6),
      BlockStateProvider.simple(GREATWOOD_LEAVES.get()),
      BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
      TwoLayersFeatureSize(1, 0, 1)
    )
  }

  fun registerConfigured(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    context.register(
      CONFIGURED_FEATURE,
      ConfiguredFeature(Feature.TREE, createGreatwood().ignoreVines().build())
    )
  }

  fun registerPlaced(context: BootstrapContext<PlacedFeature>) {
    val otherRegistry = context.lookup(Registries.CONFIGURED_FEATURE)

    context.register(
      PLACED_FEATURE, PlacedFeature(
        otherRegistry.getOrThrow(CONFIGURED_FEATURE),
        listOf(
          RarityFilter.onAverageOnceEvery(64),
          InSquarePlacement.spread(),
          SurfaceWaterDepthFilter.forMaxDepth(0),
          PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
          BiomeFilter.biome(),
          PlacementUtils.filteredByBlockSurvival(T7Blocks.GREATWOOD_SAPLING.get())
        )
      )
    )
  }

  fun registerBiomeModifier(context: BootstrapContext<BiomeModifier>) {
    val placedFeatureRegistry = context.lookup(Registries.PLACED_FEATURE)
    val biomeRegistry = context.lookup(Registries.BIOME)

    context.register(
      BIOME_MODIFIER,
      AddFeaturesBiomeModifier(
        biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD),
        HolderSet.direct(placedFeatureRegistry.getOrThrow(PLACED_FEATURE)),
        GenerationStep.Decoration.VEGETAL_DECORATION
      )
    )
  }
}
