package me.alegian.thavma.impl.common.recipe

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.ShapedRecipePattern

class WorkbenchRecipeSerializer : RecipeSerializer<WorkbenchRecipe> {
  override fun codec() = CODEC
  override fun streamCodec() = STREAM_CODEC
}

private val CODEC = RecordCodecBuilder.mapCodec { builder ->
  builder.group(
    ShapedRecipePattern.MAP_CODEC.forGetter(WorkbenchRecipe::pattern),
    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WorkbenchRecipe::result),
    AspectMap.CODEC.fieldOf("aspects").forGetter(WorkbenchRecipe::aspects)
  ).apply(builder, ::WorkbenchRecipe)
}
private val STREAM_CODEC = StreamCodec.composite(
  ShapedRecipePattern.STREAM_CODEC, WorkbenchRecipe::pattern,
  ItemStack.STREAM_CODEC, WorkbenchRecipe::result,
  AspectMap.STREAM_CODEC, WorkbenchRecipe::aspects,
  ::WorkbenchRecipe
)
