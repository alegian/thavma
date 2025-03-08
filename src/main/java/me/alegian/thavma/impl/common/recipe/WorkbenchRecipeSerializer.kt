package me.alegian.thavma.impl.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.AspectMap
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.ShapedRecipePattern

class WorkbenchRecipeSerializer : RecipeSerializer<WorkbenchRecipe> {
  override fun codec(): MapCodec<WorkbenchRecipe> {
    return CODEC
  }

  override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> {
    return STREAM_CODEC
  }

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ShapedRecipePattern.MAP_CODEC.forGetter(WorkbenchRecipe::getPattern),
        ItemStack.CODEC.fieldOf("resultItem").forGetter(WorkbenchRecipe::getResultItem),
        AspectMap.CODEC.fieldOf("resultAspects").forGetter(WorkbenchRecipe::getResultAspects)
      ).apply(builder, ::WorkbenchRecipe)
    }
    val STREAM_CODEC = StreamCodec.composite(
      ShapedRecipePattern.STREAM_CODEC, WorkbenchRecipe::getPattern,
      ItemStack.STREAM_CODEC, WorkbenchRecipe::getResultItem,
      AspectMap.STREAM_CODEC, WorkbenchRecipe::getResultAspects,
      ::WorkbenchRecipe
    )
  }
}
