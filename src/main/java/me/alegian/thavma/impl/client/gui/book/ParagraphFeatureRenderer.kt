package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.derived
import me.alegian.thavma.impl.client.gui.layout.draw
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

object ParagraphFeatureRenderer : PageFeatureRenderer<ParagraphFeature> {
  override fun initPageFeature(
    screen: EntryScreen,
    feature: ParagraphFeature,
  ) {
    renderParagraph(screen.getFont(), feature.text)
  }

  fun renderParagraph(
    font: Font,
    text: Component,
    centered: Boolean = false,
  ) {
    var lines: List<FormattedCharSequence> = listOf()
    Row({
      width = grow()
      height = derived { w ->
        lines = font.split(text, w.toInt())
        (font.lineHeight * lines.size * ClientHelper.LINE_GAP_FACTOR).toFloat()
      }
    }) {
      draw {
        Renderable { guiGraphics, _, _, _ ->
          guiGraphics.usePose {
            for (line in lines) {
              if (centered) guiGraphics.drawCenteredString(font, line, size.x / 2)
              else guiGraphics.drawString(font, line)
              translateXY(0, font.lineHeight * ClientHelper.LINE_GAP_FACTOR)
            }
          }
        }
      }
    }
  }
}
