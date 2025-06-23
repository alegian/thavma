package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.providers.aspects.ArmorAspects
import me.alegian.thavma.impl.init.data.providers.aspects.EntityAspects
import me.alegian.thavma.impl.init.data.providers.aspects.MineralAspects
import me.alegian.thavma.impl.init.data.providers.aspects.ToolAspects
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AETHER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALKIMIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AVERSIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.COGNITIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.EXANIMIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.FABRICO
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
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VOLATUS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.common.data.DataMapProvider.Builder
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class T7DataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
  DataMapProvider(packOutput, lookupProvider) {
  override fun gather(lookupProvider: HolderLookup.Provider) {
    EntityAspects.gather(this, lookupProvider)
    MineralAspects.gather(this, lookupProvider)
    ToolAspects.gather(this, lookupProvider)
    ArmorAspects.gather(this, lookupProvider)

    val b = builder(T7DataMaps.AspectContent.BLOCK)
    val i = builder(T7DataMaps.AspectContent.ITEM)

    i.item(Items.BLAZE_POWDER) {
      it.add(IGNIS.get(), 4)
        .add(POTENTIA.get(), 2)
        .add(ALKIMIA.get(), 2)
    }
    i.item(Tags.Items.GUNPOWDERS) {
      it.add(POTENTIA.get(), 4)
        .add(ALKIMIA.get(), 2)
        .add(TERRA.get(), 1)
    }
    i.item(Tags.Items.RODS_BLAZE) {
      it.add(IGNIS.get(), 12)
        .add(POTENTIA.get(), 8)
    }
    i.item(Tags.Items.RODS_BREEZE) {
      it.add(AER.get(), 16)
        .add(MOTUS.get(), 4)
    }
    i.item(Items.WIND_CHARGE) {
      it.add(AER.get(), 4)
        .add(MOTUS.get(), 1)
    }
    i.item(Items.FIRE_CHARGE) {
      it.add(POTENTIA.get(), 4)
        .add(IGNIS.get(), 4)
        .add(AVERSIO.get(), 2)
    }
    i.item(Tags.Items.LEATHERS) {
      it.add(BESTIA.get(), 8)
    }
    i.item(T7Items.FABRIC.get()){
      it.add(FABRICO.get(), 4).add(PRAECANTATIO.get(), 1)
    }
    i.item(T7Items.ARCANE_LENS.get()){
      it.add(METALLUM.get(), 16)
        .add(DESIDERIUM.get(), 32)
        .add(PRAECANTATIO.get(), 2)
        .add(VITREUS.get(), 2)
    }
    i.item(T7Tags.Items.GOGGLES){
      it.add(METALLUM.get(), 32)
        .add(DESIDERIUM.get(), 32)
        .add(PRAECANTATIO.get(), 4)
        .add(VITREUS.get(), 4)
    }
    i.item(T7Items.ROTTEN_BRAIN.get()){
      it.add(COGNITIO.get(), 16)
        .add(EXANIMIS.get(), 8)
    }
    i.item(Items.ROTTEN_FLESH){
      it.add(HUMANUS.get(), 2)
        .add(EXANIMIS.get(), 2)
    }
    i.item(Items.ARMADILLO_SCUTE){
      it.add(BESTIA, 2)
        .add(PRAEMUNIO, 1)
    }
    i.item(Items.FLINT){
      it.add(TERRA, 4)
        .add(INSTRUMENTUM, 2)
    }
    i.item(Tags.Items.FEATHERS){
      it.add(VOLATUS, 4)
        .add(AER, 4)
    }

    b.block(Tags.Blocks.STONES) { it.add(TERRA.get(), 4) }
    b.block(Blocks.STONE_STAIRS) { it.add(TERRA.get(), 6) }
    b.block(Blocks.STONE_SLAB) { it.add(TERRA.get(), 2) }
    b.block(Blocks.DIRT) { it.add(TERRA.get(), 4) }

    b.block(Blocks.POLISHED_GRANITE) { it.add(TERRA.get(), 3) }
    b.block(Blocks.POLISHED_DIORITE) { it.add(TERRA.get(), 3) }
    b.block(Blocks.POLISHED_ANDESITE) { it.add(TERRA.get(), 3) }
    b.block(Blocks.COARSE_DIRT) { it.add(TERRA.get(), 3) }

    b.block(Blocks.GRASS_BLOCK) {
      it.add(TERRA.get(), 5).add(HERBA.get(), 2)
    }
    b.block(Blocks.PODZOL) {
      it.add(TERRA.get(), 5).add(HERBA.get(), 1)
    }
    b.block(Blocks.SHORT_GRASS) {
      it.add(HERBA.get(), 5).add(AER.get(), 1)
    }
    b.block(Blocks.TALL_GRASS) {
      it.add(HERBA.get(), 5).add(AER.get(), 1)
    }

    b.block(Tags.Blocks.COBBLESTONES) {
      it.add(TERRA.get(), 4)
    }
    b.block(Tags.Blocks.SANDS) {
      it.add(TERRA.get(), 4)
    }

    b.block(BlockTags.TERRACOTTA) {
      it.add(AQUA.get(), 15)
        .add(TERRA.get(), 15)
        .add(IGNIS.get(), 1)
        .add(SENSUS.get(), 1)
    }
    b.block(BlockTags.CONCRETE_POWDER) {
      it.add(TERRA.get(), 3)
    }
    b.block(Tags.Blocks.CONCRETES) {
      it.add(TERRA.get(), 3)
        .add(AQUA.get(), 1)
        .add(AETHER.get(), 1)
    }

    i.item(Tags.Items.RODS_WOODEN) { it.add(HERBA.get(), 2) }
    b.block(BlockTags.PLANKS) { it.add(HERBA.get(), 4) }
    b.block(BlockTags.LEAVES) { it.add(HERBA.get(), 4) }
    b.block(BlockTags.SAPLINGS) { it.add(HERBA.get(), 8).add(VICTUS.get(), 4) }
    b.block(BlockTags.WOODEN_STAIRS) { it.add(HERBA.get(), 6) }
    b.block(BlockTags.WOODEN_SLABS) { it.add(HERBA.get(), 2) }
    b.block(BlockTags.LOGS) { it.add(HERBA.get(), 16) }
    b.block(BlockTags.FLOWERS) { it.add(HERBA.get(), 4).add(VICTUS.get(), 1) }

    b.block(T7Blocks.TABLE.get()) { it.add(HERBA.get(), 12) }
    b.block(T7Blocks.RESEARCH_TABLE.get()) {
      it.add(HERBA.get(), 12)
        .add(PRAECANTATIO.get(), 2)
    }

    b.block(BlockTags.WOOL) {
      it.add(BESTIA.get(), 11).add(SENSUS.get(), 3).add(FABRICO.get(), 3)
    }

    b.block(Tags.Blocks.GLASS_BLOCKS_CHEAP) { it.add(VITREUS.get(), 4) }
    b.block(Tags.Blocks.GLASS_PANES) { it.add(VITREUS.get(), 1) }

    b.block(BlockTags.WOOL) { it.add(BESTIA.get(), 8).add(FABRICO.get(), 4) }

    b.block(Blocks.TORCH) { it.add(LUX.get(), 4) }

    b.block(Blocks.BEDROCK) {
      it.add(VACUOS.get(), 25)
        .add(TERRA.get(), 25)
        .add(TENEBRAE.get(), 25)
    }
    b.block(T7Blocks.ETERNAL_FLAME.get()) {
      it.add(LUX.get(), 12)
        .add(POTENTIA.get(), 8)
        .add(IGNIS.get(), 8)
    }
    b.block(T7Blocks.ELEMENTAL_STONE.get()) {
      it.add(TERRA.get(), 4)
        .add(PRAECANTATIO.get(), 1)
    }
    b.block(T7Blocks.CRACKED_ELEMENTAL_STONE.get()) {
      it.add(TERRA.get(), 4)
        .add(PRAECANTATIO.get(), 1)
    }
    b.block(T7Blocks.ELEMENTAL_STONE_STAIRS.get()) {
      it.add(TERRA.get(), 6)
        .add(PRAECANTATIO.get(), 1)
    }
    b.block(T7Blocks.ELEMENTAL_STONE_SLAB.get()) {
      it.add(TERRA.get(), 2)
    }
    b.block(T7Blocks.ELEMENTAL_STONE_BRICKS.get()) {
      it.add(TERRA.get(), 4)
        .add(PRAECANTATIO.get(), 1)
    }
    b.block(T7Blocks.ELEMENTAL_CORE.get()) {
      it.add(TERRA.get(), 6)
        .add(PRAECANTATIO.get(), 2)
    }
    b.block(Blocks.CRAFTING_TABLE) {
      it.add(FABRICO.get(), 8)
        .add(HERBA.get(), 4)
    }
    b.block(T7Blocks.ARCANE_WORKBENCH.get()) {
      it.add(FABRICO.get(), 12)
        .add(HERBA.get(), 4)
        .add(PRAECANTATIO.get(), 2)
    }
    b.block(Blocks.CAULDRON) {
      it.add(METALLUM.get(), 56)
        .add(ALKIMIA.get(), 8)
    }
    b.block(T7Blocks.CRUCIBLE.get()) {
      it.add(METALLUM.get(), 56)
        .add(ALKIMIA.get(), 8)
        .add(PRAECANTATIO.get(), 2)
    }
    b.block(T7Blocks.SEALING_JAR.get()) {
      it.add(VITREUS.get(), 4)
        .add(VACUOS.get(), 2)
        .add(PRAECANTATIO.get(), 2)
    }
  }
}

/**
 * When checking for Aspect contents, the Block aspects are prioritized over the Item aspects.
 * Therefore, to avoid ambiguities, BlockItem aspect registration is forbidden.
 */
fun Builder<AspectMap, Item>.item(item: Item, builderConsumer: Consumer<AspectMap.Builder>) {
  require(item !is BlockItem) { "Cannot register Aspects for BlockItems, you should register for their Blocks instead" }

  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(key(item), aspectBuilder.build(), false)
}

fun Builder<AspectMap, Item>.item(tag: TagKey<Item>, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(tag, aspectBuilder.build(), false)
}

fun Builder<AspectMap, Block>.block(block: Block, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(key(block), aspectBuilder.build(), false)
}

fun Builder<AspectMap, Block>.block(
  tag: TagKey<Block>,
  builderConsumer: Consumer<AspectMap.Builder>
) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(tag, aspectBuilder.build(), false)
}

fun Builder<AspectMap, EntityType<*>>.entity(entityType: EntityType<*>, builderConsumer: Consumer<AspectMap.Builder>) {
  val aspectBuilder = AspectMap.builder()
  builderConsumer.accept(aspectBuilder)
  add(key(entityType), aspectBuilder.build(), false)
}

private fun key(entityType: EntityType<*>): ResourceKey<EntityType<*>> {
  return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow()
}

private fun key(item: Item): ResourceKey<Item> {
  return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow()
}

private fun key(block: Block): ResourceKey<Block> {
  return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()
}
