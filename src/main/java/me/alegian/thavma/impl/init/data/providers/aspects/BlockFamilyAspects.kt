package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.T7BlockFamilies
import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.blockFamily
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALIENIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERMUTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAECANTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import net.minecraft.core.HolderLookup
import net.minecraft.data.BlockFamilies

object BlockFamilyAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) =
    datamapProvider.builder(T7DataMaps.AspectContent.BLOCK).run {
      blockFamily(BlockFamilies.ACACIA_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.CHERRY_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.BIRCH_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.CRIMSON_PLANKS) {
        it.add(HERBA, 2).add(IGNIS, 2)
      }
      blockFamily(BlockFamilies.JUNGLE_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.OAK_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.DARK_OAK_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.SPRUCE_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.WARPED_PLANKS) {
        it.add(HERBA, 2).add(IGNIS, 2)
      }
      blockFamily(BlockFamilies.MANGROVE_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.BAMBOO_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.BAMBOO_MOSAIC) {
        it.add(HERBA, 2)
      }
      blockFamily(BlockFamilies.MUD_BRICKS) {
        it.add(TERRA, 2).add(HERBA, 2)
      }
      blockFamily(BlockFamilies.ANDESITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.POLISHED_ANDESITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.BLACKSTONE) {
        it.add(TERRA, 2).add(IGNIS, 2)
      }
      blockFamily(BlockFamilies.POLISHED_BLACKSTONE) {
        it.add(TERRA, 2).add(IGNIS, 2)
      }
      blockFamily(BlockFamilies.POLISHED_BLACKSTONE_BRICKS) {
        it.add(TERRA, 2).add(IGNIS, 2)
      }
      blockFamily(BlockFamilies.BRICKS) {
        it.add(TERRA, 4).add(IGNIS, 4)
      }
      blockFamily(BlockFamilies.END_STONE_BRICKS) {
        it.add(TERRA, 2).add(ALIENIS, 2)
      }
      blockFamily(BlockFamilies.MOSSY_STONE_BRICKS) {
        it.add(TERRA, 2).add(HERBA, 2)
      }
      blockFamily(BlockFamilies.COPPER_BLOCK) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_COPPER_BLOCK) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.EXPOSED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.EXPOSED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_EXPOSED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_EXPOSED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WEATHERED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WEATHERED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_WEATHERED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_WEATHERED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.OXIDIZED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.OXIDIZED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_OXIDIZED_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.WAXED_OXIDIZED_CUT_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      blockFamily(BlockFamilies.COBBLESTONE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.MOSSY_COBBLESTONE) {
        it.add(TERRA, 2).add(HERBA, 2)
      }
      blockFamily(BlockFamilies.DIORITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.POLISHED_DIORITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.GRANITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.POLISHED_GRANITE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.TUFF) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.POLISHED_TUFF) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.TUFF_BRICKS) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.NETHER_BRICKS) {
        it.add(TERRA, 4).add(IGNIS, 4)
      }
      blockFamily(BlockFamilies.RED_NETHER_BRICKS) {
        it.add(TERRA, 2).add(IGNIS, 2).add(HERBA, 2)
      }
      blockFamily(BlockFamilies.PRISMARINE) {
        it.add(AQUA, 4).add(TERRA, 4)
      }
      blockFamily(BlockFamilies.PURPUR) {
        it.add(ALIENIS, 2).add(HERBA, 2)
      }
      blockFamily(BlockFamilies.PRISMARINE_BRICKS) {
        it.add(AQUA, 8).add(TERRA, 8)
      }
      blockFamily(BlockFamilies.DARK_PRISMARINE) {
        it.add(AQUA, 4).add(TERRA, 4)
      }
      blockFamily(BlockFamilies.QUARTZ) {
        it.add(VITREUS, 16)
          .add(IGNIS, 4)
      }
      blockFamily(BlockFamilies.SMOOTH_QUARTZ) {
        it.add(VITREUS, 16)
          .add(IGNIS, 4)
      }
      blockFamily(BlockFamilies.SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.CUT_SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.SMOOTH_SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.RED_SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.CUT_RED_SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.SMOOTH_RED_SANDSTONE) {
        it.add(TERRA, 4)
      }
      blockFamily(BlockFamilies.STONE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.STONE_BRICK) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.DEEPSLATE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.COBBLED_DEEPSLATE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.POLISHED_DEEPSLATE) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.DEEPSLATE_BRICKS) {
        it.add(TERRA, 2)
      }
      blockFamily(BlockFamilies.DEEPSLATE_TILES) {
        it.add(TERRA, 2)
      }
      blockFamily(T7BlockFamilies.GREATWOOD_PLANKS) {
        it.add(HERBA, 2)
      }
      blockFamily(T7BlockFamilies.ELEMENTAL_STONE) {
        it.add(TERRA, 4)
          .add(PRAECANTATIO, 1)
      }
    }
}