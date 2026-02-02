package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Items

object ArmorAspects {
  val ironHelmet = MineralAspects.iron.mutate(Mutations.helmet(4))
  val ironChestplate = MineralAspects.iron.mutate(Mutations.chestplate(12))
  val ironLeggings = MineralAspects.iron.mutate(Mutations.leggings(10))
  val ironBoots = MineralAspects.iron.mutate(Mutations.boots(4))
  val ironHorseArmor = MineralAspects.iron.mutate(Mutations.horseArmor(6))

  val goldHelmet = MineralAspects.gold.mutate(Mutations.helmet(4))
  val goldChestplate = MineralAspects.gold.mutate(Mutations.chestplate(10))
  val goldLeggings = MineralAspects.gold.mutate(Mutations.leggings(6))
  val goldBoots = MineralAspects.gold.mutate(Mutations.boots(2))
  val goldHorseArmor = MineralAspects.gold.mutate(Mutations.horseArmor(8))

  val diamondHelmet = MineralAspects.diamond.mutate(Mutations.helmet(8))
  val diamondChestplate = MineralAspects.diamond.mutate(Mutations.chestplate(18))
  val diamondLeggings = MineralAspects.diamond.mutate(Mutations.leggings(14))
  val diamondBoots = MineralAspects.diamond.mutate(Mutations.boots(8))
  val diamondHorseArmor = MineralAspects.diamond.mutate(Mutations.horseArmor(12))

  private val netheriteUpgrade = MineralAspects.netherite.mutate { it.add(Aspects.PRAEMUNIO, 1) }
  val netheriteHelmet = diamondHelmet.add(netheriteUpgrade)
  val netheriteChestplate = diamondChestplate.add(netheriteUpgrade)
  val netheriteLeggings = diamondLeggings.add(netheriteUpgrade)
  val netheriteBoots = diamondBoots.add(netheriteUpgrade)

  val thavmiteHelmet = MineralAspects.thavmite.mutate(Mutations.helmet(7))
  val thavmiteChestplate = MineralAspects.thavmite.mutate(Mutations.chestplate(13))
  val thavmiteLeggings = MineralAspects.thavmite.mutate(Mutations.leggings(11))
  val thavmiteBoots = MineralAspects.thavmite.mutate(Mutations.boots(5))

  private val chainBase = MineralAspects.iron.mutate { it.scale(0.5) }
  val chainHelmet = chainBase.mutate(Mutations.helmet(4))
  val chainChestplate = chainBase.mutate(Mutations.chestplate(10))
  val chainLeggings = chainBase.mutate(Mutations.leggings(8))
  val chainBoots = chainBase.mutate(Mutations.boots(2))

  val leatherHelmet = BlockAndItemAspects.leather.mutate(Mutations.helmet(2))
  val leatherChestplate = BlockAndItemAspects.leather.mutate(Mutations.chestplate(6))
  val leatherLeggings = BlockAndItemAspects.leather.mutate(Mutations.leggings(4))
  val leatherBoots = BlockAndItemAspects.leather.mutate(Mutations.boots(2))
  val leatherHorseArmor = BlockAndItemAspects.leather.mutate(Mutations.horseArmor(4))

  val goggles = BlockAndItemAspects.arcaneLens.mutate { it.scale(2) }.add(MineralAspects.orichalcum.mutate { it.scale(2) })
  val apprenticeChestplate = BlockAndItemAspects.fabric.mutate(Mutations.chestplate(2))
  val apprenticeLeggings = BlockAndItemAspects.fabric.mutate(Mutations.leggings(2))
  val apprenticeBoots = BlockAndItemAspects.fabric.mutate(Mutations.boots(2))

  val wolfArmor = BlockAndItemAspects.armadilloScute.mutate(Mutations.wolfArmor(0))
  val turtleHelmet = BlockAndItemAspects.turtleScute.mutate(Mutations.helmet(0))

  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      leatherHelmet.save(this, Items.LEATHER_HELMET)
      leatherChestplate.save(this, Items.LEATHER_CHESTPLATE)
      leatherLeggings.save(this, Items.LEATHER_LEGGINGS)
      leatherBoots.save(this, Items.LEATHER_BOOTS)
      leatherHorseArmor.save(this, Items.LEATHER_HORSE_ARMOR)

      goggles.save(this, T7Tags.Items.GOGGLES)
      apprenticeChestplate.save(this, T7Items.APPRENTICE_CHESTPLATE)
      apprenticeLeggings.save(this, T7Items.APPRENTICE_LEGGINGS)
      apprenticeBoots.save(this, T7Items.APPRENTICE_BOOTS)

      chainHelmet.save(this, Items.CHAINMAIL_HELMET)
      chainChestplate.save(this, Items.CHAINMAIL_CHESTPLATE)
      chainLeggings.save(this, Items.CHAINMAIL_LEGGINGS)
      chainBoots.save(this, Items.CHAINMAIL_BOOTS)

      ironHelmet.save(this, Items.IRON_HELMET)
      ironChestplate.save(this, Items.IRON_CHESTPLATE)
      ironLeggings.save(this, Items.IRON_LEGGINGS)
      ironBoots.save(this, Items.IRON_BOOTS)
      ironHorseArmor.save(this, Items.IRON_HORSE_ARMOR)

      goldHelmet.save(this, Items.GOLDEN_HELMET)
      goldChestplate.save(this, Items.GOLDEN_CHESTPLATE)
      goldLeggings.save(this, Items.GOLDEN_LEGGINGS)
      goldBoots.save(this, Items.GOLDEN_BOOTS)
      goldHorseArmor.save(this, Items.GOLDEN_HORSE_ARMOR)

      diamondHelmet.save(this, Items.DIAMOND_HELMET)
      diamondChestplate.save(this, Items.DIAMOND_CHESTPLATE)
      diamondLeggings.save(this, Items.DIAMOND_LEGGINGS)
      diamondBoots.save(this, Items.DIAMOND_BOOTS)
      diamondHorseArmor.save(this, Items.DIAMOND_HORSE_ARMOR)

      netheriteHelmet.save(this, Items.NETHERITE_HELMET)
      netheriteChestplate.save(this, Items.NETHERITE_CHESTPLATE)
      netheriteLeggings.save(this, Items.NETHERITE_LEGGINGS)
      netheriteBoots.save(this, Items.NETHERITE_BOOTS)

      thavmiteHelmet.save(this, T7Items.THAVMITE_HELMET)
      thavmiteChestplate.save(this, T7Items.THAVMITE_CHESTPLATE)
      thavmiteLeggings.save(this, T7Items.THAVMITE_LEGGINGS)
      thavmiteBoots.save(this, T7Items.THAVMITE_BOOTS)

      wolfArmor.save(this, Items.WOLF_ARMOR)
      turtleHelmet.save(this, Items.TURTLE_HELMET)
    }
  }
}