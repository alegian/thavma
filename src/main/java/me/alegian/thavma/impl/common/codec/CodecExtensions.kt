package me.alegian.thavma.impl.common.codec

import com.mojang.serialization.Codec
import java.util.*
import kotlin.jvm.optionals.getOrNull

fun <T> Codec<T>.mutableSetOf(): Codec<MutableSet<T>> {
  return listOf().xmap(
    { it.toMutableSet() },
    { it.toList() }
  )
}

fun <T> Codec<T>.optional(name: String) = optionalFieldOf(name).codec()

fun <T : Any> Codec<T>.nullable(name: String) = optional(name).xmap({ it.getOrNull() }, { Optional.ofNullable(it) })

// codec.nullable().listOf() doesnt work because of hardcoded null check deep inside listcodec
fun <T : Any> Codec<T>.listOfNullable(name: String) = optional(name).listOf().xmap({ it.map { it.getOrNull() } }, { it.map { Optional.ofNullable(it) } })