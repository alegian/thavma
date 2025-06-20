package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.providers.builders.CrucibleRecipeBuilder
import me.alegian.thavma.impl.init.data.providers.builders.InfusionRecipeBuilder
import me.alegian.thavma.impl.init.data.providers.builders.WorkbenchRecipeBuilder
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects.AETHER
import me.alegian.thavma.impl.init.registries.deferred.Aspects.IGNIS
import me.alegian.thavma.impl.init.registries.deferred.Aspects.LUX
import me.alegian.thavma.impl.init.registries.deferred.Aspects.POTENTIA
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.CRACKED_ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_CORE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ELEMENTAL_STONE_BRICKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.GREATWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ORICHALCUM_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_LOG
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SILVERWOOD_PLANKS
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.THAVMITE_BLOCK
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANE_LENS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES
import me.alegian.thavma.impl.init.registries.deferred.T7Items.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Items.IRON_HANDLE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ORICHALCUM_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_AXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_BOOTS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_CHESTPLATE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HAMMER
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HELMET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_HOE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_INGOT
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_LEGGINGS
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_NUGGET
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_PICKAXE
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SHOVEL
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_SWORD
import me.alegian.thavma.impl.init.registries.deferred.T7Items.wandOrThrow
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandHandleMaterials.IRON
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

