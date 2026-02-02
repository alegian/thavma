package me.alegian.thavma.impl.common.util

import org.joml.Vector3f

operator fun Vector3f.minus(other: Vector3f) = this.sub(other)
operator fun Vector3f.plus(other: Vector3f) = this.add(other)
