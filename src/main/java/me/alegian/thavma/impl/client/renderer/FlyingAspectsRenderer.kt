package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.T7RenderTypes
import me.alegian.thavma.impl.client.util.addVertex
import me.alegian.thavma.impl.common.infusion.MAIN_AXIS_RESOLUTION
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.util.cross
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.Mth.smoothstep
import net.minecraft.world.phys.Vec3
import me.alegian.thavma.impl.common.util.plus
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.times
import me.alegian.thavma.impl.common.util.div
import kotlin.math.*

// number of "corners" of every 3d cylinder slice
const val CR0SS_AXIS_RESOLUTION = 16


// the range at which a trajectory point is considered an endpoint
const val ENDPOINT_RANGE = 0.3

// height is how high the "inverse gravity" parabola will rise
private fun trajectory(start: Vec3, end: Vec3, height: Double): List<Vec3> {
  val diff = end - start
  val dl = diff.normalize() / MAIN_AXIS_RESOLUTION.toDouble()
  val trajectoryLength = trajectoryLength(start, end)
  return (0..trajectoryLength).map {
    val t = it.toDouble() / trajectoryLength // t ranges from 0 to 1
    val quadraticYOffset = Vec3(0.0, 1.0, 0.0) * t * (1 - t) * 4.0 * height
    start + dl * it.toDouble() + quadraticYOffset
  }
}

fun renderFlyingAspects(start: Vec3, end: Vec3, trajectoryHeight: Double, headIndex: Int, length: Int, poseStack: PoseStack, multiBufferSource: MultiBufferSource, ticks: Float, color: Int, radius: Double) {
  val vc = multiBufferSource.getBuffer(T7RenderTypes.FLYING_ASPECTS)
  val traj = trajectory(start, end, trajectoryHeight)

  renderVariableRadiusCylinder(traj.subList(max(0, headIndex - length), headIndex + 1), vc, poseStack, ticks, color, radius)
}

private fun renderVariableRadiusCylinder(trajectory: List<Vec3>, vc: VertexConsumer, poseStack: PoseStack, ticks: Float, color: Int, baseRadius: Double) {
  // we keep track of the previous normals to fix open ends in the cylinder, in non-linear trajectories
  var prevNormal1 = Vec3(0.0, 1.0, 0.0)
  var prevNormal2 = Vec3(1.0, 0.0, 0.0)

  for (i in 0 until trajectory.size - 1) {
    val currentPoint = trajectory[i]
    val nextPoint = trajectory[i + 1]

    val direction = nextPoint - currentPoint
    val randomOtherDirection = direction - Vec3(0.0, 1.0, 0.0)
    val normal1 = (direction cross randomOtherDirection).normalize()
    val normal2 = (direction cross normal1).normalize()

    val radius = oscillatingRadius(i, trajectory, ticks, baseRadius)
    val nextRadius = oscillatingRadius(i + 1, trajectory, ticks, baseRadius)

    for (j in 0..CR0SS_AXIS_RESOLUTION - 1) {
      val angle = 2 * PI * j / CR0SS_AXIS_RESOLUTION
      val nextAngle = 2 * PI * (j + 1) / CR0SS_AXIS_RESOLUTION

      val offset1 = prevNormal1 * cos(angle) + prevNormal2 * sin(angle)
      val offset2 = prevNormal1 * cos(nextAngle) + prevNormal2 * sin(nextAngle)
      val offset3 = normal1 * cos(nextAngle) + normal2 * sin(nextAngle)
      val offset4 = normal1 * cos(angle) + normal2 * sin(angle)

      // the first vertices use the previous normals, to avoid open ends
      vc.addVertex(poseStack, currentPoint + offset1 * radius).setColor(color)
      vc.addVertex(poseStack, currentPoint + offset2 * radius).setColor(color)
      vc.addVertex(poseStack, nextPoint + offset3 * nextRadius).setColor(color)
      vc.addVertex(poseStack, nextPoint + offset4 * nextRadius).setColor(color)
    }

    prevNormal1 = normal1
    prevNormal2 = normal2
  }
}

/**
 * Calculates the radius of the cylinder at the current point in the trajectory.
 * Endpoint radii are multiplied with an extra term to avoid open ends.
 */
private fun oscillatingRadius(i: Int, trajectory: List<Vec3>, ticks: Float, scale: Double): Double {
  val timePhase = -1.5 * 2 * PI * ticks / 20 // minus makes it look like start is being sucked into end
  val default = scale + 0.02 * sin(i * 2 * PI / MAIN_AXIS_RESOLUTION + timePhase)

  val x = trajectory[i]
  val distanceToEndpoint = min(x.distanceTo(trajectory.first()), x.distanceTo(trajectory.last()))
  val linearFade = (distanceToEndpoint / ENDPOINT_RANGE).coerceIn(0.0, 1.0)
  if (distanceToEndpoint < 0.3) return default * smoothstep(linearFade)
  return default
}
