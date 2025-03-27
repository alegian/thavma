package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.Node
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import kotlin.math.abs

private const val CELL_SIZE = 48

fun renderGrid(nodes: List<Node>, guiGraphics: GuiGraphics) {
  for (node in nodes) {
    renderNode(guiGraphics, node)
    for (child in node.children) {
      renderConnection(child, node, guiGraphics)
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
    connectionCorner2x2(guiGraphics, to, from, false, 0)
  } else if (diffY == 2) {
    connectionLine(guiGraphics, from.x.toFloat(), from.y + 1f, 90)
  } else if (diffX == 2) {
    connectionLine(guiGraphics, from.x + 1f, from.y.toFloat(), 0)
  } else {
    connectionCorner1x1(guiGraphics, to, from, false, 0)
  }
}

private fun renderDebug(guiGraphics: GuiGraphics) {
  guiGraphics.usePose {
    guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())

    for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * CELL_SIZE, -0x1)

    for (i in -31..31) guiGraphics.vLine(i * CELL_SIZE, -10000, 10000, -0x1)
  }
}

private fun renderNode(guiGraphics: GuiGraphics, node: Node) {
  val xPos = (CELL_SIZE * (node.x - 0.5f)).toDouble()
  val yPos = (CELL_SIZE * (node.y - 0.5f)).toDouble()

  guiGraphics.usePose {
    translateXY(xPos, yPos)
    guiGraphics.blit(
      T7Textures.Thaumonomicon.NODE.location,
      0,
      0,
      0,
      0f,
      0f,
      CELL_SIZE,
      CELL_SIZE,
      CELL_SIZE,
      CELL_SIZE
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
    T7Textures.Thaumonomicon.LINE.location
  )
}

fun connectionCorner1x1(guiGraphics: GuiGraphics, to: Node, from: Node, flip: Boolean, rotationDegrees: Int) {
  val flipFactor = if (flip) -1f else 1f

  render(
    guiGraphics,
    (to.x + from.x + flipFactor) / 2f,
    (to.y + from.y + flipFactor) / 2f,
    rotationDegrees,
    flipFactor,
    T7Textures.Thaumonomicon.CORNER_1X1.location
  )
}

fun connectionCorner2x2(guiGraphics: GuiGraphics, to: Node, from: Node, flip: Boolean, rotationDegrees: Int) {
  val flipFactor = if (flip) -1f else 1f

  render(
    guiGraphics,
    (to.x + from.x + flipFactor) / 2f,
    (to.y + from.y + flipFactor) / 2f,
    rotationDegrees,
    2* flipFactor,
    T7Textures.Thaumonomicon.CORNER_2X2.location
  )
}

private fun render(graphics: GuiGraphics, x: Float, y: Float, rotationDegrees: Int, size: Float, textureLoc: ResourceLocation) {
  val xPos = (CELL_SIZE * (x - size / 2f)).toDouble()
  val yPos = (CELL_SIZE * (y - size / 2f)).toDouble()

  graphics.usePose {
    rotateZ(rotationDegrees.toFloat())
    translateXY(xPos, yPos)
    graphics.blit(
      textureLoc,
      0,
      0,
      0,
      0f,
      0f,
      (CELL_SIZE * size).toInt(),
      (CELL_SIZE * size).toInt(),
      (CELL_SIZE * size).toInt(),
      (CELL_SIZE * size).toInt()
    )
  }
}
