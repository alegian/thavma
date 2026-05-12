package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.FigureFeature
import me.alegian.thavma.impl.common.book.FormattedTextFeature
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.Minecraft

object DynamicFeaturesRenderer : PageFeatureRenderer<PageFeature> {
  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeatures(screen: EntryScreen, features: List<PageFeature>, maxWidth: Int) {

    val font = Minecraft.getInstance().font
    val LINE_HEIGHT = font.lineHeight + 2

    Column({
      size = grow()
      gap = 4
    }) {
      for (feature in features) {
        when (feature) {
          is TitleFeature -> {
            Title(feature, maxWidth)
            Separator()
          }

          is FormattedTextFeature -> Row({
            size = grow()
          }) {
            relativeRenderable { guiGraphics, _, _, _ ->
              guiGraphics.usePose {
                for (line in feature.text) {
                  //guiGraphics.drawString(feature.font, line)
                  guiGraphics.drawString(font, line)
                  //translateXY(0, feature.font.lineHeight)
                  translateXY(0, LINE_HEIGHT)
                }
                //translateXY(0, feature.font.lineHeight * 2 / 3)
                translateXY(0, LINE_HEIGHT * 2 / 3)
              }
            }
          }

          is FigureFeature -> Image(feature)
        }
      }
    }
  }


  private fun Separator() {
    Row({
      width = grow()
      alignMain = Alignment.CENTER
    }) {
      TextureBox(SEPARATOR) {}
    }
  }

  private fun Title(title: TitleFeature, maxWidth: Int) {
    val font = Minecraft.getInstance().font
    val lines = font.split(title.text, maxWidth)

    Row({
      width = grow()
      height = fixed((font.lineHeight + 2) * lines.size)
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.usePose {
          for (line in lines) {
            guiGraphics.drawCenteredString(font, line, size.x / 2)
            translateXY(0, font.lineHeight)
          }
        }
      }
    }
  }

  private fun Image(figure: FigureFeature) {
    Row({
      width = grow()
      height = fixed(figure.textureHeight)
    }) {
      TextureBox(figure.image) {}
    }
  }
}



