package me.alegian.thavma.impl.init.data.worldgen.ore

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest

/**
 * Helpers for registering the 6 different aspects of InfusedStoneOre.
 */
fun registerConfiguredInfusedStone(context: BootstrapContext<ConfiguredFeature<*, *>>, key: ResourceKey<ConfiguredFeature<*, *>>?, stoneOre: BlockState?, deepSlateOre: BlockState?): Holder<ConfiguredFeature<*, *>> {
  if (stoneOre == null || deepSlateOre == null) throw IllegalStateException("Thavma Exception: Null block when registering configured feature")
  if (key == null) throw IllegalStateException("Thavma Exception: Null key when registering configured feature")

  val stoneReplaceables = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
  val deepslateReplaceables = TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)

  val list = listOf(
    OreConfiguration.target(stoneReplaceables, stoneOre),
    OreConfiguration.target(deepslateReplaceables, deepSlateOre)
  )

  return context.register(
    key,
    ConfiguredFeature(Feature.ORE, OreConfiguration(list, 7))
  )
}

fun registerPlacedInfusedStone(context: BootstrapContext<PlacedFeature>, configuredKey: ResourceKey<ConfiguredFeature<*, *>>?, placedKey: ResourceKey<PlacedFeature>?): Holder<PlacedFeature> {
  if (configuredKey == null) throw IllegalStateException("Thavma Exception: Null configured key when registering placed feature")
  if (placedKey == null) throw IllegalStateException("Thavma Exception: Null placed key when registering placed feature")

  val otherRegistry = context.lookup(Registries.CONFIGURED_FEATURE)

  return context.register(
    placedKey, PlacedFeature(
      otherRegistry.getOrThrow(configuredKey),
      listOf()
    )
  )
}
