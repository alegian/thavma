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
          renderConnection(dv.x, dv.y, guiGraphics, child.preferX, false)
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

  if (absDx <= 0f && absDy <= 0f) return
  else if (absDx > 2 && absDy > 2) throw IllegalStateException()
  else if (!invert && (preferX && absDx > absDy || !preferX && absDy > absDx)) {
    guiGraphics.pose().translateXY(dx, dy)
    renderConnection(-dx, -dy, guiGraphics, preferX, true)
  } else if (absDx == absDy) {
    val inversion = if(invert) -1 else 1
    if(invert) guiGraphics.pose().translateXY(dx, dy)
    connectionCorner(guiGraphics, dx*inversion, dy*inversion, preferX)
  } else if (absDx > 2) {
    connectionLine(guiGraphics, signX, signY, false)
    guiGraphics.pose().translateXY(signX, 0)
    renderConnection(dx - signX, dy, guiGraphics, preferX, invert)
  } else {
    connectionLine(guiGraphics, signX, signY, true)
    guiGraphics.pose().translateXY(0, signY)
    renderConnection(dx, dy - signY, guiGraphics, preferX, invert)
  }
}

private fun renderDebug(guiGraphics: GuiGraphics) {
  guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())

  for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * CELL_SIZE.toInt(), -0x1)

  for (i in -31..31) guiGraphics.vLine(i * CELL_SIZE.toInt(), -10000, 10000, -0x1)
}

private fun PoseStack.translateToNode(node: Node) = this.translateXY(node.pos.x, node.pos.y)

private fun renderNode(guiGraphics: GuiGraphics) {
  guiGraphics.usePose {
    translateXY(-0.5, -0.5)
    guiGraphics.blit(
      T7Textures.Thaumonomicon.NODE.location,
      0,
      0,
      0,
      0f,
      0f,
      1,
      1,
      1,
      1
    )
  }
}

fun connectionLine(guiGraphics: GuiGraphics, signX: Float, signY: Float, vertical: Boolean) {
  guiGraphics.usePose {
    if (vertical) mulPose(Axis.of(Vector3f(1f, -1f, 0f)).rotationDegrees(180f))
    render(
      guiGraphics,
      0f,
      -signY,
      signX,
      signY,
      T7Textures.Thaumonomicon.LINE.location
    )
  }
}

fun connectionCorner(guiGraphics: GuiGraphics, dx: Float, dy: Float, preferX: Boolean) {
  val textureLoc =
    if (abs(dx) == 1f) T7Textures.Thaumonomicon.CORNER_1X1.location
    else T7Textures.Thaumonomicon.CORNER_2X2.location

  guiGraphics.usePose {
    if (preferX) mulPose(Axis.of(Vector3f(1f, -1f, 0f)).rotationDegrees(180f))
    render(
      guiGraphics,
      0f,
      dy,
      dx,
      -dy,
      textureLoc
    )
  }
}

private fun render(graphics: GuiGraphics, x: Float, y: Float, width: Float, height: Float, textureLoc: ResourceLocation) {
  val xPos = x + 0.5 * sign(width)
  val yPos = y + 0.5 * sign(height)

  graphics.usePose {
    translateXY(xPos, yPos)
    // allows negative size drawing, which greatly simplifies math
    RenderSystem.disableCull()
    graphics.blit(
      textureLoc,
      0,
      0,
      0,
      0f,
      0f,
      width.toInt(),
      height.toInt(),
      width.toInt(),
      height.toInt()
    )
    RenderSystem.enableCull()
  }
}
