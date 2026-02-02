package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.block.InfusedBlock
import me.alegian.thavma.impl.common.item.ShardItem
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_LEVITATOR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRACKED_ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_BRICKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_STAIRS
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
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SHARDS
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.BedBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

private object BlastResistant {
  val SET = listOf<ItemLike>(T7Blocks.SEALING_JAR).map { it.asItem() }.toSet()
}

class T7BlockLootSubProvider(lookupProvider: HolderLookup.Provider) : BlockLootSubProvider(BlastResistant.SET, FeatureFlags.DEFAULT_FLAGS, lookupProvider) {
  override fun generate() {
    dropSelf(GREATWOOD_LOG.get())
    dropSelf(GREATWOOD_PLANKS.get())
    add(GREATWOOD_LEAVES.get()) { l -> this.createLeavesDrops(l, GREATWOOD_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES) }
    dropSelf(GREATWOOD_SAPLING.get())

    dropSelf(SILVERWOOD_LOG.get())
    dropSelf(SILVERWOOD_PLANKS.get())
    add(SILVERWOOD_LEAVES.get()) { l -> this.createLeavesDrops(l, SILVERWOOD_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES) }
    dropSelf(SILVERWOOD_SAPLING.get())

    dropSelf(CRUCIBLE.get())
    dropSelf(ARCANE_WORKBENCH.get())
    dropSelf(MATRIX.get())
    dropSelf(PEDESTAL.get())
    dropSelf(TABLE.get())
    dropSelf(ITEM_HATCH.get())
    add(RESEARCH_TABLE.get()) { b -> createSinglePropConditionTable(b, BedBlock.PART, BedPart.HEAD) }
    add(T7Blocks.ETERNAL_FLAME.get()) { b -> createBooleanPropertyTable(b, T7BlockStateProperties.FAKE, false) }
    add(PILLAR.get()) { b -> createBooleanPropertyTable(b, T7BlockStateProperties.MASTER, true) }
    dropSelf(ELEMENTAL_STONE.get())
    dropSelf(ELEMENTAL_CORE.get())
    dropSelf(CRACKED_ELEMENTAL_STONE.get())
    dropSelf(ELEMENTAL_STONE_BRICKS.get())

    dropSelf(ARCANE_LEVITATOR.get())

    dropSelf(THAVMITE_BLOCK.get())
    dropSelf(ORICHALCUM_BLOCK.get())

    add(T7Blocks.SEALING_JAR.get()) { b -> createJarTable(b) }

    for (aspect in PRIMAL_ASPECTS) {
      infusedBlock(INFUSED_STONES[aspect], SHARDS[aspect])
      infusedBlock(INFUSED_DEEPSLATES[aspect], SHARDS[aspect])
    }

    dropSelf(HUNGRY_CHEST.get())
    dropSelf(ELEMENTAL_STONE_STAIRS.get())
    add(ELEMENTAL_STONE_SLAB.get()) { b -> createSlabItemTable(b) }
    dropSelf(GREATWOOD_STAIRS.get())
    add(GREATWOOD_SLAB.get()) { b -> createSlabItemTable(b) }
  }

  override fun getKnownBlocks(): Iterable<Block> {
    return T7Blocks.REGISTRAR.entries
      .stream()
      .map { e -> e.value() }
      .toList()
  }

  private fun infusedBlock(block: DeferredBlock<InfusedBlock>?, item: DeferredItem<ShardItem>?) {
    if (block == null || item == null) throw IllegalStateException("Thavma Exception: Null block or item when creating loot table for infused ore")
    this.add(block.get()) { b -> this.createOreDrop(b, item.get()) }
  }

  /**
   * modified createSinglePropConditionTable, allows for normal booleans...
   */
  private fun createBooleanPropertyTable(
    block: Block, property: BooleanProperty, value: Boolean
  ): LootTable.Builder {
    return LootTable.lootTable()
      .withPool(
        applyExplosionCondition(
          block,
          LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0f))
            .add(
              LootItem.lootTableItem(block)
                .`when`(
                  LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value))
                )
            )
        )
      )
  }

  private fun createJarTable(block: Block): LootTable.Builder {
    return LootTable.lootTable()
      .withPool(
        applyExplosionCondition(
          block,
          LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0f))
            .add(
              LootItem.lootTableItem(block)
                .apply(
                  CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                    .include(T7DataComponents.ASPECTS.get())
                )
            )
        )
      )
  }
}
