package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.draw
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable

object ParagraphFeatureRenderer : PageFeatureRenderer<ParagraphFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: ParagraphFeature,
    maxWidth: Int,
    font: Font
  ) {

    val lines = font.split(feature.text, maxWidth)
    Row({
      width = grow()
      height = fixed(LINE_HEIGHT * (lines.size + PARAGRAPH_OFFSET))
    }) {
      draw {
      Renderable { guiGraphics, _, _, _ ->
        guiGraphics.usePose {
          for (line in lines) {
            guiGraphics.drawString(font, line)
            translateXY(0, LINE_HEIGHT)
          }
          translateXY(0, PRG_OFFSET_OTHER)
        }
      }
      }
    }
  }
}