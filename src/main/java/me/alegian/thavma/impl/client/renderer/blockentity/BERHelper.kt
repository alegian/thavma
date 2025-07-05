package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.T7BufferBuilder
import me.alegian.thavma.impl.client.T7RenderTypes
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.FastColor
import kotlin.math.cos
import kotlin.math.sin

fun renderAuraNodeLayer(
  poseStack: PoseStack,
  bufferSource: MultiBufferSource,
  packedColor: Int,
  a: Float,
  scale: Float
) {
  val buffer = T7BufferBuilder(bufferSource.getBuffer(T7RenderTypes.AURA_NODE))

  // circles inscribed in triangles are only half as small as their circumscribed circles
  val radius = 1f

  val r = FastColor.ARGB32.red(packedColor) / 255f
  val g = FastColor.ARGB32.green(packedColor) / 255f
  val b = FastColor.ARGB32.blue(packedColor) / 255f

  // render a single triangle. everything else is core shaders
  val baseAngle = 2 * Math.PI / 3
  nodeVertex(poseStack, radius, 0f, r, g, b, a, buffer, scale)
  nodeVertex(
    poseStack,
    (cos(baseAngle) * radius).toFloat(),
    (sin(baseAngle) * radius).toFloat(),
    r,
    g,
    b,
    a,
    buffer,
    scale
  )
  nodeVertex(
    poseStack,
    (cos(baseAngle * 2) * radius).toFloat(),
    (sin(baseAngle * 2) * radius).toFloat(),
    r,
    g,
    b,
    a,
    buffer,
    scale
  )
}

private fun nodeVertex(
  poseStack: PoseStack,
  x: Float,
  y: Float,
  r: Float,
  g: Float,
  b: Float,
  a: Float,
  buffer: T7BufferBuilder,
  scale: Float
) {
  buffer.addVertex(poseStack.last(), x, y, 0f).setColor(r, g, b, a).setCenter().setScale(scale)
}

