package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.providers.aspects.*
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.BlockFamily
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.common.data.DataMapProvider.Builder
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Supplier

class T7DataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
  DataMapProvider(packOutput, lookupProvider) {
  override fun gather(lookupProvider: HolderLookup.Provider) {
    AspectRelations.gather(this)
    EntityAspects.gather(this, lookupProvider)
    MineralAspects.gather(this, lookupProvider)
    ToolAspects.gather(this, lookupProvider)
    ArmorAspects.gather(this, lookupProvider)
    BlockFamilyAspects.gather(this, lookupProvider)

    val i = builder(T7DataMaps.AspectContent.ITEM)

    i.item(Items.BLAZE_POWDER) {
      it.add(Aspects.IGNIS, 2)
        .add(Aspects.ALKIMIA, 1)
    }
    i.item(Tags.Items.GUNPOWDERS) {
      it.add(Aspects.IGNIS, 4)
        .add(Aspects.ALKIMIA, 2)
        .add(Aspects.TERRA, 1)
    }
    i.item(Tags.Items.RODS_BLAZE) {
      it.add(Aspects.IGNIS, 4)
        .add(Aspects.ALKIMIA, 2)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Tags.Items.RODS_BREEZE) {
      it.add(Aspects.AETHER, 6)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Items.WIND_CHARGE) {
      it.add(Aspects.ALKIMIA, 4)
        .add(Aspects.AETHER, 4)
        .add(Aspects.INSTRUMENTUM, 2)
    }
    i.item(Items.FIRE_CHARGE) {
      it.add(Aspects.ALKIMIA, 4)
        .add(Aspects.IGNIS, 4)
        .add(Aspects.INSTRUMENTUM, 2)
    }
    i.item(Tags.Items.LEATHERS) {
      it.add(Aspects.CORPUS, 3)
    }
    i.item(T7Items.FABRIC) {
      it.add(Aspects.FABRICO, 4).add(Aspects.AETHER, 1)
    }
    i.item(T7Items.ARCANE_LENS) {
      it.add(Aspects.METALLUM, 16)
        .add(Aspects.AETHER, 2)
        .add(Aspects.VITREUS, 2)
    }
    i.item(T7Items.ROTTEN_BRAIN) {
      it.add(Aspects.COGNITIO, 12)
        .add(Aspects.HOSTILIS, 2)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Items.ROTTEN_FLESH) {
      it.add(Aspects.HOSTILIS, 2)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Items.ARMADILLO_SCUTE) {
      it.add(Aspects.CORPUS, 2)
        .add(Aspects.PRAEMUNIO, 1)
    }
    i.item(Items.TURTLE_SCUTE) {
      it.add(Aspects.CORPUS, 2)
        .add(Aspects.PRAEMUNIO, 1)
        .add(Aspects.AQUA, 1)
    }
    i.item(Items.FLINT) {
      it.add(Aspects.TERRA, 4)
        .add(Aspects.INSTRUMENTUM, 2)
    }
    i.item(Tags.Items.FEATHERS) {
      it.add(Aspects.INSTRUMENTUM, 1)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Tags.Items.ENDER_PEARLS) {
      it.add(Aspects.INSTRUMENTUM, 4)
        .add(Aspects.TENEBRAE, 4)
        .add(Aspects.CORPUS, 2)
    }
    i.item(Items.ENDER_EYE) {
      it.add(Aspects.INSTRUMENTUM, 4)
        .add(Aspects.TENEBRAE, 4)
        .add(Aspects.AETHER, 2)
    }
    i.item(Tags.Items.STRINGS) {
      it.add(Aspects.CORPUS, 1)
        .add(Aspects.FABRICO, 1)
    }
    i.item(Items.GHAST_TEAR) {
      it.add(Aspects.HOSTILIS, 4)
        .add(Aspects.ALKIMIA, 4)
    }
    i.item(Items.SUGAR) {
      it.add(Aspects.HERBA, 2)
        .add(Aspects.VICTUS, 1)
    }
    i.item(Items.PAPER) {
      it.add(Aspects.COGNITIO, 1)
        .add(Aspects.HERBA, 1)
    }
    i.item(Items.FIREWORK_ROCKET) {
      it.add(Aspects.IGNIS, 1)
        .add(Aspects.ORNATUS, 1)
    }
    i.item(Items.FIREWORK_STAR) {
      it.add(Aspects.IGNIS, 1)
        .add(Aspects.ORNATUS, 2)
    }
    i.item(Items.SNOWBALL) { it.add(Aspects.AQUA, 1) }
    i.item(Items.NETHER_BRICK) { it.add(Aspects.TERRA, 1).add(Aspects.IGNIS, 1) }
    i.item(Items.PRISMARINE_CRYSTALS) { it.add(Aspects.VITREUS, 4).add(Aspects.AQUA, 4).add(Aspects.LUX, 2) }
    i.item(Items.PRISMARINE_SHARD) { it.add(Aspects.AQUA, 2).add(Aspects.TERRA, 2) }
    i.item(Items.CHORUS_FRUIT) { it.add(Aspects.HERBA, 2).add(Aspects.TENEBRAE, 2) }
    i.item(Items.POPPED_CHORUS_FRUIT) { it.add(Aspects.HERBA, 2).add(Aspects.TENEBRAE, 2) }
    i.item(Items.CLAY_BALL) { it.add(Aspects.TERRA, 1).add(Aspects.AQUA, 1) }
    i.item(Items.BRICK) { it.add(Aspects.TERRA, 1).add(Aspects.IGNIS, 1) }

    i.item(Tags.Items.RODS_WOODEN) { it.add(Aspects.HERBA, 2) }

    i.item(Tags.Items.EGGS) { it.add(Aspects.VICTUS, 4).add(Aspects.CORPUS, 2) }

    i.item(Tags.Items.CROPS_WHEAT) { it.add(Aspects.VICTUS, 2).add(Aspects.HERBA, 2) }

    i.item(Items.END_CRYSTAL) {
      it.add(Aspects.VITREUS, 14)
        .add(Aspects.IGNIS, 8)
        .add(Aspects.TENEBRAE, 4)
        .add(Aspects.AETHER, 4)
    }

    i.item(Blocks.NETHER_WART) { it.add(Aspects.HERBA, 2).add(Aspects.IGNIS, 2) }
    i.item(Blocks.CHORUS_PLANT) { it.add(Aspects.HERBA, 2).add(Aspects.TENEBRAE, 2) }
    i.item(Blocks.CHORUS_FLOWER) { it.add(Aspects.HERBA, 4).add(Aspects.TENEBRAE, 4) }
    i.item(Tags.Items.NETHERRACKS) { it.add(Aspects.TERRA, 1).add(Aspects.IGNIS, 1) }
    i.item(Blocks.MUD) { it.add(Aspects.TERRA, 1).add(Aspects.AQUA, 1) }
    i.item(Blocks.PACKED_MUD) { it.add(Aspects.TERRA, 2).add(Aspects.HERBA, 2) }
    i.item(ItemTags.DIRT) { it.add(Aspects.TERRA, 1) }
    i.item(Blocks.COARSE_DIRT) { it.add(Aspects.TERRA, 1) }
    i.item(Tags.Items.GRAVELS) { it.add(Aspects.TERRA, 2) }
    i.item(Tags.Items.STONES) { it.add(Aspects.TERRA, 2) }
    i.item(Tags.Items.COBBLESTONES) { it.add(Aspects.TERRA, 2) }
    i.item(Tags.Items.SANDS) { it.add(Aspects.TERRA, 2) }
    i.item(Blocks.CLAY) { it.add(Aspects.TERRA, 4).add(Aspects.AQUA, 4) }

    i.item(Blocks.GRASS_BLOCK) {
      it.add(Aspects.TERRA, 1).add(Aspects.HERBA, 1)
    }
    i.item(Blocks.PODZOL) {
      it.add(Aspects.TERRA, 1).add(Aspects.HERBA, 1)
    }
    i.item(Blocks.SHORT_GRASS) {
      it.add(Aspects.HERBA, 1)
    }
    i.item(Blocks.TALL_GRASS) {
      it.add(Aspects.HERBA, 1)
    }

    i.item(ItemTags.TERRACOTTA) {
      it.add(Aspects.TERRA, 4)
        .add(Aspects.IGNIS, 4)
        .add(Aspects.ORNATUS, 1)
    }
    i.item(Tags.Items.CONCRETE_POWDERS) {
      it.add(Aspects.TERRA, 3)
    }
    i.item(Tags.Items.CONCRETES) {
      it.add(Aspects.TERRA, 3)
        .add(Aspects.AQUA, 1)
        .add(Aspects.AETHER, 1)
    }

    i.item(ItemTags.LEAVES) { it.add(Aspects.HERBA, 2) }
    i.item(ItemTags.SAPLINGS) { it.add(Aspects.HERBA, 4).add(Aspects.VICTUS, 4) }
    i.item(ItemTags.LOGS) { it.add(Aspects.HERBA, 8) }
    i.item(ItemTags.FLOWERS) { it.add(Aspects.HERBA, 4).add(Aspects.VICTUS, 1) }

    i.item(Blocks.SUGAR_CANE) {
      it.add(Aspects.HERBA, 2)
        .add(Aspects.AQUA, 1)
    }

    i.item(T7Blocks.TABLE) { it.add(Aspects.HERBA, 12) }
    i.item(T7Blocks.RESEARCH_TABLE) {
      it.add(Aspects.HERBA, 12)
        .add(Aspects.AETHER, 2)
    }

    i.item(ItemTags.WOOL) {
      it.add(Aspects.CORPUS, 4).add(Aspects.ORNATUS, 2).add(Aspects.FABRICO, 4)
    }

    i.item(Tags.Items.GLASS_BLOCKS_CHEAP) { it.add(Aspects.VITREUS, 2) }
    i.item(Tags.Items.GLASS_PANES) { it.add(Aspects.VITREUS, 1) }

    i.item(ItemTags.WOOL) { it.add(Aspects.CORPUS, 2).add(Aspects.FABRICO, 4) }

    i.item(Blocks.TORCH) { it.add(Aspects.LUX, 4) }

    i.item(Blocks.SNOW) { it.add(Aspects.AQUA, 1) }
    i.item(Blocks.SNOW_BLOCK) { it.add(Aspects.AQUA, 4) }
    i.item(Blocks.POWDER_SNOW) { it.add(Aspects.AQUA, 2) }

    i.item(Blocks.BEDROCK) {
      it.add(Aspects.TERRA, 25)
        .add(Aspects.TENEBRAE, 25)
    }
    i.item(T7Blocks.ETERNAL_FLAME) {
      it.add(Aspects.LUX, 12)
        .add(Aspects.IGNIS, 8)
    }
    i.item(T7Blocks.CRACKED_ELEMENTAL_STONE) {
      it.add(Aspects.TERRA, 4)
        .add(Aspects.AETHER, 1)
    }
    i.item(T7Blocks.ELEMENTAL_STONE_BRICKS) {
      it.add(Aspects.TERRA, 4)
        .add(Aspects.AETHER, 1)
    }
    i.item(T7Blocks.ELEMENTAL_CORE) {
      it.add(Aspects.TERRA, 6)
        .add(Aspects.AETHER, 2)
    }
    i.item(Blocks.CRAFTING_TABLE) {
      it.add(Aspects.FABRICO, 8)
        .add(Aspects.HERBA, 4)
    }
    i.item(T7Blocks.ARCANE_WORKBENCH) {
      it.add(Aspects.FABRICO, 12)
        .add(Aspects.HERBA, 4)
        .add(Aspects.AETHER, 2)
    }
    i.item(Items.CAULDRON) {
      it.add(Aspects.METALLUM, 56)
        .add(Aspects.ALKIMIA, 8)
    }
    i.item(T7Blocks.CRUCIBLE) {
      it.add(Aspects.METALLUM, 56)
        .add(Aspects.ALKIMIA, 8)
        .add(Aspects.AETHER, 2)
    }
    i.item(T7Blocks.SEALING_JAR) {
      it.add(Aspects.VITREUS, 4)
        .add(Aspects.ALKIMIA, 2)
        .add(Aspects.AETHER, 2)
    }
    i.item(Blocks.TNT) {
      it.add(Aspects.ALKIMIA, 8)
        .add(Aspects.IGNIS, 8)
        .add(Aspects.TERRA, 1)
    }
    i.item(Blocks.TRIPWIRE_HOOK) {
      it.add(Aspects.MACHINA, 2)
        .add(Aspects.METALLUM, 1)
    }
    i.item(Blocks.HEAVY_CORE) {
      it.add(Aspects.METALLUM, 16)
        .add(Aspects.INSTRUMENTUM, 6)
    }
    i.item(Blocks.BAMBOO) { it.add(Aspects.HERBA, 1) }
    i.item(ItemTags.BAMBOO_BLOCKS) { it.add(Aspects.HERBA, 4) }
  }
}

fun Builder<AspectMap, Item>.blockFamily(blockFamily: BlockFamily, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  val aspects = aspectBuilder.build()

  fun addFamilyVariant(block: Block?, multiplier: Number) {
    if (block == null) return
    add(key(block.asItem()), aspects.scale(multiplier), false)
  }

  addFamilyVariant(blockFamily.baseBlock, 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.BUTTON), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.CHISELED), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.CRACKED), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.CUT), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.DOOR), 2)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.CUSTOM_FENCE), 1.5)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.FENCE), 1.5)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.CUSTOM_FENCE_GATE), 4)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.FENCE_GATE), 4)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.MOSAIC), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.SIGN), 2)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.SLAB), 0.5)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.STAIRS), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.PRESSURE_PLATE), 2)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.POLISHED), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.TRAPDOOR), 3)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.WALL), 1)
  addFamilyVariant(blockFamily.get(BlockFamily.Variant.WALL_SIGN), 2)
}

fun Builder<AspectMap, Item>.item(item: ItemLike, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(key(item.asItem()), aspectBuilder.build(), false)
}

fun <T : Item> Builder<AspectMap, Item>.item(sup: Supplier<T>, builderConsumer: Consumer<AspectMap.Builder>) =
  item(sup.get(), builderConsumer)

fun Builder<AspectMap, Item>.item(tag: TagKey<Item>, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(tag, aspectBuilder.build(), false)
}

fun Builder<AspectMap, EntityType<*>>.entity(entityType: EntityType<*>, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(key(entityType), aspectBuilder.build(), false)
}

fun <T : EntityType<*>> Builder<AspectMap, EntityType<*>>.entity(sup: Supplier<T>, builderConsumer: Consumer<AspectMap.Builder>) =
  entity(sup.get(), builderConsumer)

fun Builder<AspectMap, EntityType<*>>.entity(tag: TagKey<EntityType<*>>, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(tag, aspectBuilder.build(), false)
}

private fun key(entityType: EntityType<*>): ResourceKey<EntityType<*>> {
  return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow()
}

private fun key(item: Item): ResourceKey<Item> {
  return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow()
}
