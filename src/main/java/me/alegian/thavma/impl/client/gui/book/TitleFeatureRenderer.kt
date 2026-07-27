package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.FormattedCharSequence

object TitleFeatureRenderer : PageFeatureRenderer<TitleFeature> {

  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeature(
    screen: EntryScreen,
    feature: TitleFeature,
    font: Font
  ) {
    Title(feature, font)
    Separator()
  }

  private fun Separator() {
    Row({
      width = grow()
      alignMain = Alignment.CENTER
    }) {
      TextureBox(SEPARATOR) {}
    }
  }

  private fun Title(title: TitleFeature, font: Font) {

    var lines: List<FormattedCharSequence> = listOf()

    Row({
      width = grow()
      height = derived { w ->
        lines = font.split(title.text, w.toInt())
        (LINE_HEIGHT * lines.size).toFloat()
      }
    }) {
      draw {
        Renderable { guiGraphics, _, _, _ ->
          guiGraphics.usePose {
            for ((index, line) in lines.withIndex()) {
              guiGraphics.drawCenteredString(
                font, line, size.x / 2
              )
              translateXY(0, LINE_HEIGHT)
              if (index != lines.size - 1) translateXY(0, PRG_OFFSET_OTHER)
            }
          }
        }
      }
    }
  }
}
