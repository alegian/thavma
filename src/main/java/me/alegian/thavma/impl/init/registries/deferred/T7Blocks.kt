package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.block.*
import me.alegian.thavma.impl.init.data.worldgen.tree.GreatwoodTree
import me.alegian.thavma.impl.init.data.worldgen.tree.SilverwoodTree
import net.minecraft.core.Direction
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object T7Blocks {
  val REGISTRAR = DeferredRegister.createBlocks(Thavma.MODID)

  val AURA_NODE = register(
    "aura_node"
  ) { AuraNodeBlock() }

  val SEALING_JAR = register(
    "sealing_jar",
    ::SealingJarBlock
  )

  val CRUCIBLE = register(
    "crucible"
  ) { CrucibleBlock() }

  val ARCANE_WORKBENCH = register(
    "arcane_workbench"
  ) { WorkbenchBlock() }

  val MATRIX = register(
    "infusion_matrix"
  ) { MatrixBlock() }

  val PILLAR = register(
    "infusion_pillar"
  ) { PillarBlock() }

  val PEDESTAL = register(
    "infusion_pedestal"
  ) { PedestalBlock() }

  val ITEM_HATCH = register(
    "item_hatch"
  ) { ItemHatch() }

  val TABLE = register(
    "table"
  ) { TableBlock() }

  val RESEARCH_TABLE = register(
    "research_table"
  ) { ResearchTableBlock() }

  val ELEMENTAL_STONE = register("elemental_stone") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)) }
  val ELEMENTAL_STONE_SLAB = register("elemental_stone_slab") { SlabBlock(BlockBehaviour.Properties.ofFullCopy(ELEMENTAL_STONE.get())) }
  val ELEMENTAL_STONE_STAIRS = register("elemental_stone_stairs") { StairBlock(ELEMENTAL_STONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ELEMENTAL_STONE.get())) }
  val ELEMENTAL_CORE = register("elemental_core") { Block(BlockBehaviour.Properties.ofFullCopy(ELEMENTAL_STONE.get())) }
  val CRACKED_ELEMENTAL_STONE = register("cracked_elemental_stone") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CRACKED_STONE_BRICKS)) }
  val ELEMENTAL_STONE_BRICKS = register("elemental_stone_bricks") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)) }

  val ARCANE_LEVITATOR = register("arcane_levitator") { ArcaneLevitatorBlock() }
  val LEVITATOR_COLUMN = register("arcane_levitator_column") { LevitatorColumnBlock() }

  val INFUSED_STONES = linkedMapWithPrimalKeys { aspect ->
    register(aspect.id.path + "_infused_stone") { InfusedBlock(aspect, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)) { Blocks.STONE } }
  }

  val INFUSED_DEEPSLATES = linkedMapWithPrimalKeys { aspect ->
    register(aspect.id.path + "_infused_deepslate") { InfusedBlock(aspect, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)) { Blocks.DEEPSLATE } }
  }

  val GREATWOOD_LEAVES = register(
    "greatwood_leaves",
    ::leaves
  )

  val GREATWOOD_LOG = register(
    "greatwood_log"
  ) { log(MapColor.WOOD, MapColor.PODZOL) }

  val GREATWOOD_PLANKS = register("greatwood_planks", ::plank)
  val GREATWOOD_SLAB = register("greatwood_slab") { SlabBlock(BlockBehaviour.Properties.ofFullCopy(GREATWOOD_PLANKS.get())) }
  val GREATWOOD_STAIRS = register("greatwood_stairs") { StairBlock(GREATWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GREATWOOD_PLANKS.get())) }

  val GREATWOOD_SAPLING = register(
    "greatwood_sapling"
  ) { sapling(GreatwoodTree.GROWER) }

  val SILVERWOOD_LEAVES = register(
    "silverwood_leaves",
    ::leaves
  )

  val SILVERWOOD_LOG = register(
    "silverwood_log"
  ) { log(MapColor.WOOD, MapColor.PODZOL) }

  val SILVERWOOD_PLANKS = register(
    "silverwood_planks",
    ::plank
  )

  val SILVERWOOD_SAPLING = register(
    "silverwood_sapling"
  ) { sapling(SilverwoodTree.GROWER) }

  val THAVMITE_BLOCK = register(
    "thavmite_block"
  ) { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)) }

  val ORICHALCUM_BLOCK = register(
    "orichalcum_block"
  ) { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)) }

  val ETERNAL_FLAME = register("eternal_flame") { EternalFlameBlock() }

  val HUNGRY_CHEST = register("hungry_chest") { HungryChestBlock() }

  private fun <T : Block> register(name: String, sup: Supplier<T>): DeferredBlock<T> {
    val block = REGISTRAR.register(name, sup)
    T7Items.REGISTRAR.registerSimpleBlockItem(name, block)
    return block
  }

  private fun leaves(): LeavesBlock {
    return LeavesBlock(
      BlockBehaviour.Properties.of()
        .mapColor(MapColor.PLANT)
        .strength(0.2f)
        .randomTicks()
        .sound(SoundType.GRASS)
        .noOcclusion()
        .isValidSpawn { state, blockGetter, pos, entity ->
          Blocks.ocelotOrParrot(
            state,
            blockGetter,
            pos,
            entity
          )
        }
        .isSuffocating { _, _, _ -> false }
        .isViewBlocking { _, _, _ -> false }
        .ignitedByLava()
        .pushReaction(PushReaction.DESTROY)
        .isRedstoneConductor { _, _, _ -> false }
    )
  }

  private fun log(pTopMapColor: MapColor, pSideMapColor: MapColor): RotatedPillarBlock {
    return RotatedPillarBlock(
      BlockBehaviour.Properties.of()
        .mapColor { blockState ->
          if (blockState.getValue(
              RotatedPillarBlock.AXIS
            ) === Direction.Axis.Y
          ) pTopMapColor else pSideMapColor
        }
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0f)
        .sound(SoundType.WOOD)
        .ignitedByLava()
    )
  }

  private fun plank(): Block {
    return Block(
      BlockBehaviour.Properties.of()
        .mapColor(MapColor.WOOD)
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0f, 3.0f)
        .sound(SoundType.WOOD)
        .ignitedByLava()
    )
  }

  private fun sapling(treeGrower: TreeGrower): SaplingBlock {
    return SaplingBlock(
      treeGrower,
      BlockBehaviour.Properties.of()
        .mapColor(MapColor.PLANT)
        .noCollission()
        .randomTicks()
        .instabreak()
        .sound(SoundType.GRASS)
        .pushReaction(PushReaction.DESTROY)
    )
  }
}
