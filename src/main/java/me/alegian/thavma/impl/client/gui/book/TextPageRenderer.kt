package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component

object TextPageRenderer : PageRenderer<TextPage> {
  override fun initPage(screen: EntryScreen, page: TextPage) {
    val LINE_HEIGHT = screen.getFont().lineHeight + 2

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
          screen.addRenderableOnly(Renderable { guiGraphics, mouseX, mouseY, tickDelta ->
            guiGraphics.usePose {
              translateXY(position.x, position.y)

              for (paragraph in page.paragraphs) {
                for (line in screen.getFont().split(paragraph, size.x.toInt())) {
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
}

fun Separator(screen: EntryScreen) {
  Row({
    width = grow()
    alignMain = Alignment.CENTER
  }) {
    Row({
      width = fixed(SEPARATOR.width)
      height = fixed(SEPARATOR.height)
    }) {
      afterLayout {
        screen.addRenderableOnly(texture(SEPARATOR))
      }
    }
  }
}

fun Title(screen: EntryScreen, text: Component) {
  Row({
    width = grow()
    height = fixed(screen.getFont().lineHeight)
  }) {
    afterLayout {
      screen.addRenderableOnly(Renderable { guiGraphics, mouseX, mouseY, tickDelta ->
        guiGraphics.usePose {
          translateXY(position.x, position.y)
          guiGraphics.drawCenteredString(screen.getFont(), text, size.x / 2)
        }
      })
    }
  }
}