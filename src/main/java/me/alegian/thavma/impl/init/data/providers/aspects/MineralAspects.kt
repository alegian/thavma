package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.block
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.LUX
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERMUTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.POTENTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.SENSUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

object MineralAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.BLOCK).run {
      block(Blocks.GLOWSTONE) {
        it.add(LUX.get(), 16)
          .add(TERRA.get(), 4)
      }
      block(Tags.Blocks.STORAGE_BLOCKS_COAL) {
        it.add(METALLUM.get(), 144)
          .add(DESIDERIUM.get(), 72)
          .add(TENEBRAE.get(), 36)
      }
      block(Blocks.NETHERITE_BLOCK) {
        it.add(METALLUM.get(), 144)
          .add(DESIDERIUM.get(), 72)
          .add(TENEBRAE.get(), 36)
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
        it.add(IGNIS.get(), 1)
          .add(VITREUS.get(), 4)
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