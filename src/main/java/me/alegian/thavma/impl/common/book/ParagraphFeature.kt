package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class ParagraphFeature(
  val text: Component,
  override val startsPage: Boolean = false,
  override val forceIndex: Int? = null
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text),
        Codec.BOOL.optionalFieldOf("starts_page", false).forGetter(ParagraphFeature::startsPage),
        Codec.INT.optionalFieldOf("force_index", null).forGetter(ParagraphFeature::forceIndex)
      ).apply(builder, ::ParagraphFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.paragraph_feature$featureIndex"
  }
}