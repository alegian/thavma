package me.alegian.thavma.impl.init.data.worldgen.ore

import me.alegian.thavma.impl.init.data.worldgen.ore.InfusedOre.PLACED_FEATURES
import me.alegian.thavma.impl.init.registries.deferred.listFromPrimals
import me.alegian.thavma.impl.rl
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration
import net.minecraft.world.level.levelgen.placement.*
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * Handles generation of all 6 different Infused Stone Ores. Uses a
 * random feature, and checks all placement criteria here (the individual
 * ores do not have placement filters)
 */
object InfusedStoneOre {
  private const val PATH: String = "ore_infused_stone"
  private val CONFIGURED_FEATURE: ResourceKey<ConfiguredFeature<*, *>> = ResourceKey.create(
    Registries.CONFIGURED_FEATURE,
    rl(PATH)
  )
  private val PLACED_FEATURE: ResourceKey<PlacedFeature> = ResourceKey.create(
    Registries.PLACED_FEATURE,
    rl(PATH)
  )
  private val BIOME_MODIFIER: ResourceKey<BiomeModifier> = ResourceKey.create(
    NeoForgeRegistries.Keys.BIOME_MODIFIERS,
    rl(PATH)
  )

  fun registerConfigured(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    val placedRegistry = context.lookup(Registries.PLACED_FEATURE)
    val everyPrimal = listFromPrimals { aspect ->
      val placedFeature = PLACED_FEATURES[aspect] ?: throw IllegalStateException("Thavma Exception: Null placed when registering configured feature")
      placedRegistry.getOrThrow(placedFeature)
    }

    // 6 ores, equally likely
    context.register(
      CONFIGURED_FEATURE,
      ConfiguredFeature(Feature.SIMPLE_RANDOM_SELECTOR, SimpleRandomFeatureConfiguration((HolderSet.direct(everyPrimal))))
    )
  }

  fun registerPlaced(context: BootstrapContext<PlacedFeature>) {
    val otherRegistry = context.lookup(Registries.CONFIGURED_FEATURE)

    context.register(
      PLACED_FEATURE, PlacedFeature(
        otherRegistry.getOrThrow(CONFIGURED_FEATURE),
        listOf(
          CountPlacement.of(30),
          InSquarePlacement.spread(),
          HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
          BiomeFilter.biome()
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
        GenerationStep.Decoration.UNDERGROUND_ORES
      )
    )
  }
}
