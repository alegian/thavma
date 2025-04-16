package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics

object TextPageRenderer: PageRenderer<TextPage> {
  override fun render(page: TextPage, guiGraphics: GuiGraphics, font: Font) {
    guiGraphics.drawString(font, page.text)
  }
}