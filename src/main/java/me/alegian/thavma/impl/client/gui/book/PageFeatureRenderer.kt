package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.PageFeatureType
import net.minecraft.client.gui.Font

val PAGE_FEATURE_RENDERERS = mutableMapOf<PageFeatureType<*>, PageFeatureRenderer<*>>()

interface PageFeatureRenderer<T : PageFeature> {
  fun initPageFeature(screen: EntryScreen, feature: T, maxWidth: Int, font: Font, scale: Float)
}