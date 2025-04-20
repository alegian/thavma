package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.sign

val CELL_SIZE = EntryWidget.TEXTURE.width
val ARROW_HEAD = Texture("gui/book/arrow_head", 32, 32)
val LINE = Texture("gui/book/line", 32, 32)
val CORNER_1X1 = Texture("gui/book/corner_1x1", 32, 32)
val CORNER_2X2 = Texture("gui/book/corner_2x2", 96, 96)

/**
 * Renders a connection between two nodes.
 * Assumes initial position at source node, and dx and dy
 * point to the destination, relative to source.
 * Respects destination's orientation preference (to connect
 * along x or y axis)
 * Uses recursion to draw long connections.
 */
fun PoseStack.renderConnectionRecursive(dx: Int, dy: Int, guiGraphics: GuiGraphics, preferX: Boolean, invert: Boolean) {
  val absDx = abs(dx)
  val absDy = abs(dy)
  val signX = dx.sign
  val signY = dy.sign
  val inversion = if (invert) -1f else 1f
  val preference = if (preferX) -1f else 1f

  if (absDx <= 0f && absDy <= 0f) return
  else if (absDx > 2 && absDy > 2) throw IllegalStateException()
  else if (!invert && (preferX && absDx > absDy || !preferX && absDy > absDx)) {
    translateXY(dx, dy)
    renderConnectionRecursive(-dx, -dy, guiGraphics, preferX, true)
  } else if (absDx == absDy) {
    translateXY(dx / 2f, dy / 2f)
    translateXY(preference * signX * inversion / 2f, -preference * signY * inversion / 2f)
    renderCorner(guiGraphics, dx * inversion, dy * inversion, preferX)
  } else if (absDx > absDy) {
    translateXY(signX, 0)
    renderLine(guiGraphics, signX * inversion, 1f, false)
    renderConnectionRecursive(dx - signX, dy, guiGraphics, preferX, invert)
  } else {
    translateXY(0, signY)
    renderLine(guiGraphics, 1f, signY * inversion, true)
    renderConnectionRecursive(dx, dy - signY, guiGraphics, preferX, invert)
  }
}

/**
 * Only one texture is ever used.
 * Vertical lines are reflected horizontals.
 * Uses negative dimensions for orientation.
 */
private fun renderLine(guiGraphics: GuiGraphics, signX: Float, signY: Float, vertical: Boolean) =
  renderGridElement(
    guiGraphics,
    signX,
    signY,
    LINE.location,
    vertical
  )

/**
 * Only 2x2 and 1x1 corners are supported. Only one texture
 * is ever used. Uses negative dimensions & reflections for orientation.
 */
private fun renderCorner(guiGraphics: GuiGraphics, dx: Float, dy: Float, reflect: Boolean) {
  val textureLoc =
    if (abs(dx) == 1f) CORNER_1X1.location
    else CORNER_2X2.location

  renderGridElement(
    guiGraphics,
    dx,
    dy,
    textureLoc,
    reflect
  )
}

/**
 * Base renderer, used by many grid elements.
 * The pose is assumed to be at the center of the object.
 * RenderSystem culling is assumed to be disabled.
 * The reflect param reflects the object across the y=x axis.
 * It supports negative size drawing, and adjusts the reflection axis accordingly
 */
fun renderGridElement(graphics: GuiGraphics, width: Float, height: Float, textureLoc: ResourceLocation, reflect: Boolean) {
  graphics.usePose {
    if (reflect) mulPose(Axis.of(Vector3f(sign(width), sign(height), 0f)).rotationDegrees(180f))
    scale(width, height, 1f)
    translateXY(-0.5f, -0.5f)
    graphics.blit(textureLoc, 0, 0, 0, 0f, 0f, 1, 1, 1, 1)
  }
}
