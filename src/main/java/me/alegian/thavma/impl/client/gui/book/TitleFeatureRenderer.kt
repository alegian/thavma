package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.Mth.ceil

object TitleFeatureRenderer : PageFeatureRenderer<TitleFeature> {
  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeature(
    screen: EntryScreen,
    feature: TitleFeature,
    maxWidth: Int,
    font: Font,
    scale: Float
  ) {
    Title(feature, maxWidth, font, scale)
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

  private fun Title(title: TitleFeature, maxWidth: Int, font: Font, scale: Float) {

    val lines = font.split(title.text, (maxWidth / scale).toInt())

    Row({
      width = grow()
      height = fixed(ceil((font.lineHeight * scale + 2)) * lines.size)
    }) {
      relativeRenderable {
        Renderable { guiGraphics, _, _, _ ->
          guiGraphics.pose().scale(scale, scale, 1.0f)
          guiGraphics.usePose {
            for ((index, line) in lines.withIndex()) {
              guiGraphics.drawCenteredString(
                font, line, size.x / scale / 2
              )
              if (index != lines.size - 1) translateXY(0, ceil((font.lineHeight * scale + 2)) / scale)
            }
          }
        }
      }
    }
  }
}