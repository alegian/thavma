package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.resources.ResourceLocation

class RecipeFeature(
  val recipeRL: ResourceLocation, override val coversOneWholePage: Boolean = true, override val mustStartPage: Boolean = true, override val mustOccupySetPage: Boolean = true,
  override val preferredPageIndex: Int = 1) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.RECIPE.get()

  override val renderedHeight: Int
    get() = 96

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ResourceLocation.CODEC.fieldOf("recipeRL").forGetter(RecipeFeature::recipeRL),
        Codec.BOOL.optionalFieldOf("covers_whole_page", true).forGetter(RecipeFeature::coversOneWholePage),
        Codec.BOOL.optionalFieldOf("starts_page", true).forGetter(RecipeFeature::mustStartPage),
        Codec.BOOL.optionalFieldOf("has_set_page", true).forGetter(RecipeFeature::mustOccupySetPage),
        Codec.INT.optionalFieldOf("preferred_page", 1).forGetter(RecipeFeature::preferredPageIndex)
      ).apply(builder, ::RecipeFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.figure_feature$featureIndex"
  }
}