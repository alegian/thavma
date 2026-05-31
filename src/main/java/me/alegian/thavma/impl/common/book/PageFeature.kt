package me.alegian.thavma.impl.common.book

import me.alegian.thavma.impl.init.registries.T7Registries

interface PageFeature {
  val coversOneWholePage: Boolean
  val mustStartPage: Boolean
  val mustOccupySetPage: Boolean
  val preferredPageIndex: Int
    get() = 1

  val type: PageFeatureType<*>

  companion object {
    val CODEC =
      T7Registries.PAGE_FEATURE_TYPE.byNameCodec().dispatch({ pageFeature -> pageFeature.type }, { type -> type.codec })
  }
}