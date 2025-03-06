package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.FABRICO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ORDO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERDITIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.SENSUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VACUOS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class T7DataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather(lookupProvider: HolderLookup.Provider) {
        val i = this.builder(T7DataMaps.AspectContent.ITEM)
        val b = this.builder(T7DataMaps.AspectContent.BLOCK)

        this.block(b, Tags.Blocks.STONES) { it.add(TERRA.get(), 5) }
        this.block(b, Blocks.DIRT) { it.add(TERRA.get(), 5) }

        this.block(b, Blocks.POLISHED_GRANITE) { it.add(TERRA.get(), 3) }
        this.block(b, Blocks.POLISHED_DIORITE) { it.add(TERRA.get(), 3) }
        this.block(b, Blocks.POLISHED_ANDESITE) { it.add(TERRA.get(), 3) }
        this.block(b, Blocks.COARSE_DIRT) { it.add(TERRA.get(), 3) }

        this.block(b, Blocks.GRASS_BLOCK) {
            it.add(TERRA.get(), 5).add(HERBA.get(), 2)
        }
        this.block(b, Blocks.PODZOL) {
            it.add(TERRA.get(), 5).add(HERBA.get(), 1)
        }
        this.block(b, Blocks.SHORT_GRASS) {
            it.add(HERBA.get(), 5).add(AER.get(), 1)
        }
        this.block(b, Blocks.TALL_GRASS) {
            it.add(HERBA.get(), 5).add(AER.get(), 1)
        }

        this.block(b, Tags.Blocks.COBBLESTONES) {
            it.add(TERRA.get(), 5).add(PERDITIO.get(), 1)
        }
        this.block(b, Tags.Blocks.SANDS) {
            it.add(TERRA.get(), 5).add(PERDITIO.get(), 5)
        }

        this.block(b, BlockTags.TERRACOTTA) {
            it.add(AQUA.get(), 15)
                .add(TERRA.get(), 15)
                .add(IGNIS.get(), 1)
                .add(SENSUS.get(), 1)
        }
        this.block(b, BlockTags.CONCRETE_POWDER) {
            it.add(TERRA.get(), 3).add(PERDITIO.get(), 2)
        }
        this.block(b, Tags.Blocks.CONCRETES) {
            it.add(TERRA.get(), 3)
                .add(PERDITIO.get(), 2)
                .add(AQUA.get(), 1)
                .add(ORDO.get(), 1)
        }

        this.block(b, BlockTags.PLANKS) { it.add(HERBA.get(), 3) }
        this.block(b, BlockTags.WOODEN_STAIRS) { it.add(HERBA.get(), 3) }
        this.block(b, BlockTags.WOODEN_SLABS) { it.add(HERBA.get(), 1) }
        this.block(b, BlockTags.LOGS) { it.add(HERBA.get(), 20) }

        this.block(b, BlockTags.WOOL) {
            it.add(BESTIA.get(), 11).add(SENSUS.get(), 3).add(FABRICO.get(), 3)
        }

        this.block(b, Tags.Blocks.GLASS_BLOCKS_CHEAP) { it.add(VITREUS.get(), 5) }
        this.block(b, Tags.Blocks.GLASS_PANES) { it.add(VITREUS.get(), 1) }

        this.block(b, Blocks.BEDROCK) {
            it.add(VACUOS.get(), 25)
                .add(PERDITIO.get(), 25)
                .add(TERRA.get(), 25)
                .add(TENEBRAE.get(), 25)
        }

        this.item(i, Items.DIAMOND) {
            it.add(VITREUS.get(), 15).add(DESIDERIUM.get(), 15)
        }
    }

    /**
     * When checking for Aspect contents, the Block aspects are prioritized over the Item aspects.
     * Therefore, to avoid ambiguities, BlockItem aspect registration is forbidden.
     */
    private fun item(builder: Builder<AspectMap, Item>, item: Item, builderConsumer: Consumer<AspectMap.Builder>) {
        require(item !is BlockItem) { "Cannot register Aspects for BlockItems, you should register for their Blocks instead" }

        val aspectBuilder = AspectMap.builder()
        builderConsumer.accept(aspectBuilder)
        builder.add(this.key(item), aspectBuilder.build(), false)
    }

    private fun block(builder: Builder<AspectMap, Block>, block: Block, builderConsumer: Consumer<AspectMap.Builder>) {
        val aspectBuilder = AspectMap.builder()
        builderConsumer.accept(aspectBuilder)
        builder.add(this.key(block), aspectBuilder.build(), false)
    }

    private fun block(
        builder: Builder<AspectMap, Block>,
        tag: TagKey<Block>,
        builderConsumer: Consumer<AspectMap.Builder>
    ) {
        val aspectBuilder = AspectMap.builder()
        builderConsumer.accept(aspectBuilder)
        builder.add(tag, aspectBuilder.build(), false)
    }

    private fun key(item: Item): ResourceKey<Item> {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow()
    }

    private fun key(block: Block): ResourceKey<Block> {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()
    }
}
