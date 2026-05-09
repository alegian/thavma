package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.ParagraphFeature
import net.minecraft.client.Minecraft

object DynamicFeaturesRenderer : PageFeatureRenderer<PageFeature> {
  private val SEPARATOR = Texture("gui/book/separator", 128, 16, 128, 16)

  override fun initPageFeatures(screen: EntryScreen, features: List<PageFeature>, currentPage: Int) {
    val font = Minecraft.getInstance().font
    val LINE_HEIGHT = font.lineHeight + 2


  }

}