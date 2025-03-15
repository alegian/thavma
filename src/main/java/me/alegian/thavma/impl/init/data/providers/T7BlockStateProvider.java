package me.alegian.thavma.impl.init.data.providers;

import me.alegian.thavma.impl.Thavma;
import me.alegian.thavma.impl.client.model.WithTransformParentModel;
import me.alegian.thavma.impl.common.block.InfusedBlock;
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static me.alegian.thavma.impl.ThavmaKt.rl;

public class T7BlockStateProvider extends BlockStateProvider {
  public T7BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, Thavma.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    this.simpleBlockWithItem(T7Blocks.INSTANCE.getCRUCIBLE().get(), this.models().getBuilder(T7Blocks.INSTANCE.getCRUCIBLE().getId().getPath())
        .parent(new ModelFile.UncheckedModelFile("block/cauldron"))
        .texture("particle", rl("block/crucible_side"))
        .texture("top", rl("block/crucible_top"))
        .texture("bottom", rl("block/crucible_bottom"))
        .texture("side", rl("block/crucible_side"))
        .texture("inside", rl("block/crucible_inner"))
        .customLoader(WithTransformParentModel.Builder::new)
        .transformParent(ResourceLocation.withDefaultNamespace("block/block"))
        .end()
    );

    for (var infusedStone : T7Blocks.INSTANCE.getINFUSED_STONES().values())
      this.infusedBlockWithItem(infusedStone.get());

    for (var infusedDeepslate : T7Blocks.INSTANCE.getINFUSED_DEEPSLATES().values())
      this.infusedBlockWithItem(infusedDeepslate.get());

    this.logBlockWithItem(T7Blocks.INSTANCE.getGREATWOOD_LOG().get());
    this.simpleBlockWithItem(T7Blocks.INSTANCE.getGREATWOOD_PLANKS().get());
    this.leavesBlockWithItem(T7Blocks.INSTANCE.getGREATWOOD_LEAVES().get());
    this.saplingBlockWithItem(T7Blocks.INSTANCE.getGREATWOOD_SAPLING().get());

    this.logBlockWithItem(T7Blocks.INSTANCE.getSILVERWOOD_LOG().get());
    this.simpleBlockWithItem(T7Blocks.INSTANCE.getSILVERWOOD_PLANKS().get());
    this.leavesBlockWithItem(T7Blocks.INSTANCE.getSILVERWOOD_LEAVES().get());
    this.saplingBlockWithItem(T7Blocks.INSTANCE.getSILVERWOOD_SAPLING().get());

    this.simpleBlockWithItem(T7Blocks.INSTANCE.getARCANUM_BLOCK().get());
    this.simpleBlockWithItem(T7Blocks.INSTANCE.getORICHALCUM_BLOCK().get());

    this.simpleBlockWithItem(T7Blocks.INSTANCE.getELEMENTAL_STONE().get());

    this.blockEntity1x1x1(T7Blocks.INSTANCE.getARCANE_WORKBENCH().get());
    this.blockEntity1x1x1(T7Blocks.INSTANCE.getMATRIX().get());
    this.blockEntity1x1x1(T7Blocks.INSTANCE.getPEDESTAL().get());
    this.blockEntity1x2x1(T7Blocks.INSTANCE.getPILLAR().get());

    this.simpleBlockWithItem(T7Blocks.INSTANCE.getESSENTIA_CONTAINER().get(), this.models().getExistingFile(rl("essentia_container")));
    this.simpleBlockWithItem(T7Blocks.INSTANCE.getRESEARCH_TABLE().get(), this.models().getExistingFile(rl("research_table")));

    this.itemModels().getBuilder(T7Blocks.INSTANCE.getAURA_NODE().getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated")).renderType(RenderType.translucent().name).texture("layer0", rl("item/aura_node"));
  }

  private void simpleBlockWithItem(Block block) {
    this.simpleBlockWithItem(block, this.cubeAll(block));
  }

  private void logBlockWithItem(RotatedPillarBlock block) {
    ResourceLocation blockRL = this.blockTexture(block);
    this.logBlock(block);
    this.itemModels().withExistingParent(this.name(block), blockRL);
  }

  private void infusedBlockWithItem(InfusedBlock infusedBlock) {
    var infusedOreBlockModel = this.models().withExistingParent(this.name(infusedBlock), rl("block/infused_stone"))
        .texture("layer0", this.blockTexture(infusedBlock.getBaseBlock()))
        .texture("layer1", rl("block/infused_stone"));
    this.simpleBlockWithItem(infusedBlock, infusedOreBlockModel);
  }

  public void leavesBlockWithItem(LeavesBlock block) {
    var blockRL = this.blockTexture(block);
    var model = this.models().leaves(this.name(block), blockRL);
    this.simpleBlockWithItem(block, model);
  }

  public void saplingBlockWithItem(SaplingBlock block) {
    var blockRL = this.blockTexture(block);
    var model = this.models().cross(this.name(block), blockRL).renderType(RenderType.cutout().name);
    this.simpleBlock(block, model);
    this.itemModels().withExistingParent(this.name(block), "item/generated").texture("layer0", blockRL);
  }

  public void blockEntity1x1x1(Block block) {
    this.simpleBlockWithItem(block, this.models().getBuilder(this.name(block)).texture("particle", this.blockTexture(block)));
    this.itemModels().withExistingParent(this.name(block), "item/chest").texture("particle", this.blockTexture(block));
  }

  public void blockEntity1x2x1(Block block) {
    this.simpleBlockWithItem(block, this.models().getBuilder(this.name(block)).texture("particle", this.blockTexture(block)));
    this.itemModels().withExistingParent(this.name(block), "item/template_bed").texture("particle", this.blockTexture(block));
  }

  private ResourceLocation key(Block block) {
    return BuiltInRegistries.BLOCK.getKey(block);
  }

  private String name(Block block) {
    return this.key(block).getPath();
  }
}
