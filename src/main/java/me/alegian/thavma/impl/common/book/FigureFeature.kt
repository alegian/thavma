package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import java.util.*

class FigureFeature(val image: Texture, val caption: Component?, override val mustStartPage: Boolean = false, override val mustOccupySetPage: Boolean = false, override val preferredPageIndex: Int = 1) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.FIGURE.get()

  override val coversOneWholePage = false


//
//  override val pageWidth: Int
//    get() = 256


  //val font: Font = Minecraft.getInstance().font

  // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
  //val LINE_HEIGHT = font.lineHeight + 2
  //val LINE_HEIGHT = 11

  //val lines = if (caption != null) font.splitter.splitLines(caption, pageWidth - 25, Style.EMPTY) else listOf()

  val textureHeight = image.height
  //val captionHeight = LINE_HEIGHT * lines.size + LINE_HEIGHT * 4 / 3

  //val captionHeight = if (caption != null) LINE_HEIGHT * caption.string.length*5/115 + LINE_HEIGHT * 4 / 3 else 0
  //override val renderedHeight = textureHeight + captionHeight

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        Texture.CODEC.fieldOf("image").forGetter(FigureFeature::image),
        ComponentSerialization.CODEC.optionalFieldOf("caption").forGetter { p -> Optional.ofNullable(p.caption) },
        Codec.BOOL.optionalFieldOf("starts_page", false).forGetter(FigureFeature::mustStartPage),
        Codec.BOOL.optionalFieldOf("has_set_page", false).forGetter(FigureFeature::mustOccupySetPage),
        Codec.INT.optionalFieldOf("preferred_page", 1).forGetter(FigureFeature::preferredPageIndex)
      ).apply(builder) { img, cap, start, index, pref ->
        FigureFeature(img, cap.orElse(null), start, index, pref)
      }
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.figure_feature$featureIndex"
  }

}