package me.alegian.thavma.impl.common.util

import org.joml.Vector2i

operator fun Vector2i.plusAssign(other: Vector2i) {
  add(other)
}

operator fun Vector2i.minusAssign(other: Vector2i) {
  sub(other)
}