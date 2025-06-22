package me.alegian.thavma.impl.init.data.worldgen

import me.alegian.thavma.impl.init.registries.deferred.T7Features
import me.alegian.thavma.impl.rl
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

object Node {
  private val LOCATION = rl("node")
  private val CONFIGURED_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, LOCATION)
  private val PLACED_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, LOCATION)
  private val BIOME_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, LOCATION)

  fun registerConfigured(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    context.register(
      CONFIGURED_FEATURE,
      ConfiguredFeature(T7Features.NODE.get(), NoneFeatureConfiguration.INSTANCE)
    )
  }

  fun registerPlaced(context: BootstrapContext<PlacedFeature>) {
    val otherRegistry = context.lookup(Registries.CONFIGURED_FEATURE)

    context.register(
      PLACED_FEATURE, PlacedFeature(
        otherRegistry.getOrThrow(CONFIGURED_FEATURE),
        listOf(
          RarityFilter.onAverageOnceEvery(48),
          InSquarePlacement.spread(),
          PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
          BiomeFilter.biome(),
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
        GenerationStep.Decoration.SURFACE_STRUCTURES
      )
    )
  }
}
