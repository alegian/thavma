package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.gui.layout.Box
import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.T7LayoutElement
import me.alegian.thavma.impl.client.gui.layout.derived
import me.alegian.thavma.impl.client.gui.layout.draw
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.gui.layout.splitForPagination
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import kotlin.math.floor

object ParagraphFeatureRenderer : PageFeatureRenderer<ParagraphFeature> {
  override fun initPageFeature(screen: EntryScreen, feature: ParagraphFeature) {
    renderParagraph(screen.getFont(), feature.text, splittable = true)
  }

  fun renderParagraph(font: Font, text: Component, centered: Boolean = false, splittable: Boolean = false) {
    var lines: List<FormattedCharSequence> = listOf()
    val lineHeight = (font.lineHeight * ClientHelper.LINE_GAP_FACTOR).toFloat()
    if (splittable) {
      splitForPagination { availableHeight ->
        splitParagraph(font, lines, size.x, lineHeight, availableHeight, centered)
      }
    }
    Row({
      width = grow()
      height = derived { w ->
        lines = font.split(text, w.toInt())
        lineHeight * lines.size
      }
    }) {
      draw { paragraphRenderable(font, lines, size.x, lineHeight, centered) }
    }
  }

  private fun splitParagraph(
    font: Font,
    lines: List<FormattedCharSequence>,
    width: Float,
    lineHeight: Float,
    availableHeight: Float,
    centered: Boolean,
  ): Pair<T7LayoutElement, T7LayoutElement>? {
    val fittingLines = floor(availableHeight / lineHeight).toInt()
    if (fittingLines <= 0 || fittingLines >= lines.size) return null

    return paragraphFragment(font, lines.subList(0, fittingLines), width, lineHeight, centered) to
      paragraphFragment(font, lines.subList(fittingLines, lines.size), width, lineHeight, centered)
  }

  private fun paragraphFragment(
    font: Font,
    lines: List<FormattedCharSequence>,
    fragmentWidth: Float,
    lineHeight: Float,
    centered: Boolean,
  ) = Box({
    width = fixed(fragmentWidth)
    height = fixed(lineHeight * lines.size)
  }) {
    splitForPagination { availableHeight ->
      splitParagraph(font, lines, size.x, lineHeight, availableHeight, centered)
    }
    draw { paragraphRenderable(font, lines, size.x, lineHeight, centered) }
  }

  private fun paragraphRenderable(
    font: Font,
    lines: List<FormattedCharSequence>,
    width: Float,
    lineHeight: Float,
    centered: Boolean,
  ) = Renderable { guiGraphics, _, _, _ ->
    guiGraphics.usePose {
      for (line in lines) {
        if (centered) guiGraphics.drawCenteredString(font, line, width / 2)
        else guiGraphics.drawString(font, line)
        translateXY(0, lineHeight)
      }
    }
  }
}
