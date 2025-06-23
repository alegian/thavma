package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAEMUNIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object ArmorAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.LEATHER_HELMET) {
        it.add(BESTIA, 40)
          .add(PRAEMUNIO, 4)
      }
      item(Items.LEATHER_CHESTPLATE) {
        it.add(BESTIA, 64)
          .add(PRAEMUNIO, 12)
      }
      item(Items.LEATHER_LEGGINGS) {
        it.add(BESTIA, 56)
          .add(PRAEMUNIO, 8)
      }
      item(Items.LEATHER_BOOTS) {
        it.add(BESTIA, 32)
          .add(PRAEMUNIO, 4)
      }

      item(Items.CHAINMAIL_HELMET) {
        it.add(METALLUM, 20)
          .add(PRAEMUNIO, 8)
      }
      item(Items.CHAINMAIL_CHESTPLATE) {
        it.add(METALLUM, 32)
          .add(PRAEMUNIO, 20)
      }
      item(Items.CHAINMAIL_LEGGINGS) {
        it.add(METALLUM, 28)
          .add(PRAEMUNIO, 16)
      }
      item(Items.CHAINMAIL_BOOTS) {
        it.add(METALLUM, 16)
          .add(PRAEMUNIO, 4)
      }

      item(Items.IRON_HELMET) {
        it.add(METALLUM, 40)
          .add(PRAEMUNIO, 8)
      }
      item(Items.IRON_CHESTPLATE) {
        it.add(METALLUM, 64)
          .add(PRAEMUNIO, 24)
      }
      item(Items.IRON_LEGGINGS) {
        it.add(METALLUM, 56)
          .add(PRAEMUNIO, 20)
      }
      item(Items.IRON_BOOTS) {
        it.add(METALLUM, 32)
          .add(PRAEMUNIO, 8)
      }

      item(Items.GOLDEN_HELMET) {
        it.add(METALLUM, 20)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 8)
      }
      item(Items.GOLDEN_CHESTPLATE) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 20)
      }
      item(Items.GOLDEN_LEGGINGS) {
        it.add(METALLUM, 28)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 12)
      }
      item(Items.GOLDEN_BOOTS) {
        it.add(METALLUM, 16)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 4)
      }

      item(Items.DIAMOND_HELMET) {
        it.add(VITREUS, 40)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 16)
      }
      item(Items.DIAMOND_CHESTPLATE) {
        it.add(VITREUS, 64)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 36)
      }
      item(Items.DIAMOND_LEGGINGS) {
        it.add(VITREUS, 56)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 28)
      }
      item(Items.DIAMOND_BOOTS) {
        it.add(VITREUS, 32)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 16)
      }

      item(Items.NETHERITE_HELMET) {
        it.add(METALLUM, 40)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 18)
          .add(TENEBRAE.get(), 4)
      }
      item(Items.NETHERITE_CHESTPLATE) {
        it.add(METALLUM, 64)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 38)
          .add(TENEBRAE.get(), 4)
      }
      item(Items.NETHERITE_LEGGINGS) {
        it.add(METALLUM, 56)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 30)
          .add(TENEBRAE.get(), 4)
      }
      item(Items.NETHERITE_BOOTS) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 18)
          .add(TENEBRAE.get(), 4)
      }

      item(Items.WOLF_ARMOR) {
        it.add(BESTIA, 8)
          .add(PRAEMUNIO, 6)
      }
      item(Items.LEATHER_HORSE_ARMOR) {
        it.add(BESTIA, 16)
          .add(PRAEMUNIO, 4)
      }
      item(Items.IRON_HORSE_ARMOR) {
        it.add(METALLUM, 16)
          .add(PRAEMUNIO, 6)
          .add(BESTIA, 8)
      }
      item(Items.GOLDEN_HORSE_ARMOR) {
        it.add(METALLUM, 8)
          .add(DESIDERIUM, 16)
          .add(PRAEMUNIO, 8)
          .add(BESTIA, 8)
      }
      item(Items.DIAMOND_HORSE_ARMOR) {
        it.add(VITREUS, 16)
          .add(DESIDERIUM, 16)
          .add(PRAEMUNIO, 12)
          .add(BESTIA, 8)
      }
    }
  }
}