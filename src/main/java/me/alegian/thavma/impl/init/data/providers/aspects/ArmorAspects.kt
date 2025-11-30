package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object ArmorAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.LEATHER_HELMET) {
        it.add(Aspects.CORPUS, 15)
          .add(Aspects.PRAEMUNIO, 2)
      }
      item(Items.LEATHER_CHESTPLATE) {
        it.add(Aspects.CORPUS, 24)
          .add(Aspects.PRAEMUNIO, 6)
      }
      item(Items.LEATHER_LEGGINGS) {
        it.add(Aspects.CORPUS, 21)
          .add(Aspects.PRAEMUNIO, 4)
      }
      item(Items.LEATHER_BOOTS) {
        it.add(Aspects.CORPUS, 12)
          .add(Aspects.PRAEMUNIO, 2)
      }

      item(T7Tags.Items.GOGGLES) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.AETHER, 4)
          .add(Aspects.VITREUS, 4)
      }
      item(T7Items.APPRENTICE_CHESTPLATE) {
        it.add(Aspects.FABRICO, 32)
          .add(Aspects.AETHER, 8)
          .add(Aspects.PRAEMUNIO, 2)
      }
      item(T7Items.APPRENTICE_LEGGINGS) {
        it.add(Aspects.FABRICO, 28)
          .add(Aspects.AETHER, 7)
          .add(Aspects.PRAEMUNIO, 2)
      }
      item(T7Items.APPRENTICE_BOOTS) {
        it.add(Aspects.FABRICO, 16)
          .add(Aspects.AETHER, 4)
          .add(Aspects.PRAEMUNIO, 2)
      }

      item(Items.CHAINMAIL_HELMET) {
        it.add(Aspects.METALLUM, 20)
          .add(Aspects.PRAEMUNIO, 4)
      }
      item(Items.CHAINMAIL_CHESTPLATE) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.PRAEMUNIO, 10)
      }
      item(Items.CHAINMAIL_LEGGINGS) {
        it.add(Aspects.METALLUM, 28)
          .add(Aspects.PRAEMUNIO, 8)
      }
      item(Items.CHAINMAIL_BOOTS) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.PRAEMUNIO, 2)
      }

      item(Items.IRON_HELMET) {
        it.add(Aspects.METALLUM, 40)
          .add(Aspects.PRAEMUNIO, 4)
      }
      item(Items.IRON_CHESTPLATE) {
        it.add(Aspects.METALLUM, 64)
          .add(Aspects.PRAEMUNIO, 12)
      }
      item(Items.IRON_LEGGINGS) {
        it.add(Aspects.METALLUM, 56)
          .add(Aspects.PRAEMUNIO, 10)
      }
      item(Items.IRON_BOOTS) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.PRAEMUNIO, 4)
      }

      item(T7Items.THAVMITE_HELMET) {
        it.add(Aspects.METALLUM, 40)
          .add(Aspects.AETHER, 20)
          .add(Aspects.PRAEMUNIO, 7)
      }
      item(T7Items.THAVMITE_CHESTPLATE) {
        it.add(Aspects.METALLUM, 64)
          .add(Aspects.AETHER, 32)
          .add(Aspects.PRAEMUNIO, 13)
      }
      item(T7Items.THAVMITE_LEGGINGS) {
        it.add(Aspects.METALLUM, 56)
          .add(Aspects.AETHER, 28)
          .add(Aspects.PRAEMUNIO, 11)
      }
      item(T7Items.THAVMITE_BOOTS) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.AETHER, 16)
          .add(Aspects.PRAEMUNIO, 5)
      }

      item(Items.GOLDEN_HELMET) {
        it.add(Aspects.METALLUM, 20)
          .add(Aspects.PRAEMUNIO, 4)
      }
      item(Items.GOLDEN_CHESTPLATE) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.PRAEMUNIO, 10)
      }
      item(Items.GOLDEN_LEGGINGS) {
        it.add(Aspects.METALLUM, 28)
          .add(Aspects.PRAEMUNIO, 6)
      }
      item(Items.GOLDEN_BOOTS) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.PRAEMUNIO, 2)
      }

      item(Items.DIAMOND_HELMET) {
        it.add(Aspects.VITREUS, 40)
          .add(Aspects.PRAEMUNIO, 8)
      }
      item(Items.DIAMOND_CHESTPLATE) {
        it.add(Aspects.VITREUS, 64)
          .add(Aspects.PRAEMUNIO, 18)
      }
      item(Items.DIAMOND_LEGGINGS) {
        it.add(Aspects.VITREUS, 56)
          .add(Aspects.PRAEMUNIO, 14)
      }
      item(Items.DIAMOND_BOOTS) {
        it.add(Aspects.VITREUS, 32)
          .add(Aspects.PRAEMUNIO, 8)
      }

      item(Items.NETHERITE_HELMET) {
        it.add(Aspects.METALLUM, 40)
          .add(Aspects.PRAEMUNIO, 9)
          .add(Aspects.TENEBRAE, 4)
      }
      item(Items.NETHERITE_CHESTPLATE) {
        it.add(Aspects.METALLUM, 64)
          .add(Aspects.PRAEMUNIO, 19)
          .add(Aspects.TENEBRAE, 4)
      }
      item(Items.NETHERITE_LEGGINGS) {
        it.add(Aspects.METALLUM, 56)
          .add(Aspects.PRAEMUNIO, 15)
          .add(Aspects.TENEBRAE, 4)
      }
      item(Items.NETHERITE_BOOTS) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.PRAEMUNIO, 9)
          .add(Aspects.TENEBRAE, 4)
      }

      item(Items.WOLF_ARMOR) {
        it.add(Aspects.CORPUS, 8)
          .add(Aspects.PRAEMUNIO, 6)
      }
      item(Items.LEATHER_HORSE_ARMOR) {
        it.add(Aspects.CORPUS, 21)
          .add(Aspects.PRAEMUNIO, 4)
      }
      item(Items.IRON_HORSE_ARMOR) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.PRAEMUNIO, 6)
      }
      item(Items.GOLDEN_HORSE_ARMOR) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.PRAEMUNIO, 8)
      }
      item(Items.DIAMOND_HORSE_ARMOR) {
        it.add(Aspects.VITREUS, 16)
          .add(Aspects.PRAEMUNIO, 12)
      }

      item(Items.TURTLE_HELMET) {
        it.add(Aspects.CORPUS, 10)
          .add(Aspects.PRAEMUNIO, 4)
          .add(Aspects.AQUA, 5)
      }
    }
  }
}