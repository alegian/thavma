package me.alegian.thavma.impl.common.util

import org.joml.Vector2i

operator fun Vector2i.minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)