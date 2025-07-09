package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.FABRICO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAECANTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAEMUNIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object ArmorAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.LEATHER_HELMET) {
        it.add(BESTIA, 40)
          .add(PRAEMUNIO, 2)
      }
      item(Items.LEATHER_CHESTPLATE) {
        it.add(BESTIA, 64)
          .add(PRAEMUNIO, 6)
      }
      item(Items.LEATHER_LEGGINGS) {
        it.add(BESTIA, 56)
          .add(PRAEMUNIO, 4)
      }
      item(Items.LEATHER_BOOTS) {
        it.add(BESTIA, 32)
          .add(PRAEMUNIO, 2)
      }

      item(T7Tags.Items.GOGGLES) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 32)
          .add(PRAECANTATIO, 4)
          .add(VITREUS, 4)
      }
      item(T7Items.APPRENTICE_CHESTPLATE) {
        it.add(FABRICO, 32)
          .add(PRAECANTATIO, 8)
          .add(PRAEMUNIO, 2)
      }
      item(T7Items.APPRENTICE_LEGGINGS) {
        it.add(FABRICO, 28)
          .add(PRAECANTATIO, 7)
          .add(PRAEMUNIO, 2)
      }
      item(T7Items.APPRENTICE_BOOTS) {
        it.add(FABRICO, 16)
          .add(PRAECANTATIO, 4)
          .add(PRAEMUNIO, 2)
      }

      item(Items.CHAINMAIL_HELMET) {
        it.add(METALLUM, 20)
          .add(PRAEMUNIO, 4)
      }
      item(Items.CHAINMAIL_CHESTPLATE) {
        it.add(METALLUM, 32)
          .add(PRAEMUNIO, 10)
      }
      item(Items.CHAINMAIL_LEGGINGS) {
        it.add(METALLUM, 28)
          .add(PRAEMUNIO, 8)
      }
      item(Items.CHAINMAIL_BOOTS) {
        it.add(METALLUM, 16)
          .add(PRAEMUNIO, 2)
      }

      item(Items.IRON_HELMET) {
        it.add(METALLUM, 40)
          .add(PRAEMUNIO, 4)
      }
      item(Items.IRON_CHESTPLATE) {
        it.add(METALLUM, 64)
          .add(PRAEMUNIO, 12)
      }
      item(Items.IRON_LEGGINGS) {
        it.add(METALLUM, 56)
          .add(PRAEMUNIO, 10)
      }
      item(Items.IRON_BOOTS) {
        it.add(METALLUM, 32)
          .add(PRAEMUNIO, 4)
      }

      item(T7Items.THAVMITE_HELMET) {
        it.add(METALLUM, 40)
          .add(PRAECANTATIO, 20)
          .add(PRAEMUNIO, 7)
      }
      item(T7Items.THAVMITE_CHESTPLATE) {
        it.add(METALLUM, 64)
          .add(PRAECANTATIO, 32)
          .add(PRAEMUNIO, 13)
      }
      item(T7Items.THAVMITE_LEGGINGS) {
        it.add(METALLUM, 56)
          .add(PRAECANTATIO, 28)
          .add(PRAEMUNIO, 11)
      }
      item(T7Items.THAVMITE_BOOTS) {
        it.add(METALLUM, 32)
          .add(PRAECANTATIO, 16)
          .add(PRAEMUNIO, 5)
      }

      item(Items.GOLDEN_HELMET) {
        it.add(METALLUM, 20)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 4)
      }
      item(Items.GOLDEN_CHESTPLATE) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 10)
      }
      item(Items.GOLDEN_LEGGINGS) {
        it.add(METALLUM, 28)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 6)
      }
      item(Items.GOLDEN_BOOTS) {
        it.add(METALLUM, 16)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 2)
      }

      item(Items.DIAMOND_HELMET) {
        it.add(VITREUS, 40)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 8)
      }
      item(Items.DIAMOND_CHESTPLATE) {
        it.add(VITREUS, 64)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 18)
      }
      item(Items.DIAMOND_LEGGINGS) {
        it.add(VITREUS, 56)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 14)
      }
      item(Items.DIAMOND_BOOTS) {
        it.add(VITREUS, 32)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 8)
      }

      item(Items.NETHERITE_HELMET) {
        it.add(METALLUM, 40)
          .add(DESIDERIUM, 40)
          .add(PRAEMUNIO, 9)
          .add(TENEBRAE, 4)
      }
      item(Items.NETHERITE_CHESTPLATE) {
        it.add(METALLUM, 64)
          .add(DESIDERIUM, 64)
          .add(PRAEMUNIO, 19)
          .add(TENEBRAE, 4)
      }
      item(Items.NETHERITE_LEGGINGS) {
        it.add(METALLUM, 56)
          .add(DESIDERIUM, 56)
          .add(PRAEMUNIO, 15)
          .add(TENEBRAE, 4)
      }
      item(Items.NETHERITE_BOOTS) {
        it.add(METALLUM, 32)
          .add(DESIDERIUM, 32)
          .add(PRAEMUNIO, 9)
          .add(TENEBRAE, 4)
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

      item(Items.TURTLE_HELMET) {
        it.add(BESTIA, 10)
          .add(PRAEMUNIO, 4)
          .add(AQUA, 5)
      }
    }
  }
}