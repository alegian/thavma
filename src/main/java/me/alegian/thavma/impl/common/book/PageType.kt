package me.alegian.thavma.impl.common.book

import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation

class PageType<T : Page>(name: ResourceLocation, val codec: MapCodec<T>) {
  val stringName = name.toString()

  override fun toString(): String {
    return stringName
  }
}