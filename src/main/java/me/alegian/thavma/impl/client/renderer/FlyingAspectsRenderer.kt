package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Minecraft
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

private fun trajectory(start: Vec3, end: Vec3, height: Double, ticks: Float): List<Vec3> {
  val diff = end - start
  val length = diff.length()
  if (length < 1.0E-4) return listOf(start, end)

  val trajLen = trajectoryLength(start, end).coerceAtLeast(1)

  val forward = diff.normalize()
  val tempUp = if (abs(forward.y) > 0.9) Vec3(1.0, 0.0, 0.0) else Vec3(0.0, 1.0, 0.0)
  val right = (forward cross tempUp).normalize()
  val up = (right cross forward).normalize()

  val seed = abs(start.x * 0.731 + start.y * 0.293 + start.z * 0.517)
  val displacementScale = min(length * 0.01, 0.035)

  return (0..trajLen).map { idx ->
    val t = idx.toDouble() / trajLen
    val base = start + diff * t
    val heightOffset = Vec3(0.0, 1.0, 0.0) * t * (1 - t) * 4.0 * height

    val fade = sin(t * PI).pow(1.15)
    val time = ticks.toDouble() * 0.006

    val dx = (sin(t * PI * 1.15 + seed + time) * 0.68 +
      sin(t * PI * 0.55 + seed * 1.7 - time * 0.4) * 0.32) * fade * displacementScale
    val dy = cos(t * PI * 0.9 + seed * 0.9 + time * 0.45) * fade * displacementScale * 0.2

    base + heightOffset + right * dx + up * dy
  }
}

fun renderFlyingAspects(start: Vec3, end: Vec3, trajectoryHeight: Double, headIndex: Int, length: Int, poseStack: PoseStack, multiBufferSource: MultiBufferSource, ticks: Float, colors: List<Int>, radius: Double) {
  val vc = multiBufferSource.getBuffer(T7RenderTypes.FLYING_ASPECTS)
  val traj = trajectory(start, end, trajectoryHeight, ticks)
  val sub = traj.subList(max(0, headIndex - length), headIndex + 1)
  if (sub.size < 2) return

  renderGradientRibbon(sub, vc, poseStack, ticks, colors, radius)

  val coreColors = colors.map {
    val rgb = brightenRGB(it and 0x00FFFFFF, 0.6f)
    val a = min(((it ushr 24) and 0xFF) * 5 / 4, 255)
    (a shl 24) or rgb
  }
  renderGradientRibbon(sub, vc, poseStack, ticks, coreColors, radius * 0.35)
}

private fun renderGradientRibbon(trajectory: List<Vec3>, vc: VertexConsumer, poseStack: PoseStack, ticks: Float, colors: List<Int>, baseRadius: Double) {
  var prevSide = Vec3(1.0, 0.0, 0.0)
  val segCount = trajectory.size - 1
  val cameraPos = Minecraft.getInstance().gameRenderer.mainCamera.position

  for (i in 0 until segCount) {
    val currentPoint = trajectory[i]
    val nextPoint = trajectory[i + 1]

    val direction = nextPoint - currentPoint
    val forward = direction.normalize()

    val toCurrentCamera = cameraPos - currentPoint
    var currentSide = if (toCurrentCamera.lengthSqr() < 1.0E-6) prevSide else forward cross toCurrentCamera.normalize()
    if (currentSide.lengthSqr() < 1.0E-6) currentSide = prevSide
    else currentSide = currentSide.normalize()
    if (currentSide.dot(prevSide) < 0) currentSide = currentSide.scale(-1.0)

    val toNextCamera = cameraPos - nextPoint
    var nextSide = if (toNextCamera.lengthSqr() < 1.0E-6) currentSide else forward cross toNextCamera.normalize()
    if (nextSide.lengthSqr() < 1.0E-6) nextSide = currentSide
    else nextSide = nextSide.normalize()
    if (nextSide.dot(currentSide) < 0) nextSide = nextSide.scale(-1.0)

    val radius = oscillatingRadius(i, trajectory, ticks, baseRadius)
    val nextRadius = oscillatingRadius(i + 1, trajectory, ticks, baseRadius)

    val t = i.toFloat() / segCount
    val tNext = (i + 1).toFloat() / segCount

    val color1 = modulateAlpha(sampleColor(colors, t, ticks * 0.0015f), smoothstep(t.toDouble()))
    val color2 = modulateAlpha(sampleColor(colors, tNext, ticks * 0.0015f), smoothstep(tNext.toDouble()))

    vc.addVertex(poseStack, currentPoint - currentSide * radius).setColor(color1)
    vc.addVertex(poseStack, currentPoint + currentSide * radius).setColor(color1)
    vc.addVertex(poseStack, nextPoint + nextSide * nextRadius).setColor(color2)
    vc.addVertex(poseStack, nextPoint - nextSide * nextRadius).setColor(color2)

    prevSide = (currentSide * 0.35 + nextSide * 0.65).normalize()
  }
}

/**
 * Calculates the radius of the cylinder at the current point in the trajectory.
 * Endpoint radii are multiplied with an extra term to avoid open ends.
 */
private fun oscillatingRadius(i: Int, trajectory: List<Vec3>, ticks: Float, scale: Double): Double {
  val x = trajectory[i]
  val distanceToEndpoint = min(x.distanceTo(trajectory.first()), x.distanceTo(trajectory.last()))
  val linearFade = (distanceToEndpoint / ENDPOINT_RANGE).coerceIn(0.0, 1.0)
  val endpointFade = if (distanceToEndpoint < ENDPOINT_RANGE) smoothstep(linearFade) else 1.0
  val timePhase = ticks * 0.008f + i * 0.012f
  val breathing = 0.985 + 0.015 * sin(timePhase)
  return scale * endpointFade * breathing
}

private fun sampleColor(colors: List<Int>, t: Float, timeOffset: Float): Int {
  if (colors.size == 1) return colors[0]
  val n = colors.size
  val phase = t * n + timeOffset
  val idx = floor(phase).toInt().mod(n)
  val nextIdx = (idx + 1).mod(n)
  val blend = phase - floor(phase)
  return lerpColor(colors[idx], colors[nextIdx], blend)
}

private fun modulateAlpha(color: Int, alphaMul: Double): Int {
  val a = (color ushr 24) and 0xFF
  val modA = (a * (0.35 + 0.65 * alphaMul)).toInt().coerceIn(0, 255)
  return (modA shl 24) or (color and 0x00FFFFFF)
}

private fun lerpColor(c1: Int, c2: Int, t: Float): Int {
  val a = (((c1 ushr 24) and 0xFF) + (((c2 ushr 24) and 0xFF) - ((c1 ushr 24) and 0xFF)) * t).toInt().coerceIn(0, 255)
  val r = (((c1 shr 16) and 0xFF) + (((c2 shr 16) and 0xFF) - ((c1 shr 16) and 0xFF)) * t).toInt().coerceIn(0, 255)
  val g = (((c1 shr 8) and 0xFF) + (((c2 shr 8) and 0xFF) - ((c1 shr 8) and 0xFF)) * t).toInt().coerceIn(0, 255)
  val b = ((c1 and 0xFF) + ((c2 and 0xFF) - (c1 and 0xFF)) * t).toInt().coerceIn(0, 255)
  return (a shl 24) or (r shl 16) or (g shl 8) or b
}

private fun brightenRGB(rgb: Int, factor: Float): Int {
  val r = (rgb shr 16) and 0xFF
  val g = (rgb shr 8) and 0xFF
  val b = rgb and 0xFF
  return ((r + (255 - r) * factor).toInt().coerceIn(0, 255) shl 16) or
    ((g + (255 - g) * factor).toInt().coerceIn(0, 255) shl 8) or
    (b + (255 - b) * factor).toInt().coerceIn(0, 255)
}
