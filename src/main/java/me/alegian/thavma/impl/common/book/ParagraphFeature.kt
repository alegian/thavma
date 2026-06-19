package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class ParagraphFeature(
  val text: Component,
  override val mustStartPage: Boolean = false,
  override val mustOccupySetPage: Boolean = false,
  override val preferredPageIndex: Int = 1
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()

  override val coversOneWholePage = false

  override fun toString(): String {
    return "ParagraphFeature with text $text"
  }

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text),
        Codec.BOOL.optionalFieldOf("must_start_page", false).forGetter(ParagraphFeature::mustStartPage),
        Codec.BOOL.optionalFieldOf("must_occupy_set_page", false).forGetter(ParagraphFeature::mustOccupySetPage),
        Codec.INT.optionalFieldOf("preferred_page_index", 1).forGetter(ParagraphFeature::preferredPageIndex)
      ).apply(builder, ::ParagraphFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.paragraph_feature$featureIndex"
  }

}