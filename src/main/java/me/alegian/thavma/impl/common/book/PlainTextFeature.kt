package me.alegian.thavma.impl.common.book

import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

/**
 * Is only ever used in EntryScreen.kt to break ParagraphFeatures into segments
 * so that they fit in their respective pages
 */


class PlainTextFeature(val text: Component, val font: Font = Minecraft.getInstance().font): PageFeature {
  override val coversWholePage: Boolean
    get() = false
  override val mustStartPage: Boolean
    get() = false
  override val mustOccupySetPage: Boolean
    get() = false

  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.PARAGRAPH.get()


  // if a recalibrated font size is used, I can multiply or divide by rendering scaling factor
  val LINE_HEIGHT = font.lineHeight + 2
  val lines = font.splitter.splitLines(text, pageWidth - 25, Style.EMPTY)

  override val renderedHeight = LINE_HEIGHT * lines.size + LINE_HEIGHT * 2 / 3
}