package me.alegian.thavma.impl.common.book

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.chat.Style

class ParagraphFeature(val text: Component, override val mustStartPage: Boolean = false, override val mustOccupySetPage: Boolean = false, override val preferredPageIndex: Int = 1) : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()

  override val coversOneWholePage = false

  val font: Font = Minecraft.getInstance().font


  // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
  val LINE_HEIGHT = font.lineHeight + 2
  val lines = font.splitter.splitLines(text, pageWidth, Style.EMPTY)

  override val renderedHeight = LINE_HEIGHT * lines.size + LINE_HEIGHT * 2 / 3

  companion object {
    val CODEC = RecordCodecBuilder.mapCodec { builder ->
      builder.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(ParagraphFeature::text),
        //ResourceLocation.CODEC.optionalFieldOf("font", ResourceLocation.withDefaultNamespace("default")).forGetter(ParagraphFeature::font),
        Codec.BOOL.optionalFieldOf("starts_page", false).forGetter(ParagraphFeature::mustStartPage),
        Codec.BOOL.optionalFieldOf("has_set_page", false).forGetter(ParagraphFeature::mustOccupySetPage),
        Codec.INT.optionalFieldOf("preferred_page", 1).forGetter(ParagraphFeature::preferredPageIndex)
      ).apply(builder, ::ParagraphFeature)
    }

    fun translationId(baseId: String, featureIndex: Int) = "$baseId.paragraph_feature$featureIndex"
  }

  //fun titleTranslationId(baseId: String, pageIndex: Int) = Page.translationId(baseId, pageIndex) + ".title"
  //fun paragraphFeatureTranslationId(baseId: String, index: Int) = Page.translationId(baseId, pageIndex) + ".paragraph$index"

}