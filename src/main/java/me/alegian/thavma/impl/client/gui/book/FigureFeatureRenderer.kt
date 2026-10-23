package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.Alignment
import me.alegian.thavma.impl.client.gui.layout.Column
import me.alegian.thavma.impl.client.gui.layout.TextureBox
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.common.book.FigureFeature

object FigureFeatureRenderer : PageFeatureRenderer<FigureFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: FigureFeature,
  ) {
    Column({ width = grow(); gap = 8; alignCross = Alignment.CENTER }) {
      TextureBox(feature.image, feature.width, feature.height) {}

      if (feature.caption != null)
        ParagraphFeatureRenderer.renderParagraph(screen.getFont(), feature.caption)
    }
  }
}
