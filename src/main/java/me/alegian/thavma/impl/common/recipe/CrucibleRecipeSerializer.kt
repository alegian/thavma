package me.alegian.thavma.impl.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class CrucibleRecipeSerializer : RecipeSerializer<CrucibleRecipe> {
  override fun codec(): MapCodec<CrucibleRecipe> {
    return CODEC
  }

  override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, CrucibleRecipe> {
    return STREAM_CODEC
  }

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { inst: RecordCodecBuilder.Instance<CrucibleRecipe> ->
      inst.group(
        AspectMap.CODEC.fieldOf("aspects").forGetter(CrucibleRecipe::getRequiredAspects),
        Ingredient.CODEC.fieldOf("catalyst").forGetter(CrucibleRecipe::getRequiredCatalyst),
        ItemStack.CODEC.fieldOf("result").forGetter(CrucibleRecipe::getResult)
      ).apply(inst, ::CrucibleRecipe)
    }
    val STREAM_CODEC = StreamCodec.composite(
      AspectMap.STREAM_CODEC, CrucibleRecipe::getRequiredAspects,
      Ingredient.CONTENTS_STREAM_CODEC, CrucibleRecipe::getRequiredCatalyst,
      ItemStack.STREAM_CODEC, CrucibleRecipe::getResult,
      ::CrucibleRecipe
    )
  }
}
