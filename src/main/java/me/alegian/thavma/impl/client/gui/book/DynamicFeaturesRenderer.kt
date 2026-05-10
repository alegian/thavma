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

  override fun initPageFeatures(screen: EntryScreen, features: List<PageFeature>) {

    Column({
      size = grow()
      gap = 4
    }) {
      for (feature in features) {
        when (feature) {
          is TitleFeature -> {
            Title(feature)
            Separator()
          }

          is FormattedTextFeature -> Row({
            size = grow()
          }) {
            relativeRenderable { guiGraphics, _, _, _ ->
              guiGraphics.usePose {
                for (line in feature.text) {
                  guiGraphics.drawString(feature.font, line)
                  translateXY(0, feature.font.lineHeight)
                }
                translateXY(0, feature.font.lineHeight * 2 / 3)
              }
            }
          }
          is FigureFeature -> Image(feature)
        }
      }
    }
  }

//    Column({
//      size = grow()
//      gap = 4
//    }) {
//      if (page.title != null) {
//        Title(page.title)
//        Separator()
//      }
//      Row({
//        size = grow()
//      }) {
//        relativeRenderable { guiGraphics, _, _, _ ->
//          guiGraphics.usePose {
//            for (paragraph in page.paragraphs) {
//              for (line in font.splitter.splitLines(paragraph, size.x.toInt(), Style.EMPTY)) {
//                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(line.string))
//                translateXY(0, LINE_HEIGHT)
//              }
//              translateXY(0, LINE_HEIGHT * 2 / 3)
//            }
//          }
//        }
//      }
//    }
//  }

  private fun Separator() {
    Row({
      width = grow()
      alignMain = Alignment.CENTER
    }) {
      TextureBox(SEPARATOR) {}
    }
  }

  private fun Title(title: TitleFeature) {
    val font = Minecraft.getInstance().font

    Row({
      width = grow()
      height = fixed(font.lineHeight)
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.drawCenteredString(font, title.text, size.x / 2)
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



