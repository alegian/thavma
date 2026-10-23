package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.pageBreak
import me.alegian.thavma.impl.common.book.PageBreakFeature

object PageBreakFeatureRenderer : PageFeatureRenderer<PageBreakFeature> {

  override fun initPageFeature(
    screen: EntryScreen,
    feature: PageBreakFeature,
  ) {
    pageBreak()
  }
}
