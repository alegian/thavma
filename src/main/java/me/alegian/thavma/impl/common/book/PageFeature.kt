package me.alegian.thavma.impl.common.book

import me.alegian.thavma.impl.init.registries.T7Registries

interface PageFeature {

  val startsPage: Boolean
  val forceIndex: Int?
    get() = null

  val type: PageFeatureType<*>

  companion object {
    val CODEC =
      T7Registries.PAGE_FEATURE_TYPE.byNameCodec().dispatch({ pageFeature -> pageFeature.type }, { type -> type.codec })
  }
}
