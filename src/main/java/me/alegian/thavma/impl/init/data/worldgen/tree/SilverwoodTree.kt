package me.alegian.thavma.impl.init.data.worldgen.tree

import me.alegian.thavma.impl.Thavma.rl
import me.alegian.thavma.impl.init.data.worldgen.tree.trunk.SilverwoodTrunkPlacer
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.*
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.*

object SilverwoodTree {
  const val NAME = "silverwood"
  private val LOCATION = rl("tree_$NAME")
  private val CONFIGURED_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, LOCATION)
  val GROWER = TreeGrower(NAME, Optional.empty(), Optional.of(CONFIGURED_FEATURE), Optional.empty())
  private val PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, LOCATION)
  private val BIOME_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, LOCATION)

  private fun createSilverwood(): TreeConfigurationBuilder {
    return TreeConfigurationBuilder(
      BlockStateProvider.simple(SILVERWOOD_LOG.get()),
      SilverwoodTrunkPlacer(9, 3, 0),
      BlockStateProvider.simple(SILVERWOOD_LEAVES.get()),
      CherryFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(10), 0f, 0.3f, 0f, 0f),
      TwoLayersFeatureSize(2, 2, 1)
    )
  }

  fun registerConfigured(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    context.register(
      CONFIGURED_FEATURE,
      ConfiguredFeature(Feature.TREE, createSilverwood().ignoreVines().build())
    )
  }

  fun registerPlaced(context: BootstrapContext<PlacedFeature>) {
    val otherRegistry = context.lookup(Registries.CONFIGURED_FEATURE)

    context.register(
      PLACED_FEATURE, PlacedFeature(
        otherRegistry.getOrThrow(CONFIGURED_FEATURE),
        listOf(
          RarityFilter.onAverageOnceEvery(9),
          InSquarePlacement.spread(),
          SurfaceWaterDepthFilter.forMaxDepth(0),
          PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
          BiomeFilter.biome(),
          PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
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
