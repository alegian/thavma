package me.alegian.thavma.impl.common.util

import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

operator fun Vector3f.minus(other: Vector3f) = this.sub(other)
operator fun Vector3f.plus(other: Vector3f) = this.add(other)

fun Vector3f.toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
