package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component

object TextPageRenderer : PageRenderer<TextPage> {
  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPage(screen: EntryScreen, page: TextPage) {
    val font = Minecraft.getInstance().font
    val LINE_HEIGHT = font.lineHeight + 2

    Column({
      size = grow()
      gap = 4
    }) {
      if (page.title != null) {
        Title(screen, page.title)
        Separator(screen)
      }
      Row({
        size = grow()
      }) {
        afterLayout {
          screen.renderables.add(Renderable { guiGraphics, mouseX, mouseY, tickDelta ->
            guiGraphics.usePose {
              translateXY(position.x, position.y)

              for (paragraph in page.paragraphs) {
                for (line in font.split(paragraph, size.x.toInt())) {
                  guiGraphics.drawString(Minecraft.getInstance().font, line)
                  translateXY(0, LINE_HEIGHT)
                }
                translateXY(0, LINE_HEIGHT)
              }
            }
          })
        }
      }
    }
  }

  private fun Separator(screen: EntryScreen) {
    Row({
      width = grow()
      alignMain = Alignment.CENTER
    }) {
      TextureBox(screen, SEPARATOR) {}
    }
  }

  private fun Title(screen: EntryScreen, text: Component) {
    val font = Minecraft.getInstance().font
   
    Row({
      width = grow()
      height = fixed(font.lineHeight)
    }) {
      afterLayout {
        screen.renderables.add(Renderable { guiGraphics, mouseX, mouseY, tickDelta ->
          guiGraphics.usePose {
            translateXY(position.x, position.y)
            guiGraphics.drawCenteredString(font, text, size.x / 2)
          }
        })
      }
    }
  }
}
