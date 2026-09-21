package me.alegian.thavma.impl.common.book

import com.mojang.serialization.MapCodec
import me.alegian.thavma.impl.init.registries.deferred.PageFeatureTypes

class PageBreakFeature() : PageFeature {
  override val type: PageFeatureType<*>
    get() = PageFeatureTypes.BREAK.get()

  companion object {
    val CODEC: MapCodec<PageBreakFeature> = MapCodec.unit(::PageBreakFeature)
  }
}