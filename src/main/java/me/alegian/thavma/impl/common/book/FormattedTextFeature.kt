package me.alegian.thavma.impl.common.book

import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.util.FormattedCharSequence

/**
 * Is only ever used in EntryScreen.kt to break ParagraphFeatures into segments
 * so that they fit in their respective pages
 */

class FormattedTextFeature(val text: List<FormattedCharSequence>, val isTitle: Boolean = false) : PageFeature {
  override val coversOneWholePage: Boolean
    get() = false
  override val mustStartPage: Boolean
    get() = false
  override val mustOccupySetPage: Boolean
    get() = false

  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()

  override fun toString(): String {
    return "FormattedTextFeature with number of lines ${text.size}"
  }

  //val font: Font = Minecraft.getInstance().font
  //val DEFAULT_FONT = ResourceLocation.fromNamespaceAndPath("thavma","font:default.ttf")
  //val x: Font? = null


  // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
  //val LINE_HEIGHT = font.lineHeight + 2
  //val LINE_HEIGHT = 11
  //val lines = font.splitter.splitLines(text, pageWidth - 25, Style.EMPTY)

  //override val renderedHeight = LINE_HEIGHT * text.size + LINE_HEIGHT * 2 / 3
}