package me.alegian.thavma.impl.common.recipe

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.codec.listOf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class InfusionRecipeSerializer : RecipeSerializer<InfusionRecipe> {
  override fun codec() = CODEC
  override fun streamCodec() = STREAM_CODEC
}

private val CODEC = RecordCodecBuilder.mapCodec<InfusionRecipe> {
  it.group(
    Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter { it.base },
    ItemStack.STRICT_CODEC.fieldOf("result").forGetter { it.result },
    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter { it.ingredients },
    AspectMap.CODEC.fieldOf("aspects").forGetter { it.aspects }
  ).apply(it, ::InfusionRecipe)
}
private val STREAM_CODEC = StreamCodec.composite(
  Ingredient.CONTENTS_STREAM_CODEC, InfusionRecipe::base,
  ItemStack.STREAM_CODEC, InfusionRecipe::result,
  Ingredient.CONTENTS_STREAM_CODEC.listOf(), InfusionRecipe::ingredients,
  AspectMap.STREAM_CODEC, InfusionRecipe::aspects,
  ::InfusionRecipe
)
