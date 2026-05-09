package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.resources.ResourceLocation

class RecipeFeature(
  val recipeRL: ResourceLocation,
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.RECIPE.get()

  override val coversWholePage: Boolean
    get() = true
  override val mustStartPage: Boolean
    get() = true
  override val mustOccupySetPage: Boolean
    get() = true


  override val renderedHeight: Int
    get() = TODO("Not yet implemented")

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ResourceLocation.CODEC.fieldOf("recipeRL").forGetter(RecipeFeature::recipeRL),
      ).apply(builder, ::RecipeFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.figure_feature$featureIndex"
  }
}