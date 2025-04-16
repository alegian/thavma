package me.alegian.thavma.impl.common.book

import me.alegian.thavma.impl.init.registries.T7Registries

interface Page {
  val type: PageType<*>

  companion object{
    val CODEC = T7Registries.PAGE_TYPE.byNameCodec().dispatch({ page -> page.type }, { type -> type.codec })
  }
}