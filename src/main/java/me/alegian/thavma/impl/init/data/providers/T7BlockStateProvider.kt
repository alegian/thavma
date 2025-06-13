package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.model.WithTransformParentModel
import me.alegian.thavma.impl.common.block.InfusedBlock
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_LEVITATOR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRACKED_ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_BRICKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ETERNAL_FLAME
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SLAB
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_STAIRS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.HUNGRY_CHEST
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ITEM_HATCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SEALING_JAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.*
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class T7BlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) : BlockStateProvider(output, Thavma.MODID, exFileHelper) {
  override fun registerStatesAndModels() {
    simpleBlockWithItem(
      CRUCIBLE.get(), this.models().getBuilder(CRUCIBLE.id.path)
        .parent(UncheckedModelFile("block/cauldron"))
        .texture("particle", rl("block/crucible_side"))
        .texture("top", rl("block/crucible_top"))
        .texture("bottom", rl("block/crucible_bottom"))
        .texture("side", rl("block/crucible_side"))
        .texture("inside", rl("block/crucible_inner"))
        .customLoader(WithTransformParentModel::Builder)
        .transformParent(ResourceLocation.withDefaultNamespace("block/block"))
        .end()
    )

    simpleBlockWithItem(
      ITEM_HATCH.get(), models()
        .withExistingParent(name(ITEM_HATCH.get()), mcLoc("block/iron_trapdoor_top"))
        .renderType(RenderType.cutout().name)
        .customLoader(WithTransformParentModel::Builder)
        .transformParent(ResourceLocation.withDefaultNamespace("block/block"))
        .end()
    )

    for (infusedStone in INFUSED_STONES.values) infusedBlockWithItem(infusedStone.get())

    for (infusedDeepslate in INFUSED_DEEPSLATES.values) infusedBlockWithItem(infusedDeepslate.get())

    logBlockWithItem(GREATWOOD_LOG.get())
    simpleBlockWithItem(GREATWOOD_PLANKS.get())
    leavesBlockWithItem(GREATWOOD_LEAVES.get())
    saplingBlockWithItem(GREATWOOD_SAPLING.get())

    logBlockWithItem(SILVERWOOD_LOG.get())
    simpleBlockWithItem(SILVERWOOD_PLANKS.get())
    leavesBlockWithItem(SILVERWOOD_LEAVES.get())
    saplingBlockWithItem(SILVERWOOD_SAPLING.get())

    simpleBlockWithItem(THAVMITE_BLOCK.get())
    simpleBlockWithItem(ORICHALCUM_BLOCK.get())

    simpleBlockWithItem(ELEMENTAL_STONE.get())
    simpleBlockWithItem(ELEMENTAL_CORE.get())
    simpleBlockWithItem(CRACKED_ELEMENTAL_STONE.get())
    simpleBlockWithItem(ELEMENTAL_STONE_BRICKS.get())

    blockEntity1x1x1(ARCANE_WORKBENCH.get())
    blockEntity1x1x1(MATRIX.get())
    blockEntity1x1x1(PEDESTAL.get())
    blockEntity1x1x1(HUNGRY_CHEST.get(), blockTexture(GREATWOOD_PLANKS.get()))
    blockEntity1x2x1(PILLAR.get())

    simpleBlockWithItem(SEALING_JAR.get(), models().getExistingFile(key(SEALING_JAR.get())))
    horizontalBlockWithItem(TABLE.get(), models().getExistingFile(key(TABLE.get())))
    horizontalBlockWithItem(RESEARCH_TABLE.get(), models().getExistingFile(key(RESEARCH_TABLE.get())))

    simpleBlockWithItem(
      ARCANE_LEVITATOR.get(),
      models().cubeBottomTop(
        name(ARCANE_LEVITATOR.get()),
        blockTexture(ARCANE_LEVITATOR.get()).withSuffix("_side"),
        blockTexture(ARCANE_LEVITATOR.get()).withSuffix("_bottom"),
        blockTexture(ARCANE_LEVITATOR.get()).withSuffix("_top")
      )
    )

    itemModels().basicItem(AURA_NODE.get().asItem()).renderType(RenderType.translucent().name)
    itemModels().basicItem(ETERNAL_FLAME.get().asItem())

    simpleStairsWithItem(ELEMENTAL_STONE_STAIRS.get(), blockTexture(ELEMENTAL_STONE.get()))
    simpleSlabWithItem(ELEMENTAL_STONE_SLAB.get(), blockTexture(ELEMENTAL_STONE.get()))
    simpleStairsWithItem(GREATWOOD_STAIRS.get(), blockTexture(GREATWOOD_PLANKS.get()))
    simpleSlabWithItem(GREATWOOD_SLAB.get(), blockTexture(GREATWOOD_PLANKS.get()))
  }

  fun simpleStairsWithItem(block: StairBlock, parentLocation: ResourceLocation){
    stairsBlock(block, parentLocation)
    itemModels().withExistingParent(name(block), blockTexture(block))
  }

  fun simpleSlabWithItem(block: SlabBlock, parentLocation: ResourceLocation){
    slabBlock(block, parentLocation, parentLocation)
    itemModels().withExistingParent(name(block), blockTexture(block))
  }

  private fun simpleBlockWithItem(block: Block) {
    simpleBlockWithItem(block, this.cubeAll(block))
  }

  private fun logBlockWithItem(block: RotatedPillarBlock) {
    val blockRL = this.blockTexture(block)
    logBlock(block)
    itemModels().withExistingParent(this.name(block), blockRL)
  }

  private fun infusedBlockWithItem(infusedBlock: InfusedBlock) {
    val infusedOreBlockModel = this.models().withExistingParent(this.name(infusedBlock), rl("block/infused_stone"))
      .texture("layer0", this.blockTexture(infusedBlock.getBaseBlock()))
      .texture("layer1", rl("block/infused_stone"))
    this.simpleBlockWithItem(infusedBlock, infusedOreBlockModel)
  }

  fun leavesBlockWithItem(block: LeavesBlock) {
    val blockRL = this.blockTexture(block)
    val model = this.models().leaves(this.name(block), blockRL)
    this.simpleBlockWithItem(block, model)
  }

  fun saplingBlockWithItem(block: SaplingBlock) {
    val blockRL = this.blockTexture(block)
    val model = this.models().cross(this.name(block), blockRL).renderType(RenderType.cutout().name)
    this.simpleBlock(block, model)
    this.itemModels().withExistingParent(this.name(block), "item/generated").texture("layer0", blockRL)
  }

  fun blockEntity1x1x1(block: Block) = blockEntity1x1x1(block, blockTexture(block))

  fun blockEntity1x1x1(block: Block, particle: ResourceLocation) {
    this.simpleBlockWithItem(block, this.models().getBuilder(this.name(block)).texture("particle", particle))
    this.itemModels().withExistingParent(this.name(block), "item/chest").texture("particle", particle)
  }

  fun blockEntity1x2x1(block: Block) {
    this.simpleBlockWithItem(block, this.models().getBuilder(this.name(block)).texture("particle", this.blockTexture(block)))
    this.itemModels().withExistingParent(this.name(block), "item/template_bed").texture("particle", this.blockTexture(block))
  }

  fun horizontalBlockWithItem(block: Block, model: ModelFile) {
    horizontalBlock(block, model)
    simpleBlockItem(block, model)
  }

  private fun key(block: Block): ResourceLocation {
    return BuiltInRegistries.BLOCK.getKey(block)
  }

  private fun name(block: Block): String {
    return this.key(block).path
  }
}
