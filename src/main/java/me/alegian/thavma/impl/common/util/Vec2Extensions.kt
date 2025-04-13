package me.alegian.thavma.impl.common.util

import net.minecraft.world.phys.Vec2

fun Vec2.trunc() = Vec2(x.toInt().toFloat(), y.toInt().toFloat())

fun vec2(x: Number, y: Number) = Vec2(x.toFloat(), y.toFloat())

operator fun Vec2.minus(x: Number) = Vec2(this.x - x.toFloat(), this.y - x.toFloat())
operator fun Vec2.plus(x: Number) = Vec2(this.x + x.toFloat(), this.y + x.toFloat())
