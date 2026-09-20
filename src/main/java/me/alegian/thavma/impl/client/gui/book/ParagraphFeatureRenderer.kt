package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.derived
import me.alegian.thavma.impl.client.gui.layout.draw
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.FormattedCharSequence

object ParagraphFeatureRenderer : PageFeatureRenderer<ParagraphFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: ParagraphFeature,
  ) {
    val font = screen.getFont()
    var lines: List<FormattedCharSequence> = listOf()
    Row({
      width = grow()
      height = derived { w ->
        lines = screen.getFont().split(feature.text, w.toInt())
        (font.lineHeight * lines.size * ClientHelper.LINE_GAP_FACTOR).toFloat()
      }
    }) {
      draw {
        Renderable { guiGraphics, _, _, _ ->
          guiGraphics.usePose {
            for (line in lines) {
              guiGraphics.drawString(screen.getFont(), line)
              translateXY(0, font.lineHeight * ClientHelper.LINE_GAP_FACTOR)
            }
          }
        }
      }
    }
  }
}
