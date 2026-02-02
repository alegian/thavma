package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.entity
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
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
        it.add(Aspects.TERRA, 8)
          .add(Aspects.VICTUS, 8)
      }
      entity(EntityType.CREEPER) {
        it.add(Aspects.IGNIS, 15)
          .add(Aspects.HERBA, 15)
      }
      entity(EntityType.DOLPHIN) {}
      entity(EntityType.DONKEY) {}
      entity(EntityType.DROWNED) {}
      entity(EntityType.ELDER_GUARDIAN) {}
      entity(EntityType.ENDERMAN) {
        it.add(Aspects.TENEBRAE, 8)
          .add(Aspects.HOSTILIS, 4)
          .add(Aspects.CIVILIS, 4)
      }
      entity(EntityType.ENDERMITE) {}
      entity(EntityType.EVOKER) {}
      entity(EntityType.FOX) {}
      entity(EntityType.FROG) {}
      entity(EntityType.GHAST) {
        it.add(Aspects.HOSTILIS, 12)
          .add(Aspects.IGNIS, 8)
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
        it.add(Aspects.TERRA, 8)
          .add(Aspects.VICTUS, 8)
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
        it.add(Aspects.TERRA, 8)
          .add(Aspects.VICTUS, 8)
      }
      entity(EntityType.SHULKER) {}
      entity(EntityType.SILVERFISH) {}
      entity(EntityType.SKELETON) {
        it.add(Aspects.TERRA, 8)
          .add(Aspects.HOSTILIS, 8)
      }
      entity(EntityType.SKELETON_HORSE) {}
      entity(EntityType.SLIME) {
        it.add(Aspects.ALKIMIA, 5)
          .add(Aspects.AQUA, 10)
          .add(Aspects.VICTUS, 10)
      }
      entity(EntityType.SNIFFER) {}
      entity(EntityType.SNOW_GOLEM) {}
      entity(EntityType.SPIDER) {
        it.add(Aspects.TERRA, 8)
          .add(Aspects.HOSTILIS, 8)
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
        it.add(Aspects.CIVILIS, 12)
      }
      entity(EntityType.VINDICATOR) {}
      entity(EntityType.WANDERING_TRADER) {}
      entity(EntityType.WARDEN) {}
      entity(EntityType.WITCH) {}
      entity(EntityType.WITHER_SKELETON) {}
      entity(EntityType.WOLF) {}
      entity(EntityType.ZOGLIN) {}
      entity(EntityType.ZOMBIE) {
        it.add(Aspects.TERRA, 8)
          .add(Aspects.HOSTILIS, 8)
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