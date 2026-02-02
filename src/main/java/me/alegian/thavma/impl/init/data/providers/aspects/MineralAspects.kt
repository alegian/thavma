package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.INSTRUMENTUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.LUX
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERMUTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.POTENTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAECANTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.SENSUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SHARDS
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object MineralAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Tags.Items.ORES_COAL) {
        it.add(IGNIS, 8)
          .add(POTENTIA, 8)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_COAL) {
        it.add(IGNIS, 72)
          .add(POTENTIA, 72)
      }
      item(Tags.Items.ORES_COPPER) {
        it.add(METALLUM, 4)
          .add(PERMUTATIO, 4)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_RAW_COPPER) {
        it.add(METALLUM, 18)
          .add(PERMUTATIO, 18)
          .add(TERRA, 18)
      }
      item(Tags.Items.STORAGE_BLOCKS_COPPER) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
      }
      item(Tags.Items.ORES_IRON) {
        it.add(METALLUM, 8)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_RAW_IRON) {
        it.add(METALLUM, 36)
          .add(TERRA, 18)
      }
      item(Tags.Items.STORAGE_BLOCKS_IRON) {
        it.add(METALLUM, 72)
      }
      item(T7Blocks.THAVMITE_BLOCK) {
        it.add(METALLUM, 72)
          .add(PRAECANTATIO, 36)
      }
      item(T7Blocks.ORICHALCUM_BLOCK) {
        it.add(METALLUM, 36)
          .add(PERMUTATIO, 36)
          .add(PRAECANTATIO, 36)
      }
      item(Tags.Items.ORES_GOLD) {
        it.add(METALLUM, 4)
          .add(DESIDERIUM, 8)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_RAW_GOLD) {
        it.add(METALLUM, 18)
          .add(DESIDERIUM, 36)
          .add(TERRA, 18)
      }
      item(Tags.Items.STORAGE_BLOCKS_GOLD) {
        it.add(METALLUM, 36)
          .add(DESIDERIUM, 72)
      }
      item(Tags.Items.ORES_DIAMOND) {
        it.add(VITREUS, 8)
          .add(DESIDERIUM, 8)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_DIAMOND) {
        it.add(VITREUS, 72)
          .add(DESIDERIUM, 72)
      }
      item(Tags.Items.ORES_EMERALD) {
        it.add(VITREUS, 8)
          .add(DESIDERIUM, 4)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_EMERALD) {
        it.add(VITREUS, 72)
          .add(DESIDERIUM, 36)
      }
      item(Tags.Items.ORES_NETHERITE_SCRAP) {
        it.add(METALLUM, 2)
          .add(TERRA, 6)
          .add(TENEBRAE, 1)
      }
      item(Tags.Items.STORAGE_BLOCKS_NETHERITE) {
        it.add(METALLUM, 144)
          .add(DESIDERIUM, 72)
          .add(TENEBRAE, 36)
      }
      item(Blocks.GLOWSTONE) {
        it.add(LUX, 16)
          .add(TERRA, 4)
      }
      item(Tags.Items.ORES_QUARTZ) {
        it.add(VITREUS, 4)
          .add(TERRA, 4)
          .add(IGNIS, 1)
      }
      item(Blocks.QUARTZ_BLOCK) {
        it.add(VITREUS, 16)
          .add(IGNIS, 4)
      }
      item(Tags.Items.ORES_LAPIS) {
        it.add(VITREUS, 4)
          .add(TERRA, 4)
          .add(SENSUS, 2)
      }
      item(Tags.Items.STORAGE_BLOCKS_LAPIS) {
        it.add(VITREUS, 36)
          .add(SENSUS, 18)
      }
      item(Tags.Items.ORES_REDSTONE) {
        it.add(POTENTIA, 4)
          .add(TERRA, 4)
      }
      item(Tags.Items.STORAGE_BLOCKS_REDSTONE) {
        it.add(POTENTIA, 36)
          .add(TERRA, 9)
      }
      item(Blocks.AMETHYST_BLOCK) {
        it.add(VITREUS, 16)
          .add(SENSUS, 4)
      }

      for (infusedBlock in (INFUSED_STONES.values + INFUSED_DEEPSLATES.values)) {
        item(infusedBlock) {
          it.add(infusedBlock.get().getAspect(), 4)
        }
      }
    }

    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(ItemTags.COALS) {
        it.add(IGNIS, 8)
          .add(POTENTIA, 8)
      }
      item(Tags.Items.RAW_MATERIALS_COPPER) {
        it.add(METALLUM, 2)
          .add(PERMUTATIO, 2)
          .add(TERRA, 2)
      }
      item(Tags.Items.INGOTS_COPPER) {
        it.add(METALLUM, 4)
          .add(PERMUTATIO, 4)
      }
      item(Tags.Items.RAW_MATERIALS_IRON) {
        it.add(METALLUM, 4)
          .add(TERRA, 2)
      }
      item(Tags.Items.INGOTS_IRON) {
        it.add(METALLUM, 8)
      }
      item(T7Items.THAVMITE_INGOT) {
        it.add(METALLUM, 8)
          .add(PRAECANTATIO, 4)
      }
      item(T7Items.ORICHALCUM_INGOT) {
        it.add(METALLUM, 4)
          .add(PERMUTATIO, 4)
          .add(INSTRUMENTUM, 4)
      }
      item(Tags.Items.RAW_MATERIALS_GOLD) {
        it.add(METALLUM, 2)
          .add(DESIDERIUM, 4)
          .add(TERRA, 2)
      }
      item(Tags.Items.INGOTS_GOLD) {
        it.add(METALLUM, 4)
          .add(DESIDERIUM, 8)
      }
      item(Tags.Items.GEMS_DIAMOND) {
        it.add(VITREUS, 8)
          .add(DESIDERIUM, 8)
      }
      item(Tags.Items.GEMS_EMERALD) {
        it.add(VITREUS, 8)
          .add(DESIDERIUM, 4)
      }
      item(Items.NETHERITE_SCRAP) {
        it.add(METALLUM, 2)
          .add(TERRA, 2)
          .add(TENEBRAE, 1)
      }
      item(Tags.Items.INGOTS_NETHERITE) {
        it.add(METALLUM, 16)
          .add(DESIDERIUM, 8)
          .add(TENEBRAE, 4)
      }
      item(Tags.Items.DUSTS_GLOWSTONE) {
        it.add(LUX, 4)
          .add(TERRA, 1)
      }
      item(Tags.Items.GEMS_QUARTZ) {
        it.add(VITREUS, 4)
          .add(IGNIS, 1)
      }
      item(Tags.Items.GEMS_LAPIS) {
        it.add(VITREUS, 4)
          .add(SENSUS, 2)
      }
      item(Tags.Items.DUSTS_REDSTONE) {
        it.add(POTENTIA, 4)
          .add(TERRA, 1)
      }
      item(Tags.Items.GEMS_AMETHYST) {
        it.add(VITREUS, 4)
          .add(SENSUS, 1)
      }

      for (shard in SHARDS.values) {
        item(shard) {
          it.add(shard.get().aspect, 4)
        }
      }

      item(Tags.Items.NUGGETS) {
        it.add(TERRA, 1)
      }
    }
  }
}