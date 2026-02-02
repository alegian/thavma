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
import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags

object EntityAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) =
    datamapProvider.builder(T7DataMaps.AspectContent.ENTITY).run {
      entity(EntityType.ALLAY) {}
      entity(EntityType.ARMADILLO) {}
      entity(EntityType.AXOLOTL) {}
      entity(EntityType.BAT) {}
      entity(EntityType.BEE) {}
      entity(EntityType.BLAZE) {}
      entity(EntityType.BOGGED) {}
      entity(EntityType.BREEZE) {}
      entity(EntityType.CAMEL) {}
      entity(EntityType.CAT) {}
      entity(EntityType.CAVE_SPIDER) {}
      entity(EntityType.CHICKEN) {}
      entity(EntityType.COD) {}
      entity(EntityType.COW) {
        it.add(TERRA, 15)
          .add(BESTIA, 15)
      }
      entity(EntityType.CREEPER) {
        it.add(IGNIS, 15)
          .add(HERBA, 15)
      }
      entity(EntityType.DOLPHIN) {}
      entity(EntityType.DONKEY) {}
      entity(EntityType.DROWNED) {}
      entity(EntityType.ELDER_GUARDIAN) {}
      entity(EntityType.ENDERMAN) {
        it.add(MOTUS, 12)
          .add(ALIENIS, 8)
          .add(HUMANUS, 4)
          .add(DESIDERIUM, 4)
      }
      entity(EntityType.ENDERMITE) {}
      entity(EntityType.EVOKER) {}
      entity(EntityType.FOX) {}
      entity(EntityType.FROG) {}
      entity(EntityType.GHAST) {
        it.add(EXANIMIS, 12)
          .add(IGNIS, 8)
      }
      entity(EntityType.GLOW_SQUID) {}
      entity(EntityType.GOAT) {}
      entity(EntityType.GUARDIAN) {}
      entity(EntityType.HOGLIN) {}
      entity(EntityType.HORSE) {}
      entity(EntityType.HUSK) {}
      entity(EntityType.IRON_GOLEM) {}
      entity(EntityType.LLAMA) {}
      entity(EntityType.MAGMA_CUBE) {}
      entity(EntityType.MOOSHROOM) {}
      entity(EntityType.MULE) {}
      entity(EntityType.OCELOT) {}
      entity(EntityType.PANDA) {}
      entity(EntityType.PARROT) {}
      entity(EntityType.PHANTOM) {}
      entity(EntityType.PIG) {
        it.add(DESIDERIUM, 5)
          .add(TERRA, 10)
          .add(BESTIA, 10)
      }
      entity(EntityType.PIGLIN) {}
      entity(EntityType.PIGLIN_BRUTE) {}
      entity(EntityType.PILLAGER) {}
      entity(EntityType.POLAR_BEAR) {}
      entity(EntityType.PUFFERFISH) {}
      entity(EntityType.RABBIT) {}
      entity(EntityType.RAVAGER) {}
      entity(EntityType.SALMON) {}
      entity(EntityType.SHEEP) {
        it.add(TERRA, 10)
          .add(BESTIA, 10)
      }
      entity(EntityType.SHULKER) {}
      entity(EntityType.SILVERFISH) {}
      entity(EntityType.SKELETON) {
        it.add(TERRA, 5)
          .add(HUMANUS, 5)
          .add(EXANIMIS, 20)
      }
      entity(EntityType.SKELETON_HORSE) {}
      entity(EntityType.SLIME) {
        it.add(ALKIMIA, 5)
          .add(AQUA, 10)
          .add(VICTUS, 10)
      }
      entity(EntityType.SNIFFER) {}
      entity(EntityType.SNOW_GOLEM) {}
      entity(EntityType.SPIDER) {
        it.add(VINCULUM, 10)
          .add(BESTIA, 10)
      }
      entity(EntityType.SQUID) {}
      entity(EntityType.STRAY) {}
      entity(EntityType.STRIDER) {}
      entity(EntityType.TADPOLE) {}
      entity(EntityType.TRADER_LLAMA) {}
      entity(EntityType.TROPICAL_FISH) {}
      entity(EntityType.TURTLE) {}
      entity(EntityType.VEX) {}
      entity(EntityType.VILLAGER) {
        it.add(HUMANUS, 15)
      }
      entity(EntityType.VINDICATOR) {}
      entity(EntityType.WANDERING_TRADER) {}
      entity(EntityType.WARDEN) {}
      entity(EntityType.WITCH) {}
      entity(EntityType.WITHER_SKELETON) {}
      entity(EntityType.WOLF) {}
      entity(EntityType.ZOGLIN) {}
      entity(EntityType.ZOMBIE) {
        it.add(TERRA, 5)
          .add(HUMANUS, 10)
          .add(EXANIMIS, 20)
      }
      entity(EntityType.ZOMBIE_HORSE) {}
      entity(EntityType.ZOMBIE_VILLAGER) {}
      entity(EntityType.ZOMBIFIED_PIGLIN) {}

      entity(EntityType.WITHER) {}
      entity(EntityType.ENDER_DRAGON) {}

      entity(Tags.EntityTypes.BOATS) {}
      entity(Tags.EntityTypes.MINECARTS) {}
      entity(EntityType.ARROW) {}
      entity(EntityType.SPECTRAL_ARROW) {}

      entity(T7EntityTypes.ANGRY_ZOMBIE) {}
    }
}