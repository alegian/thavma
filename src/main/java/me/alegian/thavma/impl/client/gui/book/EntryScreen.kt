package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

private val BG = Texture("gui/book/background", 510, 282, 512, 512)

class EntryScreen(private val entry: ResearchEntry) : Screen(Component.literal("Book Entry")) {
  private var left = 0.0
  private var top = 0.0

  override fun init() {
    super.init()
    left = (width - BG.width) / 2.0
    top = (height - BG.height) / 2.0
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderTransparentBackground(guiGraphics)
    guiGraphics.usePose {
      translate(left, top, 0.0)
      guiGraphics.blit(BG)

      val page = entry.pages.getOrNull(0)
      if (page is TextPage) guiGraphics.drawString(font, page.text)
    }
  }
}