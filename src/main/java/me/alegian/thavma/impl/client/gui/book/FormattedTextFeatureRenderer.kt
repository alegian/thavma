package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.gui.layout.relativeRenderable
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.FormattedTextFeature
import net.minecraft.client.gui.Font
import net.minecraft.util.Mth.ceil

object FormattedTextFeatureRenderer : PageFeatureRenderer<FormattedTextFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: FormattedTextFeature,
    maxWidth: Int,
    font: Font,
    scale: Float
  ) {
    val LINE_HEIGHT = ceil((font.lineHeight * scale + 2))

    Row({
      width = grow()
      height = fixed(LINE_HEIGHT * (feature.text.size + 0.5f))
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.pose().scale(scale, scale, 1.0f)
        guiGraphics.usePose {
          for (line in feature.text) {
            guiGraphics.drawString(font, line)
            translateXY(0, LINE_HEIGHT / scale)
          }
          translateXY(0, LINE_HEIGHT * 2 / 3)
        }
      }
    }
  }
}