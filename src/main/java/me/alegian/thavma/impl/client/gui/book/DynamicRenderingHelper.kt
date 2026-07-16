package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.PageFeature
import net.minecraft.client.Minecraft

val LINE_HEIGHT = Minecraft.getInstance().font.lineHeight + 2
const val PARAGRAPH_OFFSET = 2 / 3f
val PRG_OFFSET_OTHER = LINE_HEIGHT * 2 / 3


/**
 *  Returns a list of lists of features, one feature per list -> one per page.
 */
fun pagifyFeatures(features: List<PageFeature>): List<List<PageFeature>> {
  return features.map { listOf(it) }
}