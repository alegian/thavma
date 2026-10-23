package me.alegian.thavma.impl.common.book

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

class ParagraphFeature(
  val text: Component,
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text),
      ).apply(builder, ::ParagraphFeature)
    }
  }
}