open class T7RecipeProvider(pOutput: PackOutput, pRegistries: CompletableFuture<HolderLookup.Provider?>) : RecipeProvider(pOutput, pRegistries) {
  override fun buildRecipes(pRecipeOutput: RecipeOutput) {
    planksFromLog(pRecipeOutput, GREATWOOD_PLANKS, GREATWOOD_LOG)
    planksFromLog(pRecipeOutput, SILVERWOOD_PLANKS, SILVERWOOD_LOG)
    wandHandle(pRecipeOutput, IRON_HANDLE.get(), Items.IRON_INGOT, Items.IRON_NUGGET)
    wand(pRecipeOutput, wandOrThrow(IRON.get(), WOOD.get()), IRON_HANDLE.get(), Tags.Items.RODS_WOODEN)
    ingot(pRecipeOutput, THAVMITE_INGOT.get(), THAVMITE_NUGGET.get(), THAVMITE_BLOCK.get())
    ingot(pRecipeOutput, ORICHALCUM_INGOT.get(), ORICHALCUM_NUGGET.get(), ORICHALCUM_BLOCK.get())

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, THAVMITE_SWORD.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern(" a ")
      .pattern(" a ")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, THAVMITE_PICKAXE.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aaa")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, THAVMITE_HAMMER.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aaa")
      .pattern("aaa")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, THAVMITE_AXE.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aa ")
      .pattern("as ")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, THAVMITE_SHOVEL.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern(" a ")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, THAVMITE_HOE.get())
      .define('a', THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aa ")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GOGGLES.get())
      .define('o', ARCANE_LENS)
      .define('g', Items.GOLD_INGOT)
      .define('l', Items.LEATHER)
      .pattern("lgl")
      .pattern("l l")
      .pattern("ogo")
      .unlockedBy(getHasName(ARCANE_LENS.get()), has(ARCANE_LENS.get()))
      .save(pRecipeOutput)
    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, GOGGLES_CURIO)
      .requires(GOGGLES)
      .unlockedBy(getHasName(GOGGLES), has(GOGGLES))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, THAVMITE_HELMET)
      .define('a', THAVMITE_INGOT)
      .pattern("aaa")
      .pattern("a a")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, THAVMITE_CHESTPLATE)
      .define('a', THAVMITE_INGOT)
      .pattern("a a")
      .pattern("aaa")
      .pattern("aaa")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, THAVMITE_LEGGINGS)
      .define('a', THAVMITE_INGOT)
      .pattern("aaa")
      .pattern("a a")
      .pattern("a a")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, THAVMITE_BOOTS)
      .define('a', THAVMITE_INGOT)
      .pattern("a a")
      .pattern("a a")
      .unlockedBy(getHasName(THAVMITE_INGOT.get()), has(THAVMITE_INGOT.get()))
      .save(pRecipeOutput)

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ELEMENTAL_STONE_BRICKS)
      .define('s', ELEMENTAL_STONE)
      .pattern("ss ")
      .pattern("ss ")
      .unlockedBy(getHasName(ELEMENTAL_STONE), has(ELEMENTAL_STONE))
      .save(pRecipeOutput)

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ELEMENTAL_STONE), RecipeCategory.BUILDING_BLOCKS, CRACKED_ELEMENTAL_STONE, 0.1f, 200)
      .unlockedBy(getHasName(ELEMENTAL_STONE), has(ELEMENTAL_STONE))
      .save(pRecipeOutput)

    CrucibleRecipeBuilder(
      ItemStack(T7Blocks.ETERNAL_FLAME),
      AspectMap.builder()
        .add(IGNIS.get(), 8)
        .add(LUX.get(), 8)
        .add(POTENTIA.get(), 8)
        .build(),
      Ingredient.of(Items.GLOWSTONE_DUST)
    ).save(pRecipeOutput)

    InfusionRecipeBuilder(
      ItemStack(Items.NETHERITE_INGOT),
      Ingredient.of(THAVMITE_INGOT),
      listOf(Ingredient.of(Items.DIAMOND), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.IRON_INGOT)),
      AspectMap.builder()
        .add(AETHER.get(), 20)
        .add(IGNIS.get(), 30)
        .build(),
    ).save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(ELEMENTAL_STONE, 8)
      .requireAspects(AspectMap.ofPrimals(16))
      .define('s', Tags.Items.STONES)
      .define('o', T7Tags.SHARD)
      .pattern("sss")
      .pattern("sos")
      .pattern("sss")
      .unlockedBy(getHasName(ELEMENTAL_STONE), has(ELEMENTAL_STONE))
      .unlockedBy("has_shards", has(T7Tags.SHARD))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(ELEMENTAL_CORE, 1)
      .requireAspects(AspectMap.ofPrimals(8))
      .define('s', ELEMENTAL_STONE)
      .define('o', T7Tags.SHARD)
      .pattern(" o ")
      .pattern("oso")
      .pattern(" o ")
      .unlockedBy("has_stones", has(Tags.Items.STONES))
      .unlockedBy("has_shards", has(T7Tags.SHARD))
      .save(pRecipeOutput)
  }

  companion object {
    protected fun ingot(pRecipeOutput: RecipeOutput, ingot: ItemLike, nugget: ItemLike, block: ItemLike) {
      nineBlockStorageRecipes(
        pRecipeOutput, RecipeCategory.MISC, ingot, RecipeCategory.BUILDING_BLOCKS, block, itemLoc(block), null, itemLoc(ingot) + "_from_block", null
      )
      nineBlockStorageRecipes(
        pRecipeOutput, RecipeCategory.MISC, nugget, RecipeCategory.MISC, ingot, itemLoc(ingot) + "_from_nuggets", null, itemLoc(nugget), null
      )
    }

    protected fun wand(pRecipeOutput: RecipeOutput, wand: ItemLike, handle: ItemLike, core: ItemLike) {
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('h', handle)
        .define('c', core)
        .pattern("  c")
        .pattern(" c ")
        .pattern("h  ")
        .group("wand")
        .unlockedBy(getHasName(handle), has(handle))
        .save(pRecipeOutput)
    }

    // for wooden cores
    protected fun wand(pRecipeOutput: RecipeOutput, wand: ItemLike, handle: ItemLike, core: TagKey<Item?>) {
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('h', handle)
        .define('c', core)
        .pattern("  c")
        .pattern(" c ")
        .pattern("h  ")
        .group("wand")
        .unlockedBy(getHasName(handle), has(handle))
        .save(pRecipeOutput)
    }

    protected fun wandHandle(pRecipeOutput: RecipeOutput, cap: ItemLike, ingot: ItemLike, nugget: ItemLike) {
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, cap)
        .define('i', ingot)
        .define('n', nugget)
        .pattern(" n ")
        .pattern(" in")
        .pattern("i  ")
        .group("wand_handle")
        .unlockedBy(getHasName(ingot), has(ingot))
        .save(pRecipeOutput)
    }

    protected fun planksFromLog(pRecipeOutput: RecipeOutput, pPlanks: ItemLike, pLog: ItemLike) {
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, 4)
        .requires(pLog)
        .group("planks")
        .unlockedBy("has_logs", has(pLog))
        .save(pRecipeOutput)
    }

    protected fun itemLoc(itemLike: ItemLike): String {
      return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString()
    }
  }
}
