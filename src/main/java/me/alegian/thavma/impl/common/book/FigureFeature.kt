package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.Style
import java.util.Optional

class FigureFeature(val image: Texture, val caption: Component?, override val mustStartPage: Boolean = false) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.FIGURE.get()

  override val coversWholePage = false
  override val mustOccupySetPage = false
  override val preferredPageIndex: Int
    get() = 1

  override val pageWidth: Int
    get() = 256


  val font: Font = Minecraft.getInstance().font

  // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
  val LINE_HEIGHT = font.lineHeight + 2
  val lines = font.splitter.splitLines(caption, pageWidth - 25, Style.EMPTY)
  override val renderedHeight = image.canvasHeight + LINE_HEIGHT * lines.size + LINE_HEIGHT * 4 / 3


  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        Texture.CODEC.fieldOf("image").forGetter(FigureFeature::image),
        ComponentSerialization.CODEC.optionalFieldOf("caption").forGetter { p -> Optional.ofNullable(p.caption) },
        Codec.BOOL.optionalFieldOf("starts_page", false).forGetter(FigureFeature::mustStartPage)
      ).apply(builder) { img, cap, start ->
        FigureFeature(img, cap.orElse(null), start)
      }
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.figure_feature$featureIndex"
  }

}