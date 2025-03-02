package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.block.InfusedBlock
import me.alegian.thavma.impl.common.item.TestaItem
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ESSENTIA_CONTAINER
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.TESTAS
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

class T7BlockLootSubProvider(lookupProvider: HolderLookup.Provider) : BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, lookupProvider) {
  override fun generate() {
    this.dropSelf(GREATWOOD_LOG.get())
    this.dropSelf(GREATWOOD_PLANKS.get())
    this.add(GREATWOOD_LEAVES.get()) { l -> this.createLeavesDrops(l, GREATWOOD_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES) }
    this.dropSelf(GREATWOOD_SAPLING.get())

    this.dropSelf(SILVERWOOD_LOG.get())
    this.dropSelf(SILVERWOOD_PLANKS.get())
    this.add(SILVERWOOD_LEAVES.get()) { l -> this.createLeavesDrops(l, SILVERWOOD_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES) }
    this.dropSelf(SILVERWOOD_SAPLING.get())

    this.dropSelf(CRUCIBLE.get())
    this.dropSelf(AURA_NODE.get()) // TODO: replace
    this.dropSelf(ARCANE_WORKBENCH.get())
    this.dropSelf(MATRIX.get())
    this.dropSelf(PILLAR.get())
    this.dropSelf(PEDESTAL.get())
    this.dropSelf(RESEARCH_TABLE.get())
    this.dropSelf(ELEMENTAL_STONE.get())

    this.dropSelf(ARCANUM_BLOCK.get())
    this.dropSelf(ORICHALCUM_BLOCK.get())

    this.dropSelf(ESSENTIA_CONTAINER.get())

    for (aspect in PRIMAL_ASPECTS) {
      this.infusedBlock(INFUSED_STONES[aspect], TESTAS[aspect])
      this.infusedBlock(INFUSED_DEEPSLATES[aspect], TESTAS[aspect])
    }
  }

  override fun getKnownBlocks(): Iterable<Block> {
    return T7Blocks.REGISTRAR.entries
      .stream()
      .map { e -> e.value() }
      .toList()
  }

  private fun infusedBlock(block: DeferredBlock<InfusedBlock>?, item: DeferredItem<TestaItem>?) {
    if (block == null || item == null) throw IllegalStateException("Thavma Exception: Null block or item when creating loot table for infused ore")
    this.add(block.get()) { b -> this.createOreDrop(b, item.get()) }
  }
}
