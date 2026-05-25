package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.PageFeature
import net.minecraft.client.gui.Font

interface PageFeatureRenderer<T: PageFeature>{
  fun initPageFeatures(screen: EntryScreen, features: List<T>, maxWidth: Int, font: Font, scale: Float)
}