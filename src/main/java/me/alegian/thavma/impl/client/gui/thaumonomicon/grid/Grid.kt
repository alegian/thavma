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
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
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
          renderConnection(child.pos, node.pos, guiGraphics, child.preferX)
        }
      }
    }
  }
  renderDebug(guiGraphics)
}

fun renderConnection(to: Vec2, from: Vec2, guiGraphics: GuiGraphics, preferX: Boolean) {
  val diffX = abs(to.x - from.x)
  val diffY = abs(to.y - from.y)
  val signX = sign(to.x - from.x)
  val signY = sign(to.y - from.y)

  if (diffX > 2 && diffY > 2) throw IllegalStateException()
  if (diffX > 2) {
    connectionLine(guiGraphics, signX, signY, preferX)
    guiGraphics.pose().translateXY(signX, 0)
    renderConnection(to, from.plus(Vec2(signX, 0f)), guiGraphics, preferX)
  } else if (diffY > 2) {
    connectionLine(guiGraphics, signX, signY, preferX)
    guiGraphics.pose().translateXY(0, signY)
    renderConnection(to, from.plus(Vec2(0f, signY)), guiGraphics, preferX)
  } else if (diffY == 2f && diffX == 2f) {
    connectionCorner2x2(guiGraphics, to, from, preferX)
  } else if (diffX == 1f && diffY == 1f) {
    connectionCorner1x1(guiGraphics, to, from, preferX)
  } else if (diffY > diffX) {
    connectionLine(guiGraphics, signX, signY, preferX)
    guiGraphics.pose().translateXY(0, signY)
    renderConnection(to, from.plus(Vec2(0f, signY)), guiGraphics, preferX)
  } else {
    connectionLine(guiGraphics, signX, signY, preferX)
    guiGraphics.pose().translateXY(signX, 0)
    renderConnection(to, from.plus(Vec2(signX, 0f)), guiGraphics, preferX)
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

fun connectionLine(guiGraphics: GuiGraphics, width: Float, height: Float, preferX: Boolean) {
  guiGraphics.usePose {
    if (preferX) mulPose(Axis.of(Vector3f(1f, -1f, 0f)).rotationDegrees(180f))
    render(
      guiGraphics,
      0f,
      -height,
      width,
      height,
      T7Textures.Thaumonomicon.LINE.location
    )
  }
}

fun connectionCorner1x1(guiGraphics: GuiGraphics, to: Vec2, from: Vec2, preferX: Boolean) =
  connectionCorner(guiGraphics, to, from, T7Textures.Thaumonomicon.CORNER_1X1.location, preferX)

fun connectionCorner2x2(guiGraphics: GuiGraphics, to: Vec2, from: Vec2, preferX: Boolean) =
  connectionCorner(guiGraphics, to, from, T7Textures.Thaumonomicon.CORNER_2X2.location, preferX)

/**
 * connectX(default): source X, target Y, width: taget-source, height: source-target
 * connectY: target X, source Y, width: source-taget, height: target-source
 */
fun connectionCorner(guiGraphics: GuiGraphics, to: Vec2, from: Vec2, textureLoc: ResourceLocation, preferX: Boolean) {
  val width = to.x - from.x
  val height = from.y - to.y

  guiGraphics.usePose {
    if (preferX) mulPose(Axis.of(Vector3f(1f, -1f, 0f)).rotationDegrees(180f))
    render(
      guiGraphics,
      0f,
      -height,
      width,
      height,
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
