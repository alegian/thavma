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
      entity(EntityType.ALLAY){}
      entity(EntityType.ARMADILLO){}
      entity(EntityType.AXOLOTL){}
      entity(EntityType.BAT){}
      entity(EntityType.BEE){}
      entity(EntityType.BLAZE){}
      entity(EntityType.BOGGED){}
      entity(EntityType.BREEZE){}
      entity(EntityType.CAMEL){}
      entity(EntityType.CAT){}
      entity(EntityType.CAVE_SPIDER){}
      entity(EntityType.CHICKEN){}
      entity(EntityType.COD){}
      entity(EntityType.COW) {
        it.add(TERRA.get(), 15)
          .add(BESTIA.get(), 15)
      }
      entity(EntityType.CREEPER) {
        it.add(IGNIS.get(), 15)
          .add(HERBA.get(), 15)
      }
      entity(EntityType.DOLPHIN){}
      entity(EntityType.DONKEY){}
      entity(EntityType.DROWNED){}
      entity(EntityType.ELDER_GUARDIAN){}
      entity(EntityType.ENDERMAN) {
        it.add(DESIDERIUM.get(), 5)
          .add(MOTUS.get(), 15)
          .add(ALIENIS.get(), 15)
      }
      entity(EntityType.ENDERMITE){}
      entity(EntityType.EVOKER){}
      entity(EntityType.FOX){}
      entity(EntityType.FROG){}
      entity(EntityType.GHAST){}
      entity(EntityType.GLOW_SQUID){}
      entity(EntityType.GOAT){}
      entity(EntityType.GUARDIAN){}
      entity(EntityType.HOGLIN){}
      entity(EntityType.HORSE){}
      entity(EntityType.HUSK){}
      entity(EntityType.IRON_GOLEM){}
      entity(EntityType.LLAMA){}
      entity(EntityType.MAGMA_CUBE){}
      entity(EntityType.MOOSHROOM){}
      entity(EntityType.MULE){}
      entity(EntityType.OCELOT){}
      entity(EntityType.PANDA){}
      entity(EntityType.PARROT){}
      entity(EntityType.PHANTOM){}
      entity(EntityType.PIG) {
        it.add(DESIDERIUM.get(), 5)
          .add(TERRA.get(), 10)
          .add(BESTIA.get(), 10)
      }
      entity(EntityType.PIGLIN){}
      entity(EntityType.PIGLIN_BRUTE){}
      entity(EntityType.PILLAGER){}
      entity(EntityType.POLAR_BEAR){}
      entity(EntityType.PUFFERFISH){}
      entity(EntityType.RABBIT){}
      entity(EntityType.RAVAGER){}
      entity(EntityType.SALMON){}
      entity(EntityType.SHEEP) {
        it.add(TERRA.get(), 10)
          .add(BESTIA.get(), 10)
      }
      entity(EntityType.SHULKER){}
      entity(EntityType.SILVERFISH){}
      entity(EntityType.ZOMBIE) {
        it.add(TERRA.get(), 5)
          .add(HUMANUS.get(), 10)
          .add(EXANIMIS.get(), 20)
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
      entity(EntityType.VILLAGER) {
        it.add(HUMANUS.get(), 15)
      }
      entity(EntityType.SLIME) {
        it.add(ALKIMIA.get(), 5)
          .add(AQUA.get(), 10)
          .add(VICTUS.get(), 10)
      }
    }
}