package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.gui.layout.relativeRenderable
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.gui.Font
import net.minecraft.util.Mth.ceil

object ParagraphFeatureRenderer : PageFeatureRenderer<ParagraphFeature> {
  override fun initPageFeature(
    screen: EntryScreen,
    feature: ParagraphFeature,
    maxWidth: Int,
    font: Font,
    scale: Float
  ) {
    val LINE_HEIGHT = ceil((font.lineHeight * scale + 2))

    Row({
      val lines = font.split(feature.text, (maxWidth / scale).toInt())
      width = grow()
      height = fixed(LINE_HEIGHT * (lines.size + 0.5f))
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.pose().scale(scale, scale, 1.0f)
        guiGraphics.usePose {
          for (line in font.split(feature.text, (maxWidth / scale).toInt())) {
            guiGraphics.drawString(font, line)
            translateXY(0, LINE_HEIGHT / scale)
          }
          translateXY(0, LINE_HEIGHT * 2 / 3)
        }
      }
    }
  }
}