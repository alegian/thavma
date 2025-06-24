package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AVERSIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.INSTRUMENTUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.MOTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERMUTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAECANTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object ToolAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.WOODEN_SWORD) {
        it.add(HERBA, 8)
          .add(AVERSIO, 1)
      }
      item(Items.WOODEN_PICKAXE) {
        it.add(HERBA, 12)
          .add(INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_AXE) {
        it.add(HERBA, 12)
          .add(INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_SHOVEL) {
        it.add(HERBA, 4)
          .add(INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_HOE) {
        it.add(HERBA, 8)
          .add(INSTRUMENTUM, 1)
      }

      item(Items.STONE_SWORD) {
        it.add(TERRA, 8)
          .add(AVERSIO, 2)
      }
      item(Items.STONE_PICKAXE) {
        it.add(TERRA, 12)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.STONE_AXE) {
        it.add(TERRA, 12)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.STONE_SHOVEL) {
        it.add(TERRA, 4)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.STONE_HOE) {
        it.add(TERRA, 8)
          .add(INSTRUMENTUM, 2)
      }

      item(Items.IRON_SWORD) {
        it.add(METALLUM, 16)
          .add(AVERSIO, 3)
      }
      item(Items.IRON_PICKAXE) {
        it.add(METALLUM, 24)
          .add(INSTRUMENTUM, 3)
      }
      item(Items.IRON_AXE) {
        it.add(METALLUM, 24)
          .add(INSTRUMENTUM, 3)
      }
      item(Items.IRON_SHOVEL) {
        it.add(METALLUM, 8)
          .add(INSTRUMENTUM, 3)
      }
      item(Items.IRON_HOE) {
        it.add(METALLUM, 16)
          .add(INSTRUMENTUM, 3)
      }

      item(T7Items.THAVMITE_SWORD) {
        it.add(METALLUM, 16)
          .add(PRAECANTATIO, 8)
          .add(AVERSIO, 4)
      }
      item(T7Items.THAVMITE_PICKAXE) {
        it.add(METALLUM, 24)
          .add(PRAECANTATIO, 12)
          .add(INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_AXE) {
        it.add(METALLUM, 24)
          .add(PRAECANTATIO, 12)
          .add(INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_SHOVEL) {
        it.add(METALLUM, 8)
          .add(PRAECANTATIO, 4)
          .add(INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_HOE) {
        it.add(METALLUM, 16)
          .add(PRAECANTATIO, 8)
          .add(INSTRUMENTUM, 4)
      }

      item(Items.GOLDEN_SWORD) {
        it.add(METALLUM, 8)
          .add(DESIDERIUM, 16)
          .add(AVERSIO, 2)
      }
      item(Items.GOLDEN_PICKAXE) {
        it.add(METALLUM, 12)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_AXE) {
        it.add(METALLUM, 12)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_SHOVEL) {
        it.add(METALLUM, 4)
          .add(DESIDERIUM, 8)
          .add(INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_HOE) {
        it.add(METALLUM, 8)
          .add(DESIDERIUM, 16)
          .add(INSTRUMENTUM, 2)
      }

      item(Items.DIAMOND_SWORD) {
        it.add(VITREUS, 16)
          .add(DESIDERIUM, 16)
          .add(AVERSIO, 4)
      }
      item(Items.DIAMOND_PICKAXE) {
        it.add(VITREUS, 24)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_AXE) {
        it.add(VITREUS, 24)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_SHOVEL) {
        it.add(VITREUS, 8)
          .add(DESIDERIUM, 8)
          .add(INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_HOE) {
        it.add(VITREUS, 16)
          .add(DESIDERIUM, 16)
          .add(INSTRUMENTUM, 4)
      }

      item(Items.NETHERITE_SWORD) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 16)
          .add(AVERSIO, 6)
          .add(TENEBRAE, 8)
      }
      item(Items.NETHERITE_PICKAXE) {
        it.add(METALLUM, 48)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 6)
          .add(TENEBRAE, 12)
      }
      item(Items.NETHERITE_AXE) {
        it.add(METALLUM, 48)
          .add(DESIDERIUM, 24)
          .add(INSTRUMENTUM, 6)
          .add(TENEBRAE, 12)
      }
      item(Items.NETHERITE_SHOVEL) {
        it.add(METALLUM, 16)
          .add(DESIDERIUM, 8)
          .add(INSTRUMENTUM, 6)
          .add(TENEBRAE, 4)
      }
      item(Items.NETHERITE_HOE) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 16)
          .add(INSTRUMENTUM, 6)
          .add(TENEBRAE, 8)
      }

      item(T7Items.IRON_HANDLE) {
        it.add(METALLUM, 16)
      }
      item(T7Items.GOLD_HANDLE) {
        it.add(METALLUM, 8)
          .add(DESIDERIUM, 16)
      }
      item(T7Items.ORICHALCUM_HANDLE) {
        it.add(METALLUM, 8)
          .add(PERMUTATIO, 8)
          .add(INSTRUMENTUM, 8)
      }
      item(T7Items.THAVMITE_HANDLE) {
        it.add(METALLUM, 16)
          .add(PRAECANTATIO, 8)
      }

      item(ItemTags.ARROWS) {
        it.add(AVERSIO, 2)
          .add(MOTUS, 1)
      }
    }
  }
}