package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7BlockTagProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider?>, existingFileHelper: ExistingFileHelper?) : BlockTagsProvider(output, lookupProvider, Thavma.MODID, existingFileHelper) {
  override fun addTags(pProvider: HolderLookup.Provider) {
    for (infusedBlock in (INFUSED_STONES.values + INFUSED_DEEPSLATES.values)) {
      tag(Tags.Blocks.ORES).add(infusedBlock.get())
      tag(Tags.Blocks.ORE_RATES_SINGULAR).add(infusedBlock.get())
      tag(BlockTags.MINEABLE_WITH_PICKAXE).add(infusedBlock.get())
      tag(BlockTags.NEEDS_STONE_TOOL).add(infusedBlock.get())
      tag(T7Tags.INFUSED_STONES).add(infusedBlock.get())
    }

    tag(T7Tags.CrucibleHeatSourceTag.BLOCK)
      .addTag(BlockTags.FIRE)
      .addTag(BlockTags.CAMPFIRES)
      .add(T7Blocks.ETERNAL_FLAME.get())
    tag(BlockTags.NEEDS_IRON_TOOL).add(
      THAVMITE_BLOCK.get(),
      ORICHALCUM_BLOCK.get()
    )
    tag(BlockTags.MINEABLE_WITH_AXE).add(
      GREATWOOD_LOG.get(),
      SILVERWOOD_LOG.get(),
      GREATWOOD_PLANKS.get(),
      SILVERWOOD_PLANKS.get()
    )

    tag(BlockTags.LEAVES).add(GREATWOOD_LEAVES.get(), SILVERWOOD_LEAVES.get())
    tag(BlockTags.LOGS_THAT_BURN).add(GREATWOOD_LOG.get(), SILVERWOOD_LOG.get())
    tag(BlockTags.SAPLINGS).add(GREATWOOD_SAPLING.get(), SILVERWOOD_SAPLING.get())
    tag(BlockTags.PLANKS).add(GREATWOOD_PLANKS.get(), SILVERWOOD_PLANKS.get())

    tag(BlockTags.WOODEN_SLABS).add(GREATWOOD_SLAB.get())
    tag(BlockTags.WOODEN_STAIRS).add(GREATWOOD_STAIRS.get())
    tag(BlockTags.SLABS).add(ELEMENTAL_STONE_SLAB.get())
    tag(BlockTags.STAIRS).add(ELEMENTAL_STONE_STAIRS.get())

    tag(Tags.Blocks.STORAGE_BLOCKS).add(
      THAVMITE_BLOCK.get(),
      ORICHALCUM_BLOCK.get()
    )

    tag(BlockTags.BEACON_BASE_BLOCKS).add(
      THAVMITE_BLOCK.get(),
      ORICHALCUM_BLOCK.get()
    )
  }
}
