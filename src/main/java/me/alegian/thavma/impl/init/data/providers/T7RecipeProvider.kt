package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.data.providers.builders.CrucibleRecipeBuilder
import me.alegian.thavma.impl.init.data.providers.builders.InfusionRecipeBuilder
import me.alegian.thavma.impl.init.data.providers.builders.WorkbenchRecipeBuilder
import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.init.registries.deferred.T7Items.wandOrThrow
import me.alegian.thavma.impl.init.registries.deferred.WandCoreMaterials.WOOD
import me.alegian.thavma.impl.init.registries.deferred.WandPlatingMaterials.IRON
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

open class T7RecipeProvider(pOutput: PackOutput, pRegistries: CompletableFuture<HolderLookup.Provider?>) : RecipeProvider(pOutput, pRegistries) {
  override fun buildRecipes(pRecipeOutput: RecipeOutput) {
    planksFromLog(pRecipeOutput, T7Blocks.GREATWOOD_PLANKS, T7Blocks.GREATWOOD_LOG)
    planksFromLog(pRecipeOutput, T7Blocks.SILVERWOOD_PLANKS, T7Blocks.SILVERWOOD_LOG)
    wand(pRecipeOutput, wandOrThrow(IRON.get(), WOOD.get()), T7Items.IRON_PLATING.get(), Tags.Items.RODS_WOODEN)
    ingot(pRecipeOutput, T7Items.THAVMITE_INGOT.get(), T7Items.THAVMITE_NUGGET.get(), T7Blocks.THAVMITE_BLOCK.get())
    ingot(pRecipeOutput, T7Items.ORICHALCUM_INGOT.get(), T7Items.ORICHALCUM_NUGGET.get(), T7Blocks.ORICHALCUM_BLOCK.get())
    slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, T7Blocks.GREATWOOD_SLAB.get(), T7Blocks.GREATWOOD_PLANKS.get())
    slab(pRecipeOutput, RecipeCategory.BUILDING_BLOCKS, T7Blocks.ELEMENTAL_STONE_SLAB.get(), T7Blocks.ELEMENTAL_STONE.get())
    stairs(pRecipeOutput, T7Blocks.GREATWOOD_STAIRS.get(), T7Blocks.GREATWOOD_PLANKS.get())
    stairs(pRecipeOutput, T7Blocks.ELEMENTAL_STONE_STAIRS.get(), T7Blocks.ELEMENTAL_STONE.get())

