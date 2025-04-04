package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.Node
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import org.joml.Vector3f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import kotlin.math.abs
import kotlin.math.sign

private const val CELL_SIZE = 48f

fun renderGrid(nodes: List<Node>, guiGraphics: GuiGraphics) {
  // allows negative size drawing, which greatly simplifies math
  RenderSystem.disableCull()
  guiGraphics.usePose {
    scaleXY(CELL_SIZE)
    for (node in nodes) {
      guiGraphics.usePose {
        translateToNode(node)
        renderNode(guiGraphics)
        for (child in node.children) {
          val dv = child.pos - node.pos
          guiGraphics.usePose {
            renderConnectionRecursive(dv.x, dv.y, guiGraphics, child.preferX, false)
          }
        }
      }
    }
  }
  RenderSystem.enableCull()
}

/**
 * Renders a connection between two nodes.
 * Assumes initial position at source node, and dx and dy
 * point to the destination, relative to source.
 * Respects destination's orientation preference (to connect
 * along x or y axis)
 * Uses recursion to draw long connections.
 */
private fun PoseStack.renderConnectionRecursive(dx: Float, dy: Float, guiGraphics: GuiGraphics, preferX: Boolean, invert: Boolean) {
  val absDx = abs(dx)
  val absDy = abs(dy)
  val signX = sign(dx)
  val signY = sign(dy)
  val inversion = if (invert) -1f else 1f
  val preference = if (preferX) -1f else 1f

  if (absDx <= 0f && absDy <= 0f) return
  else if (absDx > 2 && absDy > 2) throw IllegalStateException()
  else if (!invert && (preferX && absDx > absDy || !preferX && absDy > absDx)) {
    translateXY(dx, dy)
    renderConnectionRecursive(-dx, -dy, guiGraphics, preferX, true)
  } else if (absDx == absDy) {
    translateXY(dx / 2, dy / 2)
    translateXY(preference * signX * inversion / 2, -preference * signY * inversion / 2)
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
  render(
    guiGraphics,
    signX,
    signY,
    T7Textures.Thaumonomicon.LINE.location,
    vertical
  )

/**
 * Only 2x2 and 1x1 corners are supported. Only one texture
 * is ever used. Uses negative dimensions & reflections for orientation.
 */
private fun renderCorner(guiGraphics: GuiGraphics, dx: Float, dy: Float, reflect: Boolean) {
  val textureLoc =
    if (abs(dx) == 1f) T7Textures.Thaumonomicon.CORNER_1X1.location
    else T7Textures.Thaumonomicon.CORNER_2X2.location

  render(
    guiGraphics,
    dx,
    dy,
    textureLoc,
    reflect
  )
}

/**
 * Renders a book entry node.
 * Constant size, does not need negative dimensions,
 * nor reflections.
 */
private fun renderNode(guiGraphics: GuiGraphics) {
  render(
    guiGraphics,
    1f,
    1f,
    T7Textures.Thaumonomicon.NODE.location,
    false
  )

  renderItem(guiGraphics)
}

/**
 * Base renderer, used by all others in this file.
 * The pose is assumed to be at the center of the object.
 * RenderSystem culling is assumed to be disabled.
 * The reflect param reflects the object across the y=x axis.
 * It supports negative size drawing, and adjusts the reflection axis accordingly
 */
private fun render(graphics: GuiGraphics, width: Float, height: Float, textureLoc: ResourceLocation, reflect: Boolean) {
  graphics.usePose {
    if (reflect) mulPose(Axis.of(Vector3f(sign(width), sign(height), 0f)).rotationDegrees(180f))
    scale(width, height, 1f)
    translateXY(-0.5f, -0.5f)
    graphics.blit(textureLoc, 0, 0, 0, 0f, 0f, 1, 1, 1, 1)
  }
}

private fun renderItem(guiGraphics: GuiGraphics) =
  guiGraphics.usePose {
    scaleXY(1 / CELL_SIZE) // back to pixel space
    scaleXY(2f) // items are 16x, nodes are 32x
    guiGraphics.renderItem(Items.DIAMOND.defaultInstance, -8, -8)
  }

private fun PoseStack.translateToNode(node: Node) = translateXY(node.pos.x, node.pos.y)
