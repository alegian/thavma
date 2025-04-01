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
import org.joml.Vector3f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import kotlin.math.abs
import kotlin.math.sign

private const val CELL_SIZE = 48f

fun renderGrid(nodes: List<Node>, guiGraphics: GuiGraphics) {
  guiGraphics.usePose {
    scaleXY(CELL_SIZE)
    for (node in nodes) {
      guiGraphics.usePose {
        translateToNode(node)
        renderNode(guiGraphics)
        for (child in node.children) {
          val dv = child.pos - node.pos
          guiGraphics.usePose {
            renderConnection(dv.x, dv.y, guiGraphics, child.preferX, false)
          }
        }
      }
    }
  }
  //renderDebug(guiGraphics)
}

fun renderConnection(dx: Float, dy: Float, guiGraphics: GuiGraphics, preferX: Boolean, invert: Boolean) {
  val absDx = abs(dx)
  val absDy = abs(dy)
  val signX = sign(dx)
  val signY = sign(dy)
  val inversion = if (invert) -1f else 1f
  val preference = if (preferX) -1f else 1f

  if (absDx <= 0f && absDy <= 0f) return
  else if (absDx > 2 && absDy > 2) throw IllegalStateException()
  else if (!invert && (preferX && absDx > absDy || !preferX && absDy > absDx)) {
    guiGraphics.pose().translateXY(dx, dy)
    renderConnection(-dx, -dy, guiGraphics, preferX, true)
  } else if (absDx == absDy) {
    guiGraphics.pose().translateXY(dx / 2, dy / 2)
    guiGraphics.pose().translateXY(preference * signX * inversion / 2, -preference * signY * inversion / 2)
    connectionCorner(guiGraphics, dx * inversion, dy * inversion, preferX)
  } else if (absDx > absDy) {
    guiGraphics.pose().translateXY(signX, 0)
    connectionLine(guiGraphics, signX * inversion, 1f, false)
    renderConnection(dx - signX, dy, guiGraphics, preferX, invert)
  } else {
    guiGraphics.pose().translateXY(0, signY)
    connectionLine(guiGraphics, 1f, signY * inversion, true)
    renderConnection(dx, dy - signY, guiGraphics, preferX, invert)
  }
}

private fun renderDebug(guiGraphics: GuiGraphics) {
  guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())

  for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * CELL_SIZE.toInt(), -0x1)

  for (i in -31..31) guiGraphics.vLine(i * CELL_SIZE.toInt(), -10000, 10000, -0x1)
}

private fun PoseStack.translateToNode(node: Node) = this.translateXY(node.pos.x, node.pos.y)

private fun renderNode(guiGraphics: GuiGraphics) =
  render(
    guiGraphics,
    1f,
    1f,
    T7Textures.Thaumonomicon.NODE.location,
    false
  )

fun connectionLine(guiGraphics: GuiGraphics, signX: Float, signY: Float, vertical: Boolean) =
  render(
    guiGraphics,
    signX,
    signY,
    T7Textures.Thaumonomicon.LINE.location,
    vertical
  )

fun connectionCorner(guiGraphics: GuiGraphics, dx: Float, dy: Float, preferX: Boolean) {
  val textureLoc =
    if (abs(dx) == 1f) T7Textures.Thaumonomicon.CORNER_1X1.location
    else T7Textures.Thaumonomicon.CORNER_2X2.location

  render(
    guiGraphics,
    dx,
    dy,
    textureLoc,
    preferX
  )
}

private fun render(graphics: GuiGraphics, width: Float, height: Float, textureLoc: ResourceLocation, flip: Boolean) {
  // allows negative size drawing, which greatly simplifies math
  RenderSystem.disableCull()
  graphics.usePose {
    if (flip) mulPose(Axis.of(Vector3f(sign(width), sign(height), 0f)).rotationDegrees(180f))
    scale(width, height, 1f)
    translateXY(-0.5f, -0.5f)
    graphics.blit(textureLoc, 0, 0, 0, 0f, 0f, 1, 1, 1, 1)
  }
  RenderSystem.enableCull()
}