    wandPlatingCrafting(pRecipeOutput, T7Items.IRON_PLATING.get(), Items.IRON_INGOT, Items.IRON_NUGGET)
    wandPlatingWorkbench(pRecipeOutput, T7Items.GOLD_PLATING.get(), Items.GOLD_INGOT, Items.GOLD_NUGGET, AspectMap.ofPrimals(8))
    wandPlatingWorkbench(pRecipeOutput, T7Items.ORICHALCUM_PLATING.get(), T7Items.ORICHALCUM_INGOT, T7Items.ORICHALCUM_NUGGET, AspectMap.ofPrimals(4))
    wandPlatingInfusion(pRecipeOutput, T7Items.THAVMITE_PLATING.get(), T7Items.THAVMITE_INGOT, T7Items.THAVMITE_NUGGET, AspectMap.builder().add(Aspects.PRAECANTATIO, 16).build())

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, T7Items.THAVMITE_SWORD.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern(" a ")
      .pattern(" a ")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.THAVMITE_PICKAXE.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aaa")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.THAVMITE_HAMMER.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aaa")
      .pattern("aaa")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.THAVMITE_AXE.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aa ")
      .pattern("as ")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.THAVMITE_SHOVEL.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern(" a ")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.THAVMITE_HOE.get())
      .define('a', T7Items.THAVMITE_INGOT.get())
      .define('s', Tags.Items.RODS_WOODEN)
      .pattern("aa ")
      .pattern(" s ")
      .pattern(" s ")
      .unlockedBy(getHasName(T7Items.THAVMITE_INGOT.get()), has(T7Items.THAVMITE_INGOT.get()))
      .save(pRecipeOutput)
    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, T7Items.GOGGLES_CURIO)
      .requires(T7Items.GOGGLES)
      .unlockedBy(getHasName(T7Items.GOGGLES), has(T7Items.GOGGLES))
      .save(pRecipeOutput)

    simpleArmor(
      pRecipeOutput,
      T7Items.THAVMITE_INGOT,
      T7Items.THAVMITE_HELMET,
      T7Items.THAVMITE_CHESTPLATE,
      T7Items.THAVMITE_LEGGINGS,
      T7Items.THAVMITE_BOOTS
    )
    simpleArmor(
      pRecipeOutput,
      T7Items.FABRIC,
      null,
      T7Items.APPRENTICE_CHESTPLATE,
      T7Items.APPRENTICE_LEGGINGS,
      T7Items.APPRENTICE_BOOTS,
    )

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, T7Blocks.ELEMENTAL_STONE_BRICKS)
      .define('s', T7Blocks.ELEMENTAL_STONE)
      .pattern("ss")
      .pattern("ss")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_STONE), has(T7Blocks.ELEMENTAL_STONE))
      .save(pRecipeOutput)

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Blocks.PEDESTAL)
      .define('s', T7Blocks.ELEMENTAL_STONE)
      .define('c', T7Blocks.ELEMENTAL_CORE)
      .define('-', T7Blocks.ELEMENTAL_STONE_SLAB)
      .pattern(" c ")
      .pattern(" s ")
      .pattern("-s-")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_CORE), has(T7Blocks.ELEMENTAL_CORE))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Blocks.MATRIX)
      .define('c', T7Blocks.ELEMENTAL_CORE)
      .pattern("ccc")
      .pattern("c c")
      .pattern("ccc")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_CORE), has(T7Blocks.ELEMENTAL_CORE))
      .save(pRecipeOutput)

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, T7Blocks.TABLE)
      .define('p', T7Blocks.GREATWOOD_PLANKS)
      .define('s', T7Blocks.GREATWOOD_SLAB)
      .pattern("sss")
      .pattern("p p")
      .pattern("p p")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_STONE), has(T7Blocks.ELEMENTAL_STONE))
      .save(pRecipeOutput)

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Items.BASIC_AMULET)
      .define('o', T7Items.ORICHALCUM_INGOT)
      .define('s', Tags.Items.STRINGS)
      .pattern(" s ")
      .pattern("s s")
      .pattern(" o ")
      .unlockedBy(getHasName(T7Items.ORICHALCUM_INGOT), has(T7Items.ORICHALCUM_INGOT))
      .unlockedBy("has_strings", has(Tags.Items.STRINGS))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Items.BASIC_BELT)
      .define('o', T7Items.ORICHALCUM_INGOT)
      .define('l', Tags.Items.LEATHERS)
      .pattern("lll")
      .pattern("l l")
      .pattern(" o ")
      .unlockedBy(getHasName(T7Items.ORICHALCUM_INGOT), has(T7Items.ORICHALCUM_INGOT))
      .unlockedBy("has_leathers", has(Tags.Items.LEATHERS))
      .save(pRecipeOutput)
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, T7Items.BASIC_RING)
      .define('o', T7Items.ORICHALCUM_INGOT)
      .pattern(" o ")
      .pattern("o o")
      .pattern(" o ")
      .unlockedBy(getHasName(T7Items.ORICHALCUM_INGOT), has(T7Items.ORICHALCUM_INGOT))
      .save(pRecipeOutput)

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, T7Items.ARCANE_LENS)
      .define('g', Tags.Items.INGOTS_GOLD)
      .define('s', T7Tags.Items.SHARDS)
      .define('p', Tags.Items.GLASS_PANES_COLORLESS)
      .pattern("sgs")
      .pattern("gpg")
      .pattern("sgs")
      .unlockedBy("has_golds", has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)
    WorkbenchRecipeBuilder.shaped(T7Items.GOGGLES, 1)
      .requireAspects(AspectMap.ofPrimals(4))
      .define('o', T7Items.ARCANE_LENS)
      .define('g', T7Items.ORICHALCUM_INGOT)
      .define('l', Items.LEATHER)
      .pattern("lgl")
      .pattern("l l")
      .pattern("ogo")
      .unlockedBy(getHasName(T7Items.ARCANE_LENS.get()), has(T7Items.ARCANE_LENS.get()))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Blocks.SEALING_JAR, 1)
      .requireAspects(AspectMap.builder().add(Aspects.AQUA.get(), 4).add(Aspects.AETHER.get(), 4).build())
      .define('s', T7Blocks.GREATWOOD_SLAB)
      .define('p', Tags.Items.GLASS_PANES)
      .pattern("psp")
      .pattern("p p")
      .pattern("ppp")
      .unlockedBy(getHasName(T7Blocks.GREATWOOD_SLAB.get()), has(T7Blocks.GREATWOOD_SLAB.get()))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Items.GREATWOOD_CORE, 1)
      .requireAspects(AspectMap.ofPrimals(8))
      .define('g', T7Blocks.GREATWOOD_LOG)
      .define('o', T7Items.SHARDS[Aspects.AETHER]!!)
      .pattern(" og")
      .pattern("ogo")
      .pattern("go ")
      .unlockedBy(getHasName(T7Blocks.GREATWOOD_LOG.get()), has(T7Blocks.GREATWOOD_LOG.get()))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)
    InfusionRecipeBuilder(
      T7Items.SILVERWOOD_CORE,
      Ingredient.of(T7Blocks.SILVERWOOD_LOG),
      listOf(Ingredient.of(T7Items.SHARDS[Aspects.AETHER]!!), Ingredient.of(T7Items.SHARDS[Aspects.AETHER]!!), Ingredient.of(T7Items.SHARDS[Aspects.AETHER]!!), Ingredient.of(T7Items.SHARDS[Aspects.AETHER]!!)),
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO, 16)
        .build(),
    ).save(pRecipeOutput)

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(T7Blocks.ELEMENTAL_STONE), RecipeCategory.BUILDING_BLOCKS, T7Blocks.CRACKED_ELEMENTAL_STONE, 0.1f, 200)
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_STONE), has(T7Blocks.ELEMENTAL_STONE))
      .save(pRecipeOutput)

    for (a in Aspects.PRIMAL_ASPECTS) {
      SimpleCookingRecipeBuilder.blasting(Ingredient.of(T7Blocks.INFUSED_STONES[a], T7Blocks.INFUSED_DEEPSLATES[a]), RecipeCategory.MISC, T7Items.SHARDS[a]!!, 1f, 100)
        .unlockedBy("has_infused_stones", has(T7Tags.Items.INFUSED_STONES))
        .save(pRecipeOutput, T7Items.SHARDS[a]!!.id.withSuffix("_blasting"))
      SimpleCookingRecipeBuilder.smelting(Ingredient.of(T7Blocks.INFUSED_STONES[a], T7Blocks.INFUSED_DEEPSLATES[a]), RecipeCategory.MISC, T7Items.SHARDS[a]!!, 1f, 200)
        .unlockedBy("has_infused_stones", has(T7Tags.Items.INFUSED_STONES))
        .save(pRecipeOutput, T7Items.SHARDS[a]!!.id.withSuffix("_smelting"))
    }

    CrucibleRecipeBuilder(
      T7Items.THAVMITE_INGOT.get().defaultInstance,
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO.get(), 4)
        .build(),
      Ingredient.of(Items.IRON_INGOT)
    ).save(pRecipeOutput)

    CrucibleRecipeBuilder(
      T7Items.ORICHALCUM_INGOT.get().defaultInstance,
      AspectMap.builder()
        .add(Aspects.INSTRUMENTUM.get(), 4)
        .build(),
      Ingredient.of(Items.COPPER_INGOT)
    ).save(pRecipeOutput)

    CrucibleRecipeBuilder(
      ItemStack(T7Blocks.ETERNAL_FLAME),
      AspectMap.builder()
        .add(Aspects.IGNIS.get(), 8)
        .add(Aspects.LUX.get(), 8)
        .add(Aspects.POTENTIA.get(), 8)
        .build(),
      Ingredient.of(Items.GLOWSTONE_DUST)
    ).save(pRecipeOutput)

    InfusionRecipeBuilder(
      T7Items.THAVMITE_VANGUARD_HELMET,
      Ingredient.of(T7Items.THAVMITE_HELMET),
      listOf(Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.DIAMOND), Ingredient.of(Items.LEATHER)),
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO, 16)
        .add(Aspects.PRAEMUNIO, 32)
        .build(),
    ).save(pRecipeOutput)
    InfusionRecipeBuilder(
      T7Items.THAVMITE_VANGUARD_CHESTPLATE,
      Ingredient.of(T7Items.THAVMITE_CHESTPLATE),
      listOf(Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.DIAMOND), Ingredient.of(Items.LEATHER)),
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO, 16)
        .add(Aspects.PRAEMUNIO, 32)
        .build(),
    ).save(pRecipeOutput)
    InfusionRecipeBuilder(
      T7Items.THAVMITE_VANGUARD_LEGGINGS,
      Ingredient.of(T7Items.THAVMITE_LEGGINGS),
      listOf(Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.DIAMOND), Ingredient.of(Items.LEATHER)),
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO, 16)
        .add(Aspects.PRAEMUNIO, 32)
        .build(),
    ).save(pRecipeOutput)
    InfusionRecipeBuilder(
      T7Items.THAVMITE_VANGUARD_BOOTS,
      Ingredient.of(T7Items.THAVMITE_BOOTS),
      listOf(Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.DIAMOND), Ingredient.of(Items.LEATHER)),
      AspectMap.builder()
        .add(Aspects.PRAECANTATIO, 16)
        .add(Aspects.PRAEMUNIO, 32)
        .build(),
    ).save(pRecipeOutput)

    InfusionRecipeBuilder(
      T7Items.DAWN_CHARM,
      Ingredient.of(T7Items.BASIC_AMULET),
      listOf(Ingredient.of(T7Blocks.ETERNAL_FLAME), Ingredient.of(Blocks.GLOWSTONE), Ingredient.of(Items.GLOW_INK_SAC)),
      AspectMap.builder()
        .add(Aspects.LUX, 40)
        .build(),
    ).save(pRecipeOutput)

    InfusionRecipeBuilder(
      T7Items.AXE_OF_THE_FOREST,
      Ingredient.of(T7Items.THAVMITE_AXE),
      listOf(Ingredient.of(T7Items.THAVMITE_PLATING), Ingredient.of(T7Items.THAVMITE_PLATING), Ingredient.of(Items.BLAZE_ROD), Ingredient.of(Items.DIAMOND)),
      AspectMap.builder()
        .add(Aspects.HERBA, 32)
        .add(Aspects.INSTRUMENTUM, 32)
        .build(),
    ).save(pRecipeOutput)

    InfusionRecipeBuilder(
      T7Items.FOCUS_ENDERPEARL,
      Ingredient.of(Items.ENDER_PEARL),
      listOf(Ingredient.of(T7Items.THAVMITE_PLATING), Ingredient.of(T7Items.THAVMITE_PLATING), Ingredient.of(Blocks.DISPENSER)),
      AspectMap.builder()
        .add(Aspects.MOTUS, 32)
        .add(Aspects.ALIENIS, 16)
        .add(Aspects.INSTRUMENTUM, 16)
        .build(),
    ).save(pRecipeOutput)
    InfusionRecipeBuilder(
      T7Items.FOCUS_EXCHANGE,
      Ingredient.of(Items.QUARTZ_BLOCK),
      listOf(Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.LAPIS_LAZULI)),
      AspectMap.builder()
        .add(Aspects.PERMUTATIO, 40)
        .add(Aspects.INSTRUMENTUM, 16)
        .build(),
    ).save(pRecipeOutput)
    WorkbenchRecipeBuilder.shaped(T7Items.FOCUS_LIGHT, 1)
      .requireAspects(AspectMap.builder().add(Aspects.IGNIS, 12).add(Aspects.AETHER, 12).build())
      .define('e', T7Blocks.ETERNAL_FLAME)
      .define('o', T7Items.ORICHALCUM_PLATING)
      .define('b', Tags.Items.STORAGE_BLOCKS_COAL)
      .pattern("oeo")
      .pattern("ebe")
      .pattern("oeo")
      .unlockedBy(getHasName(T7Blocks.ETERNAL_FLAME), has(T7Blocks.ETERNAL_FLAME))
      .unlockedBy(getHasName(T7Items.ORICHALCUM_PLATING), has(T7Items.ORICHALCUM_PLATING))
      .unlockedBy("has_coal_storage_blocks", has(Tags.Items.STORAGE_BLOCKS_COAL))
      .save(pRecipeOutput)
    WorkbenchRecipeBuilder.shaped(T7Items.FOCUS_ENDERCHEST, 1)
      .requireAspects(AspectMap.builder().add(Aspects.TERRA, 12).add(Aspects.AER, 12).build())
      .define('e', Blocks.ENDER_CHEST)
      .define('o', Tags.Items.OBSIDIANS)
      .define('b', Items.BUNDLE)
      .pattern("ooo")
      .pattern("oeo")
      .pattern("obo")
      .unlockedBy(getHasName(Blocks.ENDER_CHEST), has(Blocks.ENDER_CHEST))
      .unlockedBy(getHasName(Items.BUNDLE), has(Items.BUNDLE))
      .unlockedBy("has_obsidians", has(Tags.Items.OBSIDIANS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Blocks.ITEM_HATCH, 1)
      .requireAspects(AspectMap.builder().add(Aspects.IGNIS, 12).build())
      .define('i', T7Items.IRON_PLATING)
      .define('t', ItemTags.TRAPDOORS)
      .pattern("iii")
      .pattern("iti")
      .pattern("iii")
      .unlockedBy(getHasName(T7Items.IRON_PLATING), has(T7Items.IRON_PLATING))
      .unlockedBy("has_trapdoors", has(ItemTags.TRAPDOORS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Items.RUNE, 4)
      .requireAspects(AspectMap.ofPrimals(8))
      .define('d', Blocks.COBBLED_DEEPSLATE)
      .define('s', T7Tags.Items.SHARDS)
      .pattern("ddd")
      .pattern("dsd")
      .pattern("ddd")
      .unlockedBy(getHasName(Blocks.COBBLED_DEEPSLATE), has(Blocks.COBBLED_DEEPSLATE))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Blocks.ELEMENTAL_STONE, 8)
      .requireAspects(AspectMap.ofPrimals(16))
      .define('s', Tags.Items.STONES)
      .define('o', T7Tags.Items.SHARDS)
      .pattern("sss")
      .pattern("sos")
      .pattern("sss")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_STONE), has(T7Blocks.ELEMENTAL_STONE))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Blocks.ELEMENTAL_CORE, 1)
      .requireAspects(AspectMap.ofPrimals(8))
      .define('s', T7Blocks.ELEMENTAL_STONE)
      .define('o', T7Tags.Items.SHARDS)
      .pattern(" o ")
      .pattern("oso")
      .pattern(" o ")
      .unlockedBy("has_stones", has(Tags.Items.STONES))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Items.FABRIC, 4)
      .requireAspects(AspectMap.ofPrimals(8))
      .define('w', ItemTags.WOOL)
      .define('s', T7Tags.Items.SHARDS)
      .pattern(" w ")
      .pattern("wsw")
      .pattern(" w ")
      .unlockedBy("has_wools", has(ItemTags.WOOL))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)

    WorkbenchRecipeBuilder.shaped(T7Blocks.ARCANE_LEVITATOR, 1)
      .requireAspects(
        AspectMap.builder()
          .add(Aspects.AER, 12)
          .add(Aspects.TERRA, 4)
          .add(Aspects.AETHER, 4)
          .build()
      )
      .define('s', T7Blocks.ELEMENTAL_STONE)
      .define('c', T7Blocks.ELEMENTAL_CORE)
      .define('a', T7Items.SHARDS[Aspects.AER]!!)
      .pattern("sas")
      .pattern("scs")
      .pattern("sas")
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_STONE), has(T7Blocks.ELEMENTAL_STONE))
      .unlockedBy(getHasName(T7Blocks.ELEMENTAL_CORE), has(T7Blocks.ELEMENTAL_CORE))
      .unlockedBy("has_shards", has(T7Tags.Items.SHARDS))
      .save(pRecipeOutput)

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, T7Blocks.SEALING_JAR)
      .requires(T7Blocks.SEALING_JAR)
      .unlockedBy(getHasName(T7Blocks.SEALING_JAR), has(T7Blocks.SEALING_JAR))
      .save(pRecipeOutput, itemLoc(T7Blocks.SEALING_JAR) + "_clear")
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

    protected fun wand(pRecipeOutput: RecipeOutput, wand: ItemLike, plating: ItemLike, core: ItemLike) {
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('p', plating)
        .define('c', core)
        .pattern("  c")
        .pattern(" c ")
        .pattern("p  ")
        .group("wand")
        .unlockedBy(getHasName(plating), has(plating))
        .save(pRecipeOutput)
    }

    // for wooden cores
    protected fun wand(pRecipeOutput: RecipeOutput, wand: ItemLike, plating: ItemLike, core: TagKey<Item?>) {
      ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, wand)
        .define('p', plating)
        .define('c', core)
        .pattern(" pp")
        .pattern(" cp")
        .pattern("c  ")
        .group("wand")
        .unlockedBy(getHasName(plating), has(plating))
        .save(pRecipeOutput)
    }

    protected fun wandPlatingCrafting(pRecipeOutput: RecipeOutput, plating: ItemLike, ingot: ItemLike, nugget: ItemLike) {
      ShapedRecipeBuilder.shaped(RecipeCategory.MISC, plating)
        .define('i', ingot)
        .define('n', nugget)
        .pattern(" ni")
        .pattern(" in")
        .group("wand_plating")
        .unlockedBy(getHasName(ingot), has(ingot))
        .save(pRecipeOutput)
    }

    protected fun wandPlatingWorkbench(pRecipeOutput: RecipeOutput, plating: ItemLike, ingot: ItemLike, nugget: ItemLike, aspects: AspectMap) {
      WorkbenchRecipeBuilder.shaped(plating, 1)
        .requireAspects(aspects)
        .define('i', ingot)
        .define('n', nugget)
        .pattern(" ni")
        .pattern(" in")
        .unlockedBy(getHasName(ingot), has(ingot))
        .save(pRecipeOutput)
    }

    protected fun wandPlatingInfusion(pRecipeOutput: RecipeOutput, plating: ItemLike, ingot: ItemLike, nugget: ItemLike, aspects: AspectMap) {
      InfusionRecipeBuilder(
        plating,
        Ingredient.of(ingot),
        listOf(Ingredient.of(ingot), Ingredient.of(nugget), Ingredient.of(nugget)),
        aspects
      ).save(pRecipeOutput)
    }

    protected fun planksFromLog(pRecipeOutput: RecipeOutput, pPlanks: ItemLike, pLog: ItemLike) {
      ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, 4)
        .requires(pLog)
        .group("planks")
        .unlockedBy("has_logs", has(pLog))
        .save(pRecipeOutput)
    }

    protected fun stairs(recipeOutput: RecipeOutput, stairs: ItemLike, material: ItemLike) =
      stairBuilder(stairs, Ingredient.of(material))
        .unlockedBy(getHasName(material), has(material))
        .save(recipeOutput)

    protected fun simpleArmor(recipeOutput: RecipeOutput, material: ItemLike, helmet: ItemLike?, chestplate: ItemLike?, leggings: ItemLike?, boots: ItemLike?) {
      if (helmet != null)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
          .define('a', material)
          .pattern("aaa")
          .pattern("a a")
          .unlockedBy(getHasName(material), has(material))
          .save(recipeOutput)
      if (chestplate != null)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
          .define('a', material)
          .pattern("a a")
          .pattern("aaa")
          .pattern("aaa")
          .unlockedBy(getHasName(material), has(material))
          .save(recipeOutput)
      if (leggings != null)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings)
          .define('a', material)
          .pattern("aaa")
          .pattern("a a")
          .pattern("a a")
          .unlockedBy(getHasName(material), has(material))
          .save(recipeOutput)
      if (boots != null)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
          .define('a', material)
          .pattern("a a")
          .pattern("a a")
          .unlockedBy(getHasName(material), has(material))
          .save(recipeOutput)
    }

    protected fun itemLoc(itemLike: ItemLike): String {
      return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString()
    }
  }
}
