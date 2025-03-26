package me.alegian.thavma.impl.client.gui.thaumonomicon.widget

import me.alegian.thavma.impl.client.gui.thaumonomicon.grid.Grid
import me.alegian.thavma.impl.client.gui.thaumonomicon.grid.GridRenderable
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.Mth
import kotlin.math.pow

private const val ZOOM_MULTIPLIER = 1.25f

// represents the renderable content of a tab in the book
class Tab(private val maxScrollX: Float, private val maxScrollY: Float) : Renderable {
  var scrollX = 0.0
  var scrollY = 0.0
  private var zoom = 2f
  private val grid = Grid(48)

  init {
    // test research nodes
    grid.addCell(Node(0, 0))
    grid.addCell(Line(2, 2, 0))
    grid.addCell(Line(3, 2, 0))
    grid.addCell(Line(4, 2, 0))
    grid.addCell(ArrowCorner3x3(1, -1, true, 0))
    grid.addCell(ArrowHead(2, -1, 0))
    grid.addCell(Node(2, -2))
    grid.addCell(ArrowCorner1x1(3, -2, false, 0))
    grid.addCell(ArrowHead(3, -2, 0))
    grid.addCell(Node(3, -3))
  }

  fun handleScroll(x: Double, y: Double) {
    scrollTo(scrollX - ZOOM_MULTIPLIER.toDouble().pow(zoom.toDouble()) * x, scrollY - ZOOM_MULTIPLIER.toDouble().pow(zoom.toDouble()) * y)
  }

  fun scrollTo(x: Double, y: Double) {
    scrollX = Mth.clamp(x, (-1 * maxScrollX).toDouble(), maxScrollX.toDouble())
    scrollY = Mth.clamp(y, (-1 * maxScrollY).toDouble(), maxScrollY.toDouble())
  }

  fun zoom(y: Float) {
    zoom = Mth.clamp(zoom - y, 0f, 5f)
  }

  override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
    graphics.usePose {
      val screenHeight = graphics.guiHeight()
      val screenWidth = graphics.guiWidth()

      graphics.enableCrop(screenWidth / 128, screenHeight / 64)

      // background stars
      translateXY(screenWidth * 0.5, screenHeight * 0.5)
      scaleXY(ZOOM_MULTIPLIER.toDouble().pow(-zoom.toDouble()).toFloat())
      graphics.blit(
        T7Textures.Thaumonomicon.TAB_BG.location,
        -3840,
        -2160,
        0,
        scrollX.toFloat(),
        scrollY.toFloat(),
        3840 * 2,
        2160 * 2,
        512,
        512
      )

      // contains research nodes and their connections
      grid.render(graphics, mouseX, mouseY, tickDelta, scrollX, scrollY)

      graphics.disableCrop()
    }
  }

  private class Node(x: Int, y: Int) : GridRenderable(T7Textures.Thaumonomicon.NODE.location, x, y)

  private class ArrowHead(x: Int, y: Int, rotationDegrees: Int) : GridRenderable(T7Textures.Thaumonomicon.ARROW_HEAD.location, x, y, rotationDegrees)

  private class Line(x: Int, y: Int, rotationDegrees: Int) : GridRenderable(T7Textures.Thaumonomicon.LINE.location, x, y, rotationDegrees)

  private class ArrowCorner1x1(x: Int, y: Int, flip: Boolean, rotationDegrees: Int) : GridRenderable(T7Textures.Thaumonomicon.CORNER_1X1.location, x, y, flip, rotationDegrees)

  private class ArrowCorner3x3(x: Int, y: Int, flip: Boolean, rotationDegrees: Int) : GridRenderable(T7Textures.Thaumonomicon.CORNER_3X3.location, x, y, 3, 3, flip, rotationDegrees)
}
