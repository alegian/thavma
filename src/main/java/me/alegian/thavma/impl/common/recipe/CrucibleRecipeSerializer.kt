package me.alegian.thavma.impl.common.recipe

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class CrucibleRecipeSerializer : RecipeSerializer<CrucibleRecipe> {
  override fun codec()= CODEC
  override fun streamCodec() = STREAM_CODEC
}

private val CODEC = RecordCodecBuilder.mapCodec { builder ->
  builder.group(
    AspectMap.CODEC.fieldOf("aspects").forGetter(CrucibleRecipe::aspects),
    Ingredient.CODEC.fieldOf("catalyst").forGetter(CrucibleRecipe::catalyst),
    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CrucibleRecipe::result)
  ).apply(builder, ::CrucibleRecipe)
}
private val STREAM_CODEC = StreamCodec.composite(
  AspectMap.STREAM_CODEC, CrucibleRecipe::aspects,
  Ingredient.CONTENTS_STREAM_CODEC, CrucibleRecipe::catalyst,
  ItemStack.STREAM_CODEC, CrucibleRecipe::result,
  ::CrucibleRecipe
)
