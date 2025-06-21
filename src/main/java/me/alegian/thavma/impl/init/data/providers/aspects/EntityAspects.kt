package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.entity
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALIENIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALKIMIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.EXANIMIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HUMANUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.MOTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VICTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VINCULUM
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType

object EntityAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) =
    datamapProvider.builder(T7DataMaps.AspectContent.ENTITY).run {
      entity(EntityType.PIG) {
        it.add(DESIDERIUM.get(), 5)
          .add(TERRA.get(), 10)
          .add(BESTIA.get(), 10)
      }
      entity(EntityType.ZOMBIE) {
        it.add(TERRA.get(), 5)
          .add(HUMANUS.get(), 10)
          .add(EXANIMIS.get(), 20)
      }
      entity(EntityType.COW) {
        it.add(TERRA.get(), 15)
          .add(BESTIA.get(), 15)
      }
      entity(EntityType.SHEEP) {
        it.add(TERRA.get(), 10)
          .add(BESTIA.get(), 10)
      }
      entity(EntityType.SPIDER) {
        it.add(VINCULUM.get(), 10)
          .add(BESTIA.get(), 10)
      }
      entity(EntityType.SKELETON) {
        it.add(TERRA.get(), 5)
          .add(HUMANUS.get(), 5)
          .add(EXANIMIS.get(), 20)
      }
      entity(EntityType.CREEPER) {
        it.add(IGNIS.get(), 15)
          .add(HERBA.get(), 15)
      }
      entity(EntityType.VILLAGER) {
        it.add(HUMANUS.get(), 15)
      }
      entity(EntityType.ENDERMAN) {
        it.add(DESIDERIUM.get(), 5)
          .add(MOTUS.get(), 15)
          .add(ALIENIS.get(), 15)
      }
      entity(EntityType.SLIME) {
        it.add(ALKIMIA.get(), 5)
          .add(AQUA.get(), 10)
          .add(VICTUS.get(), 10)
      }
    }
}