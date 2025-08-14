package me.alegian.thavma.impl.common.util

import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3

fun Vec3i.toVec3() = Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())