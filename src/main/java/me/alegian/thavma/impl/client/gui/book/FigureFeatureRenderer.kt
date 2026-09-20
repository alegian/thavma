package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.CenteredTextureBox
import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.common.book.FigureFeature

object FigureFeatureRenderer : PageFeatureRenderer<FigureFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: FigureFeature,
  ) {
    Row({
      width = grow()
      height = fixed(feature.height)
    }) {
      CenteredTextureBox(feature.image, feature.width, feature.height)
    }
  }
}
