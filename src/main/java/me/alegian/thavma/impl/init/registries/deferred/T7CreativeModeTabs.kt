package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_LEVITATOR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRACKED_ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_BRICKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ETERNAL_FLAME
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.HUNGRY_CHEST
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ITEM_HATCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SEALING_JAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.GREATWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.SILVERWOOD
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.GOLD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.IRON
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.THAVMITE
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredRegister

object T7CreativeModeTabs {
  val REGISTRAR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Thavma.MODID)

  init {
    REGISTRAR.register(
      Thavma.MODID
    ) { ->
      CreativeModeTab
        .builder()
        .title(Component.translatable(Thavma.MODID))
        .icon { T7Items.BOOK.get().defaultInstance }
        .displayItems { _, output ->
          output.accept(AURA_NODE.get())
          output.accept(SEALING_JAR.get())

          output.accept(CRUCIBLE.get())
          output.accept(ARCANE_WORKBENCH.get())
          output.accept(ITEM_HATCH.get())
          output.accept(TABLE.get())
          output.accept(RESEARCH_TABLE.get())
          output.accept(MATRIX.get())
          output.accept(PILLAR.get())
          output.accept(PEDESTAL.get())
          output.accept(PEDESTAL.get())

          output.accept(ELEMENTAL_STONE.get())
          output.accept(ELEMENTAL_STONE_STAIRS.get())
          output.accept(ELEMENTAL_STONE_SLAB.get())
          output.accept(ELEMENTAL_CORE.get())
          output.accept(CRACKED_ELEMENTAL_STONE.get())
          output.accept(ELEMENTAL_STONE_BRICKS.get())

          output.accept(ARCANE_LEVITATOR.get())

          for (infusedStone in INFUSED_STONES.values) output.accept(infusedStone.get())
          for (infusedStone in INFUSED_DEEPSLATES.values) output.accept(infusedStone.get())

          output.accept(THAVMITE_BLOCK.get())
          output.accept(ORICHALCUM_BLOCK.get())

          output.accept(GREATWOOD_LEAVES.get())
          output.accept(GREATWOOD_LOG.get())
          output.accept(GREATWOOD_PLANKS.get())
          output.accept(GREATWOOD_STAIRS.get())
          output.accept(GREATWOOD_SLAB.get())
          output.accept(GREATWOOD_SAPLING.get())

          output.accept(SILVERWOOD_LEAVES.get())
          output.accept(SILVERWOOD_SAPLING.get())
          output.accept(SILVERWOOD_LOG.get())
          output.accept(SILVERWOOD_PLANKS.get())

          output.accept(ETERNAL_FLAME.get())
          output.accept(HUNGRY_CHEST.get())

          output.accept(T7Items.IRON_HANDLE)
          output.accept(T7Items.GOLD_HANDLE)
          output.accept(T7Items.ORICHALCUM_HANDLE)
          output.accept(T7Items.THAVMITE_HANDLE)

          output.accept(T7Items.EYE_OF_WARDEN)
          output.accept(T7Items.ROTTEN_BRAIN)
          output.accept(T7Items.SIGIL)
          output.accept(T7Items.FABRIC)

          output.accept(T7Items.GREATWOOD_CORE)
          output.accept(T7Items.SILVERWOOD_CORE)

          output.accept(T7Items.RUNE)
          output.accept(T7Items.THAVMITE_INGOT)
          output.accept(T7Items.THAVMITE_NUGGET)
          output.accept(T7Items.ORICHALCUM_INGOT)
          output.accept(T7Items.ORICHALCUM_NUGGET)
          output.accept(T7Items.RESEARCH_SCROLL)
          output.accept(T7Items.ARCANE_LENS)
          output.accept(T7Items.BOOK)

          output.accept(T7Items.wandOrThrow(IRON.get(), WOOD.get()))
          output.accept(
            T7Items.wandOrThrow(
              GOLD.get(),
              GREATWOOD.get()
            )
          )
          output.accept(
            T7Items.wandOrThrow(
              THAVMITE.get(),
              SILVERWOOD.get()
            )
          )

          output.accept(T7Items.GOGGLES)
          output.accept(T7Items.GOGGLES_CURIO)
          output.accept(T7Items.DAWN_CHARM)
          output.accept(T7Items.APPRENTICE_CHESTPLATE)
          output.accept(T7Items.APPRENTICE_LEGGINGS)
          output.accept(T7Items.APPRENTICE_BOOTS)

          output.accept(T7Items.THAVMITE_HELMET)
          output.accept(T7Items.THAVMITE_CHESTPLATE)
          output.accept(T7Items.THAVMITE_LEGGINGS)
          output.accept(T7Items.THAVMITE_BOOTS)

          output.accept(T7Items.THAVMITE_VANGUARD_HELMET)
          output.accept(T7Items.THAVMITE_VANGUARD_CHESTPLATE)
          output.accept(T7Items.THAVMITE_VANGUARD_LEGGINGS)
          output.accept(T7Items.THAVMITE_VANGUARD_BOOTS)

          for (shard in T7Items.SHARDS.values) output.accept(shard)

          output.accept(T7Items.THAVMITE_AXE)
          output.accept(T7Items.THAVMITE_PICKAXE)
          output.accept(T7Items.THAVMITE_HAMMER)
          output.accept(T7Items.THAVMITE_SHOVEL)
          output.accept(T7Items.THAVMITE_HOE)

          output.accept(T7Items.THAVMITE_SWORD)
          output.accept(T7Items.THAVMITE_KATANA)
          output.accept(T7Items.ZEPHYR)

          output.accept(T7Items.ANGRY_ZOMBIE_SPAWN_EGG)
        }
        .build()
    }
  }
}
