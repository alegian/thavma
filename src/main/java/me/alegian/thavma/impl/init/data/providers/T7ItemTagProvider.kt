package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.APPRENTICE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOLD_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GREATWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SHARDS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.SILVERWOOD_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_KATANA
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PLATING
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SWORD
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_VANGUARD_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ZEPHYR
import me.alegian.thavma.impl.integration.curios.CuriosIntegration
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7ItemTagProvider(pOutput: PackOutput, pLookupProvider: CompletableFuture<HolderLookup.Provider?>, pBlockTags: CompletableFuture<TagLookup<Block?>?>, pExistingFileHelper: ExistingFileHelper?) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, Thavma.MODID, pExistingFileHelper) {
  override fun addTags(lookupProvider: HolderLookup.Provider) {
    tag(T7Tags.Items.WAND_PLATINGS).add(
      IRON_PLATING.get(),
      GOLD_PLATING.get(),
      ORICHALCUM_PLATING.get(),
      THAVMITE_PLATING.get()
    )

    tag(T7Tags.Items.WAND_CORES)
      .addTag(Tags.Items.RODS_WOODEN)
      .add(
        GREATWOOD_CORE.get(),
        SILVERWOOD_CORE.get()
      )

    for (shard in SHARDS.values)
      tag(T7Tags.Items.SHARDS).add(shard.get())

    tag(Tags.Items.GEMS).addTag(T7Tags.Items.SHARDS)

    tag(Tags.Items.INGOTS).add(
      THAVMITE_INGOT.get(),
      ORICHALCUM_INGOT.get()
    )

    tag(T7Tags.Items.FOCI).add(
      T7Items.FOCUS_EMBERS.get(),
      T7Items.FOCUS_EXCAVATION.get(),
      T7Items.FOCUS_ENDERCHEST.get(),
      T7Items.FOCUS_LIGHT.get(),
      T7Items.FOCUS_HOLE.get(),
      T7Items.FOCUS_ENDERPEARL.get(),
      T7Items.FOCUS_EXCHANGE.get(),
      T7Items.FOCUS_LIGHTNING.get(),
    )

    tag(Tags.Items.NUGGETS).add(
      THAVMITE_NUGGET.get(),
      ORICHALCUM_NUGGET.get()
    )

    tag(ItemTags.SWORDS).add(
      THAVMITE_SWORD.get(),
      THAVMITE_KATANA.get(),
      ZEPHYR.get()
    )
    tag(ItemTags.AXES).add(
      T7Items.THAVMITE_AXE.get(),
      T7Items.AXE_OF_THE_FOREST.get(),
    )
    tag(ItemTags.PICKAXES).add(THAVMITE_PICKAXE.get())
    tag(ItemTags.MINING_ENCHANTABLE).add(THAVMITE_HAMMER.get())
    tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(THAVMITE_HAMMER.get())
    tag(Tags.Items.MINING_TOOL_TOOLS).add(THAVMITE_HAMMER.get())
    tag(ItemTags.SHOVELS).add(THAVMITE_SHOVEL.get())
    tag(ItemTags.HOES).add(THAVMITE_HOE.get())

    tag(ItemTags.FOOT_ARMOR).add(
      T7Items.THAVMITE_BOOTS.get(),
      T7Items.THAVMITE_VANGUARD_BOOTS.get(),
      APPRENTICE_BOOTS.get()
    )
    tag(ItemTags.LEG_ARMOR).add(
      THAVMITE_LEGGINGS.get(),
      THAVMITE_VANGUARD_LEGGINGS.get(),
      APPRENTICE_LEGGINGS.get()
    )
    tag(ItemTags.CHEST_ARMOR).add(
      THAVMITE_CHESTPLATE.get(),
      THAVMITE_VANGUARD_CHESTPLATE.get(),
      APPRENTICE_CHESTPLATE.get()
    )
    tag(ItemTags.HEAD_ARMOR).add(
      T7Items.GOGGLES.get(),
      THAVMITE_HELMET.get(),
      THAVMITE_VANGUARD_HELMET.get()
    )

    tag(T7Tags.Items.CATALYSTS).add(
      Items.GLOWSTONE_DUST,
      Items.IRON_INGOT,
      Items.COPPER_INGOT,
    )

    tag(T7Tags.Items.GOGGLES).add(
      T7Items.GOGGLES.get(),
      T7Items.GOGGLES_CURIO.get(),
    )

    tag(T7Tags.Items.STEP_HEIGHT).add(
      T7Items.THAVMITE_BOOTS.get(),
      T7Items.THAVMITE_VANGUARD_BOOTS.get()
    )

    tag(T7Tags.Items.TREE_FELLING).add(
      T7Items.AXE_OF_THE_FOREST.get()
    )

    copy(BlockTags.LEAVES, ItemTags.LEAVES)
    copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN)
    copy(BlockTags.PLANKS, ItemTags.PLANKS)
    copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS)
    copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS)
    copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS)
    copy(BlockTags.SLABS, ItemTags.SLABS)
    copy(BlockTags.STAIRS, ItemTags.STAIRS)
    copy(Tags.Blocks.ORES, Tags.Items.ORES)
    copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR)
    copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
    copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
    copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
    copy(T7Tags.Blocks.INFUSED_STONES, T7Tags.Items.INFUSED_STONES)

    CuriosIntegration.get().addTags(this, lookupProvider)
  }

  // override visibility
  public override fun tag(tag: TagKey<Item>): IntrinsicTagAppender<Item> {
    return super.tag(tag)
  }
}
