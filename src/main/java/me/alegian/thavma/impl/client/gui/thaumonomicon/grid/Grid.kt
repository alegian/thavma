package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.Node
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
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
          renderConnection(child, node, guiGraphics)
        }
      }
    }
  }
  renderDebug(guiGraphics)
}

fun renderConnection(to: Node, from: Node, guiGraphics: GuiGraphics) {
  val diffX = abs(to.x - from.x)
  val diffY = abs(to.y - from.y)
  if (diffX > 2 && diffY > 2) throw IllegalStateException()
  if (diffX > 2) {
    connectionLine(guiGraphics, from.x + 1f, from.y.toFloat(), 0)
  } else if (diffY > 2) {
    connectionLine(guiGraphics, from.x.toFloat(), from.y + 1f, 90)
  } else if (diffY == 2 && diffX == 2) {
    connectionCorner2x2(guiGraphics, to, from)
  } else if (diffY == 2) {
    connectionLine(guiGraphics, from.x.toFloat(), from.y + 1f, 90)
  } else if (diffX == 2) {
    connectionLine(guiGraphics, from.x + 1f, from.y.toFloat(), 0)
  } else {
    connectionCorner1x1(guiGraphics, to, from)
  }
}

private fun renderDebug(guiGraphics: GuiGraphics) {
  guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())

  for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * CELL_SIZE.toInt(), -0x1)

  for (i in -31..31) guiGraphics.vLine(i * CELL_SIZE.toInt(), -10000, 10000, -0x1)
}

private fun PoseStack.translateToNode(node: Node) = this.translateXY(node.x, node.y)

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

fun arrowHead(guiGraphics: GuiGraphics, to: Node, from: Node, flip: Boolean) {
  val flipFactor = if (flip) -1f else 1f
  render(
    guiGraphics,
    (to.x + from.x) / 2f + flipFactor,
    (to.y + from.y) / 2f,
    90,
    flipFactor,
    flipFactor,
    T7Textures.Thaumonomicon.ARROW_HEAD.location
  )
}

fun connectionLine(guiGraphics: GuiGraphics, x: Float, y: Float, rotationDegrees: Int) {
  render(
    guiGraphics,
    x,
    y,
    rotationDegrees,
    1f,
    1f,
    T7Textures.Thaumonomicon.LINE.location
  )
}

fun connectionCorner1x1(guiGraphics: GuiGraphics, to: Node, from: Node) =
  connectionCorner(guiGraphics, to, from, T7Textures.Thaumonomicon.CORNER_1X1.location)

fun connectionCorner2x2(guiGraphics: GuiGraphics, to: Node, from: Node) =
  connectionCorner(guiGraphics, to, from, T7Textures.Thaumonomicon.CORNER_2X2.location)

/**
 * connectX(default): source X, target Y, width: taget-source, height: source-target
 * connectY: target X, source Y, width: source-taget, height: target-source
 */
fun connectionCorner(guiGraphics: GuiGraphics, to: Node, from: Node, textureLoc: ResourceLocation) {
  if (to.preferX)
    render(
      guiGraphics,
      (to.x - from.x).toFloat(),
      0f,
      0,
      (from.x - to.x).toFloat(),
      (to.y - from.y).toFloat(),
      textureLoc
    )
  else
    render(
      guiGraphics,
      0f,
      (to.y - from.y).toFloat(),
      0,
      (to.x - from.x).toFloat(),
      (from.y - to.y).toFloat(),
      textureLoc
    )
}

private fun render(graphics: GuiGraphics, x: Float, y: Float, rotationDegrees: Int, width: Float, height: Float, textureLoc: ResourceLocation) {
  val xPos = x + 0.5 * sign(width)
  val yPos = y + 0.5 * sign(height)

  graphics.usePose {
    rotateZ(rotationDegrees.toFloat())
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
