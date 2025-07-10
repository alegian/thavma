package me.alegian.thavma.impl.init.data

import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.data.BlockFamily

object T7BlockFamilies {
  val GREATWOOD_PLANKS = BlockFamily.Builder(T7Blocks.GREATWOOD_PLANKS.get())
    .slab(T7Blocks.GREATWOOD_SLAB.get())
    .stairs(T7Blocks.GREATWOOD_STAIRS.get())
    .family

  val ELEMENTAL_STONE = BlockFamily.Builder(T7Blocks.ELEMENTAL_STONE.get())
    .slab(T7Blocks.ELEMENTAL_STONE_SLAB.get())
    .stairs(T7Blocks.ELEMENTAL_STONE_STAIRS.get())
    .family
}