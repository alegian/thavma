package me.alegian.thavma.impl.common.util

import net.minecraft.world.phys.Vec3

infix fun Vec3.cross(other: Vec3) = this.cross(other)
operator fun Vec3.unaryMinus() = scale(-1.0)
operator fun Vec3.minus(x: Number) = Vec3(this.x - x.toDouble(), this.y - x.toDouble(), this.z - x.toDouble())
operator fun Vec3.minus(other: Vec3) = add(-other)
operator fun Vec3.plus(x: Number) = Vec3(this.x + x.toDouble(), this.y + x.toDouble(), this.z + x.toDouble())
operator fun Vec3.plus(other: Vec3) = add(other)
operator fun Vec3.times(x: Number) = scale(x.toDouble())
operator fun Vec3.times(other: Vec3) = Vec3(this.x * other.x, this.y * other.y, this.z * other.z)
operator fun Vec3.div(x: Number) = scale(1 / x.toDouble())
operator fun Vec3.div(other: Vec3) = Vec3(this.x / other.x, this.y / other.y, this.z / other.z)
