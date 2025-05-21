package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.model.WithTransformParentModel
import me.alegian.thavma.impl.common.block.InfusedBlock
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.AURA_NODE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ESSENTIA_CONTAINER
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LEAVES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_SAPLING
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_DEEPSLATES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.INFUSED_STONES
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ITEM_HATCH
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.MATRIX
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PEDESTAL
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
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
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SaplingBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class T7BlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) : BlockStateProvider(output, Thavma.MODID, exFileHelper) {
  override fun registerStatesAndModels() {
    this.simpleBlockWithItem(
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

    for (infusedStone in INFUSED_STONES.values) this.infusedBlockWithItem(infusedStone.get())

    for (infusedDeepslate in INFUSED_DEEPSLATES.values) this.infusedBlockWithItem(infusedDeepslate.get())

    this.logBlockWithItem(GREATWOOD_LOG.get())
    this.simpleBlockWithItem(GREATWOOD_PLANKS.get())
    this.leavesBlockWithItem(GREATWOOD_LEAVES.get())
    this.saplingBlockWithItem(GREATWOOD_SAPLING.get())

    this.logBlockWithItem(SILVERWOOD_LOG.get())
    this.simpleBlockWithItem(SILVERWOOD_PLANKS.get())
    this.leavesBlockWithItem(SILVERWOOD_LEAVES.get())
    this.saplingBlockWithItem(SILVERWOOD_SAPLING.get())

    this.simpleBlockWithItem(THAVMITE_BLOCK.get())
    this.simpleBlockWithItem(ORICHALCUM_BLOCK.get())

    this.simpleBlockWithItem(ELEMENTAL_STONE.get())

    this.blockEntity1x1x1(ARCANE_WORKBENCH.get())
    this.blockEntity1x1x1(MATRIX.get())
    this.blockEntity1x1x1(PEDESTAL.get())
    this.blockEntity1x2x1(PILLAR.get())

    this.simpleBlockWithItem(ESSENTIA_CONTAINER.get(), this.models().getExistingFile(rl("essentia_container")))
    this.horizontalBlockWithItem(TABLE.get(), this.models().getExistingFile(rl("table")))
    this.horizontalBlockWithItem(RESEARCH_TABLE.get(), this.models().getExistingFile(rl("research_table")))

    this.itemModels().getBuilder(AURA_NODE.id.path).parent(UncheckedModelFile("item/generated")).renderType(RenderType.translucent().name).texture("layer0", rl("item/aura_node"))
  }

  private fun simpleBlockWithItem(block: Block) {
    this.simpleBlockWithItem(block, this.cubeAll(block))
  }

  private fun logBlockWithItem(block: RotatedPillarBlock) {
    val blockRL = this.blockTexture(block)
    this.logBlock(block)
    this.itemModels().withExistingParent(this.name(block), blockRL)
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

  fun blockEntity1x1x1(block: Block) {
    this.simpleBlockWithItem(block, this.models().getBuilder(this.name(block)).texture("particle", this.blockTexture(block)))
    this.itemModels().withExistingParent(this.name(block), "item/chest").texture("particle", this.blockTexture(block))
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
