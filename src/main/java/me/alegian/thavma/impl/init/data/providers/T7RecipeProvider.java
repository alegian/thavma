package me.alegian.thavma.impl.init.data.providers;

import me.alegian.thavma.impl.common.aspect.AspectMap;
import me.alegian.thavma.impl.init.data.providers.builders.CrucibleRecipeBuilder;
import me.alegian.thavma.impl.init.data.providers.builders.WorkbenchRecipeBuilder;
import me.alegian.thavma.impl.init.registries.deferred.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class T7RecipeProvider extends RecipeProvider {
  public T7RecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
    super(pOutput, pRegistries);
  }

  protected static void ingot(RecipeOutput pRecipeOutput, ItemLike ingot, ItemLike nugget, ItemLike block) {
    RecipeProvider.nineBlockStorageRecipes(
        pRecipeOutput, RecipeCategory.MISC, ingot, RecipeCategory.BUILDING_BLOCKS, block, T7RecipeProvider.itemLoc(block), null, T7RecipeProvider.itemLoc(ingot) + "_from_block", null
    );
    RecipeProvider.nineBlockStorageRecipes(
        pRecipeOutput, RecipeCategory.MISC, nugget, RecipeCategory.MISC, ingot, T7RecipeProvider.itemLoc(ingot) + "_from_nuggets", null, T7RecipeProvider.itemLoc(nugget), null
    );
  }

  protected static void wand(RecipeOutput pRecipeOutput, ItemLike wand, ItemLike handle, ItemLike core) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('h', handle)
        .define('c', core)
        .pattern("  c")
        .pattern(" c ")
        .pattern("h  ")
        .group("wand")
        .unlockedBy(RecipeProvider.getHasName(handle), RecipeProvider.has(handle))
        .save(pRecipeOutput);
  }

  // for wooden cores
  protected static void wand(RecipeOutput pRecipeOutput, ItemLike wand, ItemLike handle, TagKey<Item> core) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('h', handle)
        .define('c', core)
        .pattern("  c")
        .pattern(" c ")
        .pattern("h  ")
        .group("wand")
        .unlockedBy(RecipeProvider.getHasName(handle), RecipeProvider.has(handle))
        .save(pRecipeOutput);
  }

  protected static void wandHandle(RecipeOutput pRecipeOutput, ItemLike cap, ItemLike ingot, ItemLike nugget) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cap)
        .define('i', ingot)
        .define('n', nugget)
        .pattern(" n ")
        .pattern(" in")
        .pattern("i  ")
        .group("wand_handle")
        .unlockedBy(RecipeProvider.getHasName(ingot), RecipeProvider.has(ingot))
        .save(pRecipeOutput);
  }

  protected static void planksFromLog(RecipeOutput pRecipeOutput, ItemLike pPlanks, ItemLike pLog) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, 4)
        .requires(pLog)
        .group("planks")
        .unlockedBy("has_logs", RecipeProvider.has(pLog))
        .save(pRecipeOutput);
  }

  protected static void inCrucible(RecipeOutput output, ItemStack result, AspectMap aspects, Ingredient catalyst) {
    var catalystItem = catalyst.getItems()[0].getItem();
    new CrucibleRecipeBuilder(result, aspects, catalyst)
        .unlockedBy(RecipeProvider.getHasName(catalystItem), RecipeProvider.has(catalystItem))
        .save(output);
  }

  protected static String itemLoc(ItemLike itemLike) {
    return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString();
  }

  @Override
  protected void buildRecipes(RecipeOutput pRecipeOutput) {
    T7RecipeProvider.planksFromLog(pRecipeOutput, T7Blocks.INSTANCE.getGREATWOOD_PLANKS(), T7Blocks.INSTANCE.getGREATWOOD_LOG());
    T7RecipeProvider.planksFromLog(pRecipeOutput, T7Blocks.INSTANCE.getSILVERWOOD_PLANKS(), T7Blocks.INSTANCE.getSILVERWOOD_LOG());
    T7RecipeProvider.wandHandle(pRecipeOutput, T7Items.INSTANCE.getIRON_HANDLE().get(), Items.IRON_INGOT, Items.IRON_NUGGET);
    T7RecipeProvider.wand(pRecipeOutput, T7Items.INSTANCE.wandOrThrow(WandHandleMaterials.INSTANCE.getIRON().get(), WandCoreMaterials.INSTANCE.getWOOD().get()), T7Items.INSTANCE.getIRON_HANDLE().get(), Tags.Items.RODS_WOODEN);
    T7RecipeProvider.ingot(pRecipeOutput, T7Items.INSTANCE.getTHAVMITE_INGOT().get(), T7Items.INSTANCE.getTHAVMITE_NUGGET().get(), T7Blocks.INSTANCE.getTHAVMITE_BLOCK().get());
    T7RecipeProvider.ingot(pRecipeOutput, T7Items.INSTANCE.getORICHALCUM_INGOT().get(), T7Items.INSTANCE.getORICHALCUM_NUGGET().get(), T7Blocks.INSTANCE.getORICHALCUM_BLOCK().get());

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.INSTANCE.getTHAVMITE_SWORD().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern(" a ")
        .pattern(" a ")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getTHAVMITE_PICKAXE().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern("aaa")
        .pattern(" s ")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getTHAVMITE_HAMMER().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern("aaa")
        .pattern("aaa")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getTHAVMITE_AXE().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern("aa ")
        .pattern("as ")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getTHAVMITE_SHOVEL().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern(" a ")
        .pattern(" s ")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getTHAVMITE_HOE().get())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT().get())
        .define('s', Tags.Items.RODS_WOODEN)
        .pattern("aa ")
        .pattern(" s ")
        .pattern(" s ")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.INSTANCE.getGOGGLES().get())
        .define('o', T7Items.INSTANCE.getARCANE_LENS())
        .define('g', Items.GOLD_INGOT)
        .define('l', Items.LEATHER)
        .pattern("lgl")
        .pattern("l l")
        .pattern("ogo")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getARCANE_LENS().get()), RecipeProvider.has(T7Items.INSTANCE.getARCANE_LENS().get()))
        .save(pRecipeOutput);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, T7Items.INSTANCE.getGOGGLES_CURIO())
        .requires(T7Items.INSTANCE.getGOGGLES())
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getGOGGLES()), RecipeProvider.has(T7Items.INSTANCE.getGOGGLES()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.INSTANCE.getTHAVMITE_HELMET())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT())
        .pattern("aaa")
        .pattern("a a")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.INSTANCE.getTHAVMITE_CHESTPLATE())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT())
        .pattern("a a")
        .pattern("aaa")
        .pattern("aaa")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.INSTANCE.getTHAVMITE_LEGGINGS())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT())
        .pattern("aaa")
        .pattern("a a")
        .pattern("a a")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.INSTANCE.getTHAVMITE_BOOTS())
        .define('a', T7Items.INSTANCE.getTHAVMITE_INGOT())
        .pattern("a a")
        .pattern("a a")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getTHAVMITE_INGOT().get()), RecipeProvider.has(T7Items.INSTANCE.getTHAVMITE_INGOT().get()))
        .save(pRecipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Blocks.INSTANCE.getARCANE_WORKBENCH())
        .define('s', T7Blocks.INSTANCE.getELEMENTAL_STONE())
        .define('l', T7Blocks.INSTANCE.getGREATWOOD_LOG())
        .define('#', T7Items.INSTANCE.getSIGIL())
        .pattern("sls")
        .pattern("s#s")
        .pattern("sls")
        .unlockedBy(RecipeProvider.getHasName(T7Items.INSTANCE.getSIGIL().get()), RecipeProvider.has(T7Items.INSTANCE.getSIGIL().get()))
        .save(pRecipeOutput);

    T7RecipeProvider.inCrucible(pRecipeOutput,
        new ItemStack(Items.DIAMOND),
        AspectMap.Companion.builder()
            .add(Aspects.INSTANCE.getTERRA().get(), 6)
            .add(Aspects.INSTANCE.getPERDITIO().get(), 2)
            .build(),
        Ingredient.of(Items.DRAGON_EGG)
    );

    WorkbenchRecipeBuilder.Companion.shaped(Items.DIAMOND, 2)
        .requireAspects(AspectMap.Companion.builder()
            .add(Aspects.INSTANCE.getIGNIS().get(), 6)
            .add(Aspects.INSTANCE.getORDO().get(), 2)
            .build())
        .define('d', Blocks.COBBLED_DEEPSLATE)
        .define('g', Items.GOLD_INGOT)
        .pattern("gd ")
        .pattern(" g ")
        .unlockedBy(RecipeProvider.getHasName(Items.GOLD_INGOT), RecipeProvider.has(Items.GOLD_INGOT))
        .save(pRecipeOutput);
  }
}
