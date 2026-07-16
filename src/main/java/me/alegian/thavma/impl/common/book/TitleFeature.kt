package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class TitleFeature(val text: Component, override val startsPage: Boolean = true, override val forceIndex: Int = -1) :
  PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.TITLE.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(TitleFeature::text),
        Codec.BOOL.optionalFieldOf("starts_page", true).forGetter(TitleFeature::startsPage),
        Codec.INT.optionalFieldOf("force_index", -1).forGetter(TitleFeature::forceIndex)
      ).apply(builder, ::TitleFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.title_feature$featureIndex"
  }
}