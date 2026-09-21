package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.TitleFeature
import net.minecraft.client.gui.Font

object TitleFeatureRenderer : PageFeatureRenderer<TitleFeature> {

  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeature(
    screen: EntryScreen,
    feature: TitleFeature,
  ) {
    Title(feature, screen.getFont())
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
    ParagraphFeatureRenderer.renderParagraph(font, title.text, centered = true)
  }
}
