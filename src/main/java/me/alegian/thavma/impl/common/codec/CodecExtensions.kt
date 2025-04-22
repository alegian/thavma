package me.alegian.thavma.impl.common.codec

import com.mojang.serialization.Codec

fun <T> Codec<T>.mutableSetOf(): Codec<MutableSet<T>> {
  return listOf().xmap(
    { list ->
      list.toMutableSet()
    },
    { set ->
      set.toList()
    }
  )
}