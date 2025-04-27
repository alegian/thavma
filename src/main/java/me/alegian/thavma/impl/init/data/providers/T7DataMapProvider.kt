package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALIENIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ALKIMIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AQUA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.BESTIA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DESIDERIUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.EXANIMIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.FABRICO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HERBA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.HUMANUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.MOTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.ORDO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PERDITIO
import me.alegian.thavma.impl.init.registries.deferred.Aspects.SENSUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TENEBRAE
import me.alegian.thavma.impl.init.registries.deferred.Aspects.TERRA
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VACUOS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VICTUS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VINCULUM
import me.alegian.thavma.impl.init.registries.deferred.Aspects.VITREUS
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
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class T7DataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather(lookupProvider: HolderLookup.Provider) {
        val i = builder(T7DataMaps.AspectContent.ITEM)
        val b = builder(T7DataMaps.AspectContent.BLOCK)
        val e = builder(T7DataMaps.AspectContent.ENTITY)

        block(b, Tags.Blocks.STONES) { it.add(TERRA.get(), 5) }
        block(b, Blocks.DIRT) { it.add(TERRA.get(), 5) }

        block(b, Blocks.POLISHED_GRANITE) { it.add(TERRA.get(), 3) }
        block(b, Blocks.POLISHED_DIORITE) { it.add(TERRA.get(), 3) }
        block(b, Blocks.POLISHED_ANDESITE) { it.add(TERRA.get(), 3) }
        block(b, Blocks.COARSE_DIRT) { it.add(TERRA.get(), 3) }

        block(b, Blocks.GRASS_BLOCK) {
            it.add(TERRA.get(), 5).add(HERBA.get(), 2)
        }
        block(b, Blocks.PODZOL) {
            it.add(TERRA.get(), 5).add(HERBA.get(), 1)
        }
        block(b, Blocks.SHORT_GRASS) {
            it.add(HERBA.get(), 5).add(AER.get(), 1)
        }
        block(b, Blocks.TALL_GRASS) {
            it.add(HERBA.get(), 5).add(AER.get(), 1)
        }

        block(b, Tags.Blocks.COBBLESTONES) {
            it.add(TERRA.get(), 5).add(PERDITIO.get(), 1)
        }
        block(b, Tags.Blocks.SANDS) {
            it.add(TERRA.get(), 5).add(PERDITIO.get(), 5)
        }

        block(b, BlockTags.TERRACOTTA) {
            it.add(AQUA.get(), 15)
                .add(TERRA.get(), 15)
                .add(IGNIS.get(), 1)
                .add(SENSUS.get(), 1)
        }
        block(b, BlockTags.CONCRETE_POWDER) {
            it.add(TERRA.get(), 3).add(PERDITIO.get(), 2)
        }
        block(b, Tags.Blocks.CONCRETES) {
            it.add(TERRA.get(), 3)
                .add(PERDITIO.get(), 2)
                .add(AQUA.get(), 1)
                .add(ORDO.get(), 1)
        }

        block(b, BlockTags.PLANKS) { it.add(HERBA.get(), 3) }
        block(b, BlockTags.WOODEN_STAIRS) { it.add(HERBA.get(), 3) }
        block(b, BlockTags.WOODEN_SLABS) { it.add(HERBA.get(), 1) }
        block(b, BlockTags.LOGS) { it.add(HERBA.get(), 20) }

        block(b, BlockTags.WOOL) {
            it.add(BESTIA.get(), 11).add(SENSUS.get(), 3).add(FABRICO.get(), 3)
        }

        block(b, Tags.Blocks.GLASS_BLOCKS_CHEAP) { it.add(VITREUS.get(), 5) }
        block(b, Tags.Blocks.GLASS_PANES) { it.add(VITREUS.get(), 1) }

        block(b, Blocks.BEDROCK) {
            it.add(VACUOS.get(), 25)
                .add(PERDITIO.get(), 25)
                .add(TERRA.get(), 25)
                .add(TENEBRAE.get(), 25)
        }

        item(i, Items.DIAMOND) {
            it.add(VITREUS.get(), 15)
                .add(DESIDERIUM.get(), 15)
        }

        entity(e, EntityType.PIG) {
            it.add(DESIDERIUM.get(), 5)
                .add(TERRA.get(), 10)
                .add(BESTIA.get(), 10)
        }
        entity(e, EntityType.ZOMBIE) {
            it.add(TERRA.get(), 5)
                .add(HUMANUS.get(), 10)
                .add(EXANIMIS.get(), 20)
        }
        entity(e, EntityType.COW) {
            it.add(TERRA.get(), 15)
                .add(BESTIA.get(), 15)
        }
        entity(e, EntityType.SHEEP) {
            it.add(TERRA.get(), 10)
                .add(BESTIA.get(), 10)
        }
        entity(e, EntityType.SPIDER) {
            it.add(VINCULUM.get(), 10)
                .add(PERDITIO.get(), 10)
                .add(BESTIA.get(), 10)
        }
        entity(e, EntityType.SKELETON) {
            it.add(TERRA.get(), 5)
                .add(HUMANUS.get(), 5)
                .add(EXANIMIS.get(), 20)
        }
        entity(e, EntityType.CREEPER) {
            it.add(IGNIS.get(), 15)
                .add(HERBA.get(), 15)
        }
        entity(e, EntityType.VILLAGER) {
            it.add(HUMANUS.get(), 15)
        }
        entity(e, EntityType.ENDERMAN) {
            it.add(DESIDERIUM.get(), 5)
                .add(MOTUS.get(), 15)
                .add(ALIENIS.get(), 15)
        }
        entity(e, EntityType.SLIME) {
            it.add(ALKIMIA.get(), 5)
                .add(AQUA.get(), 10)
                .add(VICTUS.get(), 10)
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
        builder.add(key(item), aspectBuilder.build(), false)
    }

    private fun entity(builder: Builder<AspectMap, EntityType<*>>, entityType: EntityType<*>, builderConsumer: Consumer<AspectMap.Builder>) {
        val aspectBuilder = AspectMap.builder()
        builderConsumer.accept(aspectBuilder)
        builder.add(key(entityType), aspectBuilder.build(), false)
    }

    private fun block(builder: Builder<AspectMap, Block>, block: Block, builderConsumer: Consumer<AspectMap.Builder>) {
        val aspectBuilder = AspectMap.builder()
        builderConsumer.accept(aspectBuilder)
        builder.add(key(block), aspectBuilder.build(), false)
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

    private fun key(entityType: EntityType<*>): ResourceKey<EntityType<*>> {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow()
    }
}
