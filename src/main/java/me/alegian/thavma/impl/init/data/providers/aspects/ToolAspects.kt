package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.data.providers.item
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object ToolAspects {
  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) {
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      item(Items.WOODEN_SWORD) {
        it.add(Aspects.HERBA, 8)
          .add(Aspects.INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_PICKAXE) {
        it.add(Aspects.HERBA, 12)
          .add(Aspects.INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_AXE) {
        it.add(Aspects.HERBA, 12)
          .add(Aspects.INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_SHOVEL) {
        it.add(Aspects.HERBA, 4)
          .add(Aspects.INSTRUMENTUM, 1)
      }
      item(Items.WOODEN_HOE) {
        it.add(Aspects.HERBA, 8)
          .add(Aspects.INSTRUMENTUM, 1)
      }

      item(Items.STONE_SWORD) {
        it.add(Aspects.TERRA, 8)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.STONE_PICKAXE) {
        it.add(Aspects.TERRA, 12)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.STONE_AXE) {
        it.add(Aspects.TERRA, 12)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.STONE_SHOVEL) {
        it.add(Aspects.TERRA, 4)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.STONE_HOE) {
        it.add(Aspects.TERRA, 8)
          .add(Aspects.INSTRUMENTUM, 2)
      }

      item(Items.IRON_SWORD) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.INSTRUMENTUM, 3)
      }
      item(Items.IRON_PICKAXE) {
        it.add(Aspects.METALLUM, 24)
          .add(Aspects.INSTRUMENTUM, 3)
      }
      item(Items.IRON_AXE) {
        it.add(Aspects.METALLUM, 24)
          .add(Aspects.INSTRUMENTUM, 3)
      }
      item(Items.IRON_SHOVEL) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.INSTRUMENTUM, 3)
      }
      item(Items.IRON_HOE) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.INSTRUMENTUM, 3)
      }

      item(T7Items.THAVMITE_SWORD) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.AETHER, 8)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_PICKAXE) {
        it.add(Aspects.METALLUM, 24)
          .add(Aspects.AETHER, 12)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_AXE) {
        it.add(Aspects.METALLUM, 24)
          .add(Aspects.AETHER, 12)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_SHOVEL) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.AETHER, 4)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_HOE) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.AETHER, 8)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(T7Items.THAVMITE_HAMMER) {
        it.add(Aspects.METALLUM, 48)
          .add(Aspects.AETHER, 24)
          .add(Aspects.INSTRUMENTUM, 4)
      }

      item(Items.GOLDEN_SWORD) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_PICKAXE) {
        it.add(Aspects.METALLUM, 12)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_AXE) {
        it.add(Aspects.METALLUM, 12)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_SHOVEL) {
        it.add(Aspects.METALLUM, 4)
          .add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.GOLDEN_HOE) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.INSTRUMENTUM, 2)
      }

      item(Items.DIAMOND_SWORD) {
        it.add(Aspects.VITREUS, 16)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_PICKAXE) {
        it.add(Aspects.VITREUS, 24)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_AXE) {
        it.add(Aspects.VITREUS, 24)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_SHOVEL) {
        it.add(Aspects.VITREUS, 8)
          .add(Aspects.INSTRUMENTUM, 4)
      }
      item(Items.DIAMOND_HOE) {
        it.add(Aspects.VITREUS, 16)
          .add(Aspects.INSTRUMENTUM, 4)
      }

      item(Items.NETHERITE_SWORD) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.INSTRUMENTUM, 6)
          .add(Aspects.TENEBRAE, 8)
      }
      item(Items.NETHERITE_PICKAXE) {
        it.add(Aspects.METALLUM, 48)
          .add(Aspects.INSTRUMENTUM, 6)
          .add(Aspects.TENEBRAE, 12)
      }
      item(Items.NETHERITE_AXE) {
        it.add(Aspects.METALLUM, 48)
          .add(Aspects.INSTRUMENTUM, 6)
          .add(Aspects.TENEBRAE, 12)
      }
      item(Items.NETHERITE_SHOVEL) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.INSTRUMENTUM, 6)
          .add(Aspects.TENEBRAE, 4)
      }
      item(Items.NETHERITE_HOE) {
        it.add(Aspects.METALLUM, 32)
          .add(Aspects.INSTRUMENTUM, 6)
          .add(Aspects.TENEBRAE, 8)
      }

      item(T7Items.IRON_PLATING) {
        it.add(Aspects.METALLUM, 16)
      }
      item(T7Items.GOLD_PLATING) {
        it.add(Aspects.METALLUM, 8)
      }
      item(T7Items.ORICHALCUM_PLATING) {
        it.add(Aspects.METALLUM, 8)
          .add(Aspects.INSTRUMENTUM, 8)
      }
      item(T7Items.THAVMITE_PLATING) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.AETHER, 8)
      }

      item(ItemTags.ARROWS) {
        it.add(Aspects.INSTRUMENTUM, 2)
      }
      item(Items.MACE) {
        it.add(Aspects.METALLUM, 16)
          .add(Aspects.AETHER, 8)
      }
      item(Items.TRIDENT) {
        it.add(Aspects.METALLUM, 12)
          .add(Aspects.AQUA, 8)
      }
      item(Tags.Items.TOOLS_BOW) {
        it.add(Aspects.INSTRUMENTUM, 8)
          .add(Aspects.HERBA, 2)
          .add(Aspects.FABRICO, 2)
      }
      item(Tags.Items.TOOLS_CROSSBOW) {
        it.add(Aspects.INSTRUMENTUM, 8)
          .add(Aspects.METALLUM, 4)
          .add(Aspects.FABRICO, 2)
      }
      item(Items.TOTEM_OF_UNDYING) {
        it.add(Aspects.VICTUS, 16)
          .add(Aspects.AETHER, 8)
      }
    }
  }
}