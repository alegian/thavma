package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AVERSIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.INSTRUMENTUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object ToolAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.WOODEN_SWORD) {
        it.add(HERBA.get(), 8)
          .add(AVERSIO.get(), 1)
      }
      item(Items.WOODEN_PICKAXE) {
        it.add(HERBA.get(), 12)
          .add(INSTRUMENTUM.get(), 1)
      }
      item(Items.WOODEN_AXE) {
        it.add(HERBA.get(), 12)
          .add(INSTRUMENTUM.get(), 1)
      }
      item(Items.WOODEN_SHOVEL) {
        it.add(HERBA.get(), 4)
          .add(INSTRUMENTUM.get(), 1)
      }
      item(Items.WOODEN_HOE) {
        it.add(HERBA.get(), 8)
          .add(INSTRUMENTUM.get(), 1)
      }

      item(Items.STONE_SWORD) {
        it.add(TERRA.get(), 8)
          .add(AVERSIO.get(), 2)
      }
      item(Items.STONE_PICKAXE) {
        it.add(TERRA.get(), 12)
          .add(INSTRUMENTUM.get(), 2)
      }
      item(Items.STONE_AXE) {
        it.add(TERRA.get(), 12)
          .add(INSTRUMENTUM.get(), 2)
      }
      item(Items.STONE_SHOVEL) {
        it.add(TERRA.get(), 4)
          .add(INSTRUMENTUM.get(), 2)
      }
      item(Items.STONE_HOE) {
        it.add(TERRA.get(), 8)
          .add(INSTRUMENTUM.get(), 2)
      }

      item(Items.IRON_SWORD) {
        it.add(METALLUM.get(), 16)
          .add(AVERSIO.get(), 3)
      }
      item(Items.IRON_PICKAXE) {
        it.add(METALLUM.get(), 24)
          .add(INSTRUMENTUM.get(), 3)
      }
      item(Items.IRON_AXE) {
        it.add(METALLUM.get(), 24)
          .add(INSTRUMENTUM.get(), 3)
      }
      item(Items.IRON_SHOVEL) {
        it.add(METALLUM.get(), 8)
          .add(INSTRUMENTUM.get(), 3)
      }
      item(Items.IRON_HOE) {
        it.add(METALLUM.get(), 16)
          .add(INSTRUMENTUM.get(), 3)
      }
    }
  }
}