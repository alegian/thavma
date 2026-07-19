package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.CenteredTextureBox
import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.fixed
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.common.book.FigureFeature
import net.minecraft.client.gui.Font

object FigureFeatureRenderer : PageFeatureRenderer<FigureFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: FigureFeature,
    maxWidth: Int,
    font: Font
  ) {
    Row({
      width = grow()
      height = fixed(feature.textureHeight)
    }) {
      CenteredTextureBox(feature.image, maxWidth)
    }
  }
}