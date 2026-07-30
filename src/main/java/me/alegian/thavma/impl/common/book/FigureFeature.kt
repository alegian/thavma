package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import java.util.*

class FigureFeature(
  val image: Texture,
  val caption: Component?,
  override val forceIndex: Int = -1
) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.FIGURE.get()

  val textureHeight = image.height

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        Texture.CODEC.fieldOf("image").forGetter(FigureFeature::image),
        ComponentSerialization.CODEC.optionalFieldOf("caption").forGetter { p -> Optional.ofNullable(p.caption) },
        Codec.INT.optionalFieldOf("force_index", -1).forGetter(FigureFeature::forceIndex)
      ).apply(builder) { img, cap, index ->
        FigureFeature(img, cap.orElse(Component.literal("")), index)
      }
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.figure_feature$featureIndex"
  }

}