package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.providers.aspects.*
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AETHER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALIENIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALKIMIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AVERSIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.COGNITIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.EXANIMIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.FABRICO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.GELUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HUMANUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.INSTRUMENTUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.LUX
import me.alegian.thavma.impl.init.registries.deferred.Aspects.METALLUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.MOTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.POTENTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAECANTATIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRAEMUNIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.SENSUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VACUOS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VICTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VINCULUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VOLATUS
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
    EntityAspects.gather(this, lookupProvider)
    MineralAspects.gather(this, lookupProvider)
    ToolAspects.gather(this, lookupProvider)
    ArmorAspects.gather(this, lookupProvider)
    BlockFamilyAspects.gather(this, lookupProvider)

    val i = builder(T7DataMaps.AspectContent.ITEM)

    i.item(Items.BLAZE_POWDER) {
      it.add(IGNIS, 2)
        .add(POTENTIA, 1)
        .add(ALKIMIA, 2)
    }
    i.item(Tags.Items.GUNPOWDERS) {
      it.add(POTENTIA, 4)
        .add(ALKIMIA, 2)
        .add(TERRA, 1)
    }
    i.item(Tags.Items.RODS_BLAZE) {
      it.add(IGNIS, 8)
        .add(POTENTIA, 4)
    }
    i.item(Tags.Items.RODS_BREEZE) {
      it.add(AER, 8)
        .add(MOTUS, 4)
    }
    i.item(Items.WIND_CHARGE) {
      it.add(AER, 2)
        .add(MOTUS, 1)
    }
    i.item(Items.FIRE_CHARGE) {
      it.add(POTENTIA, 4)
        .add(IGNIS, 4)
        .add(AVERSIO, 2)
    }
    i.item(Tags.Items.LEATHERS) {
      it.add(BESTIA, 8)
    }
    i.item(T7Items.FABRIC) {
      it.add(FABRICO, 4).add(PRAECANTATIO, 1)
    }
    i.item(T7Items.ARCANE_LENS) {
      it.add(METALLUM, 16)
        .add(DESIDERIUM, 32)
        .add(PRAECANTATIO, 2)
        .add(VITREUS, 2)
    }
    i.item(T7Items.ROTTEN_BRAIN) {
      it.add(COGNITIO, 16)
        .add(EXANIMIS, 8)
    }
    i.item(Items.ROTTEN_FLESH) {
      it.add(HUMANUS, 2)
        .add(EXANIMIS, 2)
    }
    i.item(Items.ARMADILLO_SCUTE) {
      it.add(BESTIA, 2)
        .add(PRAEMUNIO, 1)
    }
    i.item(Items.TURTLE_SCUTE) {
      it.add(BESTIA, 2)
        .add(PRAEMUNIO, 1)
        .add(AQUA, 1)
    }
    i.item(Items.FLINT) {
      it.add(TERRA, 4)
        .add(INSTRUMENTUM, 2)
    }
    i.item(Tags.Items.FEATHERS) {
      it.add(VOLATUS, 4)
        .add(AER, 4)
    }
    i.item(Tags.Items.ENDER_PEARLS) {
      it.add(MOTUS, 4)
        .add(ALIENIS, 4)
    }
    i.item(Items.ENDER_EYE) {
      it.add(MOTUS, 4)
        .add(ALIENIS, 4)
        .add(PRAECANTATIO, 2)
    }
    i.item(Tags.Items.STRINGS) {
      it.add(BESTIA, 1)
        .add(FABRICO, 1)
    }
    i.item(Items.GHAST_TEAR) {
      it.add(EXANIMIS, 4)
        .add(ALKIMIA, 4)
    }
    i.item(Items.SUGAR) {
      it.add(HERBA, 2)
        .add(VICTUS, 1)
    }
    i.item(Items.PAPER) {
      it.add(COGNITIO, 1)
        .add(HERBA, 1)
    }
    i.item(Items.FIREWORK_ROCKET) {
      it.add(POTENTIA, 1)
        .add(SENSUS, 1)
    }
    i.item(Items.FIREWORK_STAR) {
      it.add(POTENTIA, 1)
        .add(SENSUS, 2)
    }
    i.item(Items.SNOWBALL) { it.add(GELUM, 1) }
    i.item(Items.NETHER_BRICK) { it.add(TERRA, 1).add(IGNIS, 1) }
    i.item(Items.PRISMARINE_CRYSTALS) { it.add(VITREUS, 4).add(AQUA, 4).add(LUX, 2) }
    i.item(Items.PRISMARINE_SHARD) { it.add(AQUA, 2).add(TERRA, 2) }
    i.item(Items.CHORUS_FRUIT) { it.add(HERBA, 2).add(ALIENIS, 2) }
    i.item(Items.POPPED_CHORUS_FRUIT) { it.add(HERBA, 2).add(ALIENIS, 2) }
    i.item(Items.CLAY_BALL) { it.add(TERRA, 1).add(AQUA, 1) }
    i.item(Items.BRICK) { it.add(TERRA, 1).add(IGNIS, 1) }

    i.item(Tags.Items.RODS_WOODEN) { it.add(HERBA, 2) }

    i.item(Tags.Items.EGGS) { it.add(VICTUS, 4).add(BESTIA, 2) }

    i.item(Tags.Items.CROPS_WHEAT) { it.add(VICTUS, 2).add(HERBA, 2) }

    i.item(Items.END_CRYSTAL) {
      it.add(VITREUS, 14)
        .add(POTENTIA, 8)
        .add(ALIENIS, 4)
        .add(IGNIS, 4)
    }

    i.item(Blocks.NETHER_WART) { it.add(HERBA, 2).add(IGNIS, 2) }
    i.item(Blocks.CHORUS_PLANT) { it.add(HERBA, 2).add(ALIENIS, 2) }
    i.item(Blocks.CHORUS_FLOWER) { it.add(HERBA, 4).add(ALIENIS, 4) }
    i.item(Tags.Items.NETHERRACKS) { it.add(TERRA, 1).add(IGNIS, 1) }
    i.item(Blocks.MUD) { it.add(TERRA, 1).add(AQUA, 1) }
    i.item(Blocks.PACKED_MUD) { it.add(TERRA, 2).add(HERBA, 2) }
    i.item(ItemTags.DIRT) { it.add(TERRA, 1) }
    i.item(Blocks.COARSE_DIRT) { it.add(TERRA, 1) }
    i.item(Tags.Items.GRAVELS) { it.add(TERRA, 2) }
    i.item(Tags.Items.STONES) { it.add(TERRA, 2) }
    i.item(Tags.Items.COBBLESTONES) { it.add(TERRA, 2) }
    i.item(Tags.Items.SANDS) { it.add(TERRA, 2) }
    i.item(Blocks.CLAY) { it.add(TERRA, 4).add(AQUA, 4) }

    i.item(Blocks.GRASS_BLOCK) {
      it.add(TERRA, 1).add(HERBA, 1)
    }
    i.item(Blocks.PODZOL) {
      it.add(TERRA, 1).add(HERBA, 1)
    }
    i.item(Blocks.SHORT_GRASS) {
      it.add(HERBA, 1).add(AER, 1)
    }
    i.item(Blocks.TALL_GRASS) {
      it.add(HERBA, 1).add(AER, 1)
    }

    i.item(ItemTags.TERRACOTTA) {
      it.add(TERRA, 4)
        .add(IGNIS, 4)
        .add(SENSUS, 1)
    }
    i.item(Tags.Items.CONCRETE_POWDERS) {
      it.add(TERRA, 3)
    }
    i.item(Tags.Items.CONCRETES) {
      it.add(TERRA, 3)
        .add(AQUA, 1)
        .add(AETHER, 1)
    }

    i.item(ItemTags.LEAVES) { it.add(HERBA, 2) }
    i.item(ItemTags.SAPLINGS) { it.add(HERBA, 4).add(VICTUS, 4) }
    i.item(ItemTags.LOGS) { it.add(HERBA, 8) }
    i.item(ItemTags.FLOWERS) { it.add(HERBA, 4).add(VICTUS, 1) }

    i.item(Blocks.SUGAR_CANE) {
      it.add(HERBA, 2)
        .add(AQUA, 1)
        .add(AER, 1)
    }

    i.item(T7Blocks.TABLE) { it.add(HERBA, 12) }
    i.item(T7Blocks.RESEARCH_TABLE) {
      it.add(HERBA, 12)
        .add(PRAECANTATIO, 2)
    }

    i.item(ItemTags.WOOL) {
      it.add(BESTIA, 4).add(SENSUS, 2).add(FABRICO, 4)
    }

    i.item(Tags.Items.GLASS_BLOCKS_CHEAP) { it.add(VITREUS, 2) }
    i.item(Tags.Items.GLASS_PANES) { it.add(VITREUS, 1) }

    i.item(ItemTags.WOOL) { it.add(BESTIA, 8).add(FABRICO, 4) }

    i.item(Blocks.TORCH) { it.add(LUX, 4) }

    i.item(Blocks.SNOW) { it.add(GELUM, 1) }
    i.item(Blocks.SNOW_BLOCK) { it.add(GELUM, 4) }
    i.item(Blocks.POWDER_SNOW) { it.add(GELUM, 2) }

    i.item(Blocks.BEDROCK) {
      it.add(VACUOS, 25)
        .add(TERRA, 25)
        .add(TENEBRAE, 25)
    }
    i.item(T7Blocks.ETERNAL_FLAME) {
      it.add(LUX, 12)
        .add(POTENTIA, 8)
        .add(IGNIS, 8)
    }
    i.item(T7Blocks.CRACKED_ELEMENTAL_STONE) {
      it.add(TERRA, 4)
        .add(PRAECANTATIO, 1)
    }
    i.item(T7Blocks.ELEMENTAL_STONE_BRICKS) {
      it.add(TERRA, 4)
        .add(PRAECANTATIO, 1)
    }
    i.item(T7Blocks.ELEMENTAL_CORE) {
      it.add(TERRA, 6)
        .add(PRAECANTATIO, 2)
    }
    i.item(Blocks.CRAFTING_TABLE) {
      it.add(FABRICO, 8)
        .add(HERBA, 4)
    }
    i.item(T7Blocks.ARCANE_WORKBENCH) {
      it.add(FABRICO, 12)
        .add(HERBA, 4)
        .add(PRAECANTATIO, 2)
    }
    i.item(Items.CAULDRON) {
      it.add(METALLUM, 56)
        .add(ALKIMIA, 8)
    }
    i.item(T7Blocks.CRUCIBLE) {
      it.add(METALLUM, 56)
        .add(ALKIMIA, 8)
        .add(PRAECANTATIO, 2)
    }
    i.item(T7Blocks.SEALING_JAR) {
      it.add(VITREUS, 4)
        .add(VACUOS, 2)
        .add(PRAECANTATIO, 2)
    }
    i.item(Blocks.TNT) {
      it.add(POTENTIA, 20)
        .add(TERRA, 1)
    }
    i.item(Blocks.TRIPWIRE_HOOK) {
      it.add(VINCULUM, 2)
        .add(METALLUM, 2)
    }
    i.item(Blocks.HEAVY_CORE) {
      it.add(METALLUM, 16)
        .add(DESIDERIUM, 8)
        .add(VACUOS, 2)
    }
    i.item(Blocks.BAMBOO) { it.add(HERBA, 1) }
    i.item(ItemTags.BAMBOO_BLOCKS) { it.add(HERBA, 4) }
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
