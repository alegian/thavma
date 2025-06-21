package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.block
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
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object MineralAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.BLOCK).run {
      block(Tags.Blocks.STORAGE_BLOCKS_COAL) {
        it.add(METALLUM.get(), 144)
          .add(DESIDERIUM.get(), 72)
          .add(TENEBRAE.get(), 36)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_COPPER) {
        it.add(METALLUM.get(), 36)
          .add(PERMUTATIO.get(), 36)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_IRON) {
        it.add(METALLUM.get(), 72)
      }
      block(T7Blocks.THAVMITE_BLOCK.get()) {
        it.add(METALLUM.get(), 72)
          .add(PRAECANTATIO.get(), 36)
      }
      block(T7Blocks.ORICHALCUM_BLOCK.get()) {
        it.add(METALLUM.get(), 36)
          .add(PRAECANTATIO.get(), 36)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_GOLD) {
        it.add(METALLUM.get(), 36)
          .add(DESIDERIUM.get(), 72)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_DIAMOND) {
        it.add(VITREUS.get(), 72)
          .add(DESIDERIUM.get(), 72)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_EMERALD) {
        it.add(VITREUS.get(), 72)
          .add(DESIDERIUM.get(), 36)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_NETHERITE) {
        it.add(METALLUM.get(), 144)
          .add(DESIDERIUM.get(), 72)
          .add(TENEBRAE.get(), 36)
      }
      block(Blocks.GLOWSTONE) {
        it.add(LUX.get(), 16)
          .add(TERRA.get(), 4)
      }
      block(Blocks.QUARTZ_BLOCK) {
        it.add(VITREUS.get(), 16)
          .add(IGNIS.get(), 4)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_LAPIS) {
        it.add(VITREUS.get(), 36)
          .add(SENSUS.get(), 18)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_REDSTONE) {
        it.add(POTENTIA.get(), 36)
          .add(TERRA.get(), 9)
      }
      block(Blocks.AMETHYST_BLOCK) {
        it.add(VITREUS.get(), 16)
          .add(SENSUS.get(), 4)
      }
    }

    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(ItemTags.COALS) {
        it.add(IGNIS.get(), 8)
          .add(POTENTIA.get(), 8)
      }
      item(Tags.Items.INGOTS_COPPER) {
        it.add(METALLUM.get(), 4)
          .add(PERMUTATIO.get(), 4)
      }
      item(Tags.Items.INGOTS_IRON) {
        it.add(METALLUM.get(), 8)
      }
      item(T7Items.THAVMITE_INGOT.get()) {
        it.add(METALLUM.get(), 8)
          .add(PRAECANTATIO.get(), 4)
      }
      item(T7Items.ORICHALCUM_INGOT.get()) {
        it.add(METALLUM.get(), 4)
          .add(INSTRUMENTUM.get(), 4)
      }
      item(Tags.Items.INGOTS_GOLD) {
        it.add(METALLUM.get(), 4)
          .add(DESIDERIUM.get(), 8)
      }
      item(Tags.Items.GEMS_DIAMOND) {
        it.add(VITREUS.get(), 8)
          .add(DESIDERIUM.get(), 8)
      }
      item(Tags.Items.GEMS_EMERALD) {
        it.add(VITREUS.get(), 8)
          .add(DESIDERIUM.get(), 4)
      }
      item(Items.NETHERITE_SCRAP) {
        it.add(METALLUM.get(), 2)
          .add(TERRA.get(), 2)
          .add(TENEBRAE.get(), 1)
      }
      item(Tags.Items.INGOTS_NETHERITE) {
        it.add(METALLUM.get(), 16)
          .add(DESIDERIUM.get(), 8)
          .add(TENEBRAE.get(), 4)
      }
      item(Tags.Items.DUSTS_GLOWSTONE) {
        it.add(LUX.get(), 4)
          .add(TERRA.get(), 1)
      }
      item(Tags.Items.GEMS_QUARTZ) {
        it.add(VITREUS.get(), 4)
          .add(IGNIS.get(), 1)
      }
      item(Tags.Items.GEMS_LAPIS) {
        it.add(VITREUS.get(), 4)
          .add(SENSUS.get(), 2)
      }
      item(Tags.Items.DUSTS_REDSTONE) {
        it.add(POTENTIA.get(), 4)
          .add(TERRA.get(), 1)
      }
      item(Tags.Items.GEMS_AMETHYST) {
        it.add(VITREUS.get(), 4)
          .add(SENSUS.get(), 1)
      }
    }
  }
}