package me.alegian.thavma.impl.init.data.providers.aspects

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.T7BlockFamilies
import me.alegian.thavma.impl.init.data.providers.T7DataMapProvider
import me.alegian.thavma.impl.init.registries.T7DataMaps
import net.minecraft.core.HolderLookup
import net.minecraft.data.BlockFamilies
import net.minecraft.data.BlockFamily
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.DataMapProvider.Builder

object BlockFamilyAspects {

  fun gather(datamapProvider: T7DataMapProvider, lookupProvider: HolderLookup.Provider) =
    datamapProvider.builder(T7DataMaps.AspectContent.ITEM).run {
      blockFamily(BlockFamilies.ACACIA_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.CHERRY_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.BIRCH_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.CRIMSON_PLANKS, BlockAndItemAspects.netherPlanks)
      blockFamily(BlockFamilies.JUNGLE_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.OAK_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.DARK_OAK_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.SPRUCE_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.WARPED_PLANKS, BlockAndItemAspects.netherPlanks)
      blockFamily(BlockFamilies.MANGROVE_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.BAMBOO_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(BlockFamilies.BAMBOO_MOSAIC, BlockAndItemAspects.bambooMosaic)
      blockFamily(BlockFamilies.MUD_BRICKS, BlockAndItemAspects.mudBricks)
      blockFamily(BlockFamilies.ANDESITE, BlockAndItemAspects.andesite)
      blockFamily(BlockFamilies.POLISHED_ANDESITE, BlockAndItemAspects.polishedAndesite)
      blockFamily(BlockFamilies.BLACKSTONE, BlockAndItemAspects.blackstone)
      blockFamily(BlockFamilies.POLISHED_BLACKSTONE, BlockAndItemAspects.polishedBlackstone)
      blockFamily(BlockFamilies.POLISHED_BLACKSTONE_BRICKS, BlockAndItemAspects.polishedBlackstoneBricks)
      blockFamily(BlockFamilies.BRICKS, BlockAndItemAspects.bricks)
      blockFamily(BlockFamilies.END_STONE_BRICKS, BlockAndItemAspects.endStoneBricks)
      blockFamily(BlockFamilies.MOSSY_STONE_BRICKS, BlockAndItemAspects.mossyStoneBricks)
      blockFamily(BlockFamilies.COPPER_BLOCK, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_COPPER_BLOCK, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.EXPOSED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.EXPOSED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_EXPOSED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_EXPOSED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WEATHERED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WEATHERED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_WEATHERED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_WEATHERED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.OXIDIZED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.OXIDIZED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_OXIDIZED_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.WAXED_OXIDIZED_CUT_COPPER, MineralAspects.copperBlock)
      blockFamily(BlockFamilies.COBBLESTONE, BlockAndItemAspects.cobblestone)
      blockFamily(BlockFamilies.MOSSY_COBBLESTONE, BlockAndItemAspects.mossyCobblestone)
      blockFamily(BlockFamilies.DIORITE, BlockAndItemAspects.diorite)
      blockFamily(BlockFamilies.POLISHED_DIORITE, BlockAndItemAspects.polishedDiorite)
      blockFamily(BlockFamilies.GRANITE, BlockAndItemAspects.granite)
      blockFamily(BlockFamilies.POLISHED_GRANITE, BlockAndItemAspects.polishedGranite)
      blockFamily(BlockFamilies.TUFF, BlockAndItemAspects.tuff)
      blockFamily(BlockFamilies.POLISHED_TUFF, BlockAndItemAspects.polishedTuff)
      blockFamily(BlockFamilies.TUFF_BRICKS, BlockAndItemAspects.tuffBricks)
      blockFamily(BlockFamilies.NETHER_BRICKS, BlockAndItemAspects.netherBricks)
      blockFamily(BlockFamilies.RED_NETHER_BRICKS, BlockAndItemAspects.redNetherBricks)
      blockFamily(BlockFamilies.PRISMARINE, BlockAndItemAspects.prismarine)
      blockFamily(BlockFamilies.PURPUR, BlockAndItemAspects.purpur)
      blockFamily(BlockFamilies.PRISMARINE_BRICKS, BlockAndItemAspects.prismarineBricks)
      blockFamily(BlockFamilies.DARK_PRISMARINE, BlockAndItemAspects.darkPrismarine)
      blockFamily(BlockFamilies.QUARTZ, MineralAspects.quartzBlock)
      blockFamily(BlockFamilies.SMOOTH_QUARTZ, BlockAndItemAspects.smoothQuartz)
      blockFamily(BlockFamilies.SANDSTONE, BlockAndItemAspects.sandstone)
      blockFamily(BlockFamilies.CUT_SANDSTONE, BlockAndItemAspects.cutSandstone)
      blockFamily(BlockFamilies.SMOOTH_SANDSTONE, BlockAndItemAspects.smoothSandstone)
      blockFamily(BlockFamilies.RED_SANDSTONE, BlockAndItemAspects.sandstone)
      blockFamily(BlockFamilies.CUT_RED_SANDSTONE, BlockAndItemAspects.cutSandstone)
      blockFamily(BlockFamilies.SMOOTH_RED_SANDSTONE, BlockAndItemAspects.smoothSandstone)
      blockFamily(BlockFamilies.STONE, BlockAndItemAspects.stone)
      blockFamily(BlockFamilies.STONE_BRICK, BlockAndItemAspects.stoneBrick)
      blockFamily(BlockFamilies.DEEPSLATE, BlockAndItemAspects.deepslate)
      blockFamily(BlockFamilies.COBBLED_DEEPSLATE, BlockAndItemAspects.cobbledDeepslate)
      blockFamily(BlockFamilies.POLISHED_DEEPSLATE, BlockAndItemAspects.polishedDeepslate)
      blockFamily(BlockFamilies.DEEPSLATE_BRICKS, BlockAndItemAspects.deepslateBricks)
      blockFamily(BlockFamilies.DEEPSLATE_TILES, BlockAndItemAspects.deepslateTiles)
      blockFamily(T7BlockFamilies.GREATWOOD_PLANKS, BlockAndItemAspects.woodenPlanks)
      blockFamily(T7BlockFamilies.ELEMENTAL_STONE, BlockAndItemAspects.elementalStone)
    }
}

private fun Builder<AspectMap, Item>.blockFamily(blockFamily: BlockFamily, baseGen: AspectGen) {
  fun saveVariant(block: Block?, mutation: (AspectMap) -> AspectMap) {
    if (block == null) return
    baseGen.mutate(mutation).save(this, block)
  }

  baseGen.save(this, blockFamily.baseBlock)
  saveVariant(blockFamily.get(BlockFamily.Variant.BUTTON), Mutations.BUTTON)
  saveVariant(blockFamily.get(BlockFamily.Variant.CHISELED), Mutations.CHISELED)
  saveVariant(blockFamily.get(BlockFamily.Variant.CRACKED), Mutations.CRACKED)
  saveVariant(blockFamily.get(BlockFamily.Variant.CUT), Mutations.CUT)
  saveVariant(blockFamily.get(BlockFamily.Variant.DOOR), Mutations.DOOR)
  saveVariant(blockFamily.get(BlockFamily.Variant.CUSTOM_FENCE), Mutations.FENCE)
  saveVariant(blockFamily.get(BlockFamily.Variant.FENCE), Mutations.FENCE)
  saveVariant(blockFamily.get(BlockFamily.Variant.CUSTOM_FENCE_GATE), Mutations.FENCE_GATE)
  saveVariant(blockFamily.get(BlockFamily.Variant.FENCE_GATE), Mutations.FENCE_GATE)
  saveVariant(blockFamily.get(BlockFamily.Variant.MOSAIC), Mutations.MOSAIC)
  saveVariant(blockFamily.get(BlockFamily.Variant.SIGN), Mutations.SIGN)
  saveVariant(blockFamily.get(BlockFamily.Variant.SLAB), Mutations.SLAB)
  saveVariant(blockFamily.get(BlockFamily.Variant.STAIRS), Mutations.STAIRS)
  saveVariant(blockFamily.get(BlockFamily.Variant.PRESSURE_PLATE), Mutations.PRESSURE_PLATE)
  saveVariant(blockFamily.get(BlockFamily.Variant.POLISHED), Mutations.POLISHED)
  saveVariant(blockFamily.get(BlockFamily.Variant.TRAPDOOR), Mutations.TRAPDOOR)
  saveVariant(blockFamily.get(BlockFamily.Variant.WALL), Mutations.WALL)
  saveVariant(blockFamily.get(BlockFamily.Variant.WALL_SIGN), Mutations.WALL_SIGN)
}
