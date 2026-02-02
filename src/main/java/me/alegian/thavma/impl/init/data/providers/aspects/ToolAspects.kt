package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object ToolAspects {
  val woodenSword = BlockAndItemAspects.woodenPlanks.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 1))
  val woodenPickaxe = BlockAndItemAspects.woodenPlanks.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 1))
  val woodenAxe = BlockAndItemAspects.woodenPlanks.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 1))
  val woodenShovel = BlockAndItemAspects.woodenPlanks.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 1))
  val woodenHoe = BlockAndItemAspects.woodenPlanks.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 1))

  val stoneSword = BlockAndItemAspects.cobblestone.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 2))
  val stonePickaxe = BlockAndItemAspects.cobblestone.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 2))
  val stoneAxe = BlockAndItemAspects.cobblestone.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 2))
  val stoneShovel = BlockAndItemAspects.cobblestone.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 2))
  val stoneHoe = BlockAndItemAspects.cobblestone.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 2))

  val ironSword = MineralAspects.iron.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 3))
  val ironPickaxe = MineralAspects.iron.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 3))
  val ironAxe = MineralAspects.iron.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 3))
  val ironShovel = MineralAspects.iron.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 3))
  val ironHoe = MineralAspects.iron.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 3))

  val thavmiteSword = MineralAspects.thavmite.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 4))
  val thavmitePickaxe = MineralAspects.thavmite.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 4))
  val thavmiteAxe = MineralAspects.thavmite.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 4))
  val thavmiteShovel = MineralAspects.thavmite.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 4))
  val thavmiteHoe = MineralAspects.thavmite.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 4))
  val thavmiteHammer = MineralAspects.thavmite.mutate(Mutations.hammer(BlockAndItemAspects.rodsWooden, 4))

  val goldenSword = MineralAspects.gold.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 2))
  val goldenPickaxe = MineralAspects.gold.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 2))
  val goldenAxe = MineralAspects.gold.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 2))
  val goldenShovel = MineralAspects.gold.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 2))
  val goldenHoe = MineralAspects.gold.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 2))

  val diamondSword = MineralAspects.diamond.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 4))
  val diamondPickaxe = MineralAspects.diamond.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 4))
  val diamondAxe = MineralAspects.diamond.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 4))
  val diamondShovel = MineralAspects.diamond.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 4))
  val diamondHoe = MineralAspects.diamond.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 4))

  val netheriteSword = MineralAspects.netherite.mutate(Mutations.sword(BlockAndItemAspects.rodsWooden, 6))
  val netheritePickaxe = MineralAspects.netherite.mutate(Mutations.pickaxe(BlockAndItemAspects.rodsWooden, 6))
  val netheriteAxe = MineralAspects.netherite.mutate(Mutations.axe(BlockAndItemAspects.rodsWooden, 6))
  val netheriteShovel = MineralAspects.netherite.mutate(Mutations.shovel(BlockAndItemAspects.rodsWooden, 6))
  val netheriteHoe = MineralAspects.netherite.mutate(Mutations.hoe(BlockAndItemAspects.rodsWooden, 6))

  val ironPlating = MineralAspects.iron.mutate(Mutations.PLATING)
  val goldPlating = MineralAspects.gold.mutate(Mutations.PLATING)
  val orichalcumPlating = MineralAspects.orichalcum.mutate(Mutations.PLATING)
  val thavmitePlating = MineralAspects.thavmite.mutate(Mutations.PLATING)

  val arrows = BlockAndItemAspects.rodsWooden.add(BlockAndItemAspects.flint).add(BlockAndItemAspects.feathers).mutate { it.remove(Aspects.CORPUS) }
  val mace = BlockAndItemAspects.heavyCore.add(BlockAndItemAspects.rodsBreeze).mutate { it.remove(Aspects.CORPUS) }
  val trident = AspectGen().mutate { it.add(Aspects.METALLUM, 12).add(Aspects.AQUA, 8) }
  val bow = BlockAndItemAspects.rodsWooden.mutate { it.scale(3) }.add(BlockAndItemAspects.string.mutate { it.scale(3) }).mutate { it.add(Aspects.INSTRUMENTUM, 8).remove(Aspects.CORPUS) }
  val crossbow = BlockAndItemAspects.rodsWooden.mutate { it.scale(3) }.add(BlockAndItemAspects.string.mutate { it.scale(2) }).add(BlockAndItemAspects.tripwireHook).add(MineralAspects.iron).mutate { it.add(Aspects.INSTRUMENTUM, 8).remove(Aspects.CORPUS) }
  val totemOfUndying = AspectGen().mutate { it.add(Aspects.VICTUS, 16).add(Aspects.AETHER, 8) }

  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      woodenSword.save(this, Items.WOODEN_SWORD)
      woodenPickaxe.save(this, Items.WOODEN_PICKAXE)
      woodenAxe.save(this, Items.WOODEN_AXE)
      woodenShovel.save(this, Items.WOODEN_SHOVEL)
      woodenHoe.save(this, Items.WOODEN_HOE)

      stoneSword.save(this, Items.STONE_SWORD)
      stonePickaxe.save(this, Items.STONE_PICKAXE)
      stoneAxe.save(this, Items.STONE_AXE)
      stoneShovel.save(this, Items.STONE_SHOVEL)
      stoneHoe.save(this, Items.STONE_HOE)

      ironSword.save(this, Items.IRON_SWORD)
      ironPickaxe.save(this, Items.IRON_PICKAXE)
      ironAxe.save(this, Items.IRON_AXE)
      ironShovel.save(this, Items.IRON_SHOVEL)
      ironHoe.save(this, Items.IRON_HOE)

      thavmiteSword.save(this, T7Items.THAVMITE_SWORD)
      thavmitePickaxe.save(this, T7Items.THAVMITE_PICKAXE)
      thavmiteAxe.save(this, T7Items.THAVMITE_AXE)
      thavmiteShovel.save(this, T7Items.THAVMITE_SHOVEL)
      thavmiteHoe.save(this, T7Items.THAVMITE_HOE)
      thavmiteHammer.save(this, T7Items.THAVMITE_HAMMER)

      goldenSword.save(this, Items.GOLDEN_SWORD)
      goldenPickaxe.save(this, Items.GOLDEN_PICKAXE)
      goldenAxe.save(this, Items.GOLDEN_AXE)
      goldenShovel.save(this, Items.GOLDEN_SHOVEL)
      goldenHoe.save(this, Items.GOLDEN_HOE)

      diamondSword.save(this, Items.DIAMOND_SWORD)
      diamondPickaxe.save(this, Items.DIAMOND_PICKAXE)
      diamondAxe.save(this, Items.DIAMOND_AXE)
      diamondShovel.save(this, Items.DIAMOND_SHOVEL)
      diamondHoe.save(this, Items.DIAMOND_HOE)

      netheriteSword.save(this, Items.NETHERITE_SWORD)
      netheritePickaxe.save(this, Items.NETHERITE_PICKAXE)
      netheriteAxe.save(this, Items.NETHERITE_AXE)
      netheriteShovel.save(this, Items.NETHERITE_SHOVEL)
      netheriteHoe.save(this, Items.NETHERITE_HOE)

      ironPlating.save(this, T7Items.IRON_PLATING)
      goldPlating.save(this, T7Items.GOLD_PLATING)
      orichalcumPlating.save(this, T7Items.ORICHALCUM_PLATING)
      thavmitePlating.save(this, T7Items.THAVMITE_PLATING)

      arrows.save(this, ItemTags.ARROWS)
      mace.save(this, Items.MACE)
      trident.save(this, Items.TRIDENT)
      bow.save(this, Tags.Items.TOOLS_BOW)
      crossbow.save(this, Tags.Items.TOOLS_CROSSBOW)
      totemOfUndying.save(this, Items.TOTEM_OF_UNDYING)
    }
  }
}
