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
import me.alegian.thavma.impl.common.book.ParagraphFeature
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.Minecraft

object DynamicFeaturesRenderer : PageFeatureRenderer<PageFeature> {
  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeatures(screen: EntryScreen, features: List<PageFeature>, maxWidth: Int) {

    val font = Minecraft.getInstance().font
    val LINE_HEIGHT = font.lineHeight + 2


    Column({
      //println("size before setting initial column is $size")
      size = grow()
      gap = 4
      //println("size after initial column is $size")
    }) {
      for (feature in features) {
        when (feature) {
          is TitleFeature -> {
            Title(feature, maxWidth)
            Separator()
          }

          is FormattedTextFeature -> Row({
            //println("size before setting row of FormattedTextFeature is $size")
            //size = grow()
            width = grow()
            height = fixed((font.lineHeight + 2) * feature.text.size + (font.lineHeight + 2) * 2 / 3)
            //println("size after FormattedTextFeature is $size")
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

          is FigureFeature -> Image(feature, maxWidth)

          is ParagraphFeature -> Row({
            //println("size before setting row of FormattedTextFeature is $size")
            //size = grow()
            val lines = font.split(feature.text, maxWidth)
            width = grow()
            height = fixed((font.lineHeight + 2) * lines.size + (font.lineHeight + 2) * 2 / 3)
            //println("size after FormattedTextFeature is $size")
          }) {
            relativeRenderable { guiGraphics, _, _, _ ->
              guiGraphics.usePose {
                for (line in font.split(feature.text, maxWidth)) {
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
        }
      }
    }
  }


  private fun Separator() {
    Row({
      //println("width before setting row of SEPARATOR is $width")
      width = grow()
      //println("width after separator is $width")
      alignMain = Alignment.CENTER
    }) {
      TextureBox(SEPARATOR) {}
    }
  }

  private fun Title(title: TitleFeature, maxWidth: Int) {
    val font = Minecraft.getInstance().font
    val lines = font.split(title.text, maxWidth)

    Row({
      //println("width before setting row of TitleFeature is $width")
      width = grow()
      //println("width after TitleFeature is $width")
      height = fixed((font.lineHeight + 2) * lines.size)
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.usePose {
          for ((index, line) in lines.withIndex()) {
            guiGraphics.drawCenteredString(font, line, size.x / 2)
            if (index != lines.size - 1) translateXY(0, font.lineHeight)
          }
        }
      }
    }
  }

//  private fun Image(figure: FigureFeature, maxWidth: Int) {
//    Row({
//      width = grow()
//      height = fixed(figure.textureHeight)
//    }) {
//      relativeRenderable { guiGraphics, _, _, _ ->
//        guiGraphics.usePose {
//          translateXY((maxWidth - figure.image.width) / 2, 0)
//          TextureBox(figure.image) {}
//        }
//      }
//    }
//  }
//}

  private fun Image(figure: FigureFeature, maxWidth: Int) {
    Row({
      width = grow()
      height = fixed(figure.textureHeight)
    }) {
      CenteredTextureBox(figure.image, maxWidth) {}
    }
  }
}



