package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.resources.ResourceLocation

class RecipeFeature(
  val recipeRL: ResourceLocation, override val forceIndex: Int = 1
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.RECIPE.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ResourceLocation.CODEC.fieldOf("recipeRL").forGetter(RecipeFeature::recipeRL),
        Codec.INT.optionalFieldOf("force_index", 1).forGetter(RecipeFeature::forceIndex)
      ).apply(builder, ::RecipeFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.recipe_feature$featureIndex"
  }
}