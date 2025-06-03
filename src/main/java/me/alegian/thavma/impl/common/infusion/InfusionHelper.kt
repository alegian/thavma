package me.alegian.thavma.impl.common.infusion

import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.minus
import kotlin.math.roundToInt

// number of trajectory points per block length
const val MAIN_AXIS_RESOLUTION = 32

fun trajectoryLength(start: Vec3, end: Vec3): Int {
  val diff = end - start
  return (diff.length() * MAIN_AXIS_RESOLUTION).roundToInt()
}
