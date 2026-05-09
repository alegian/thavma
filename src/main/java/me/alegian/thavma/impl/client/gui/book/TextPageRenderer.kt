package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

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
        Title(page.title)
        Separator()
      }
      Row({
        size = grow()
      }) {
        relativeRenderable { guiGraphics, _, _, _ ->
          guiGraphics.usePose {
            for (paragraph in page.paragraphs) {
              for (line in font.splitter.splitLines(paragraph, size.x.toInt(), Style.EMPTY)) {
                guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(line.string))
                translateXY(0, LINE_HEIGHT)
              }
              translateXY(0, LINE_HEIGHT * 2 / 3)
            }
          }
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

  private fun Title(text: Component) {
    val font = Minecraft.getInstance().font

    Row({
      width = grow()
      height = fixed(font.lineHeight)
    }) {
      relativeRenderable { guiGraphics, _, _, _ ->
        guiGraphics.drawCenteredString(font, text, size.x / 2)
      }
    }
  }
}
