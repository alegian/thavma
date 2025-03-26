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
    val node1 = Node(0f, 0f)
    val node2 = Node(2f, -2f)
    val node3 = Node(3f, -3f)
    grid.add(node1)
    grid.add(node2)
    grid.add(connectionCorner2x2(node2, node1, true, 0))
    grid.add(ArrowHead(node2, node1, false))
    grid.add(connectionCorner1x1(node3, node2, true, 0))
    grid.add(ArrowHead(node3, node2, true))
    grid.add(Node(3f, -3f))
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

  private class Node(x: Float, y: Float) : GridRenderable(T7Textures.Thaumonomicon.NODE.location, x, y)

  private fun ArrowHead(to: Node, from: Node, flip: Boolean): GridRenderable {
    val flipFactor = if (flip) -1f else 1f
    val rotationDegrees =
      if (to.x > from.x && to.y < from.y) 0
      else if (to.x < from.x && to.y < from.y) 90
      else if (to.x < from.x && to.y > from.y) 180
      else 270
    val rotationFactor = if(rotationDegrees % 180 == 0) 0 else 1
    val x = to.x + flipFactor * rotationFactor
    val y = to.y + flipFactor* rotationFactor
    return GridRenderable(T7Textures.Thaumonomicon.ARROW_HEAD.location,x, y, flipFactor, flipFactor, rotationDegrees)
  }

  private class Line(x: Float, y: Float, rotationDegrees: Int) : GridRenderable(T7Textures.Thaumonomicon.LINE.location, x, y, rotationDegrees = rotationDegrees)

  private fun connectionCorner1x1(to: Node, from: Node, flip: Boolean, rotationDegrees: Int): GridRenderable {
    val flipFactor = if (flip) -1f else 1f
    return GridRenderable(T7Textures.Thaumonomicon.CORNER_1X1.location, (to.x + from.x + flipFactor) / 2f, (to.y + from.y + flipFactor) / 2f, flipFactor, flipFactor, rotationDegrees)
  }

  private fun connectionCorner2x2(to: Node, from: Node, flip: Boolean, rotationDegrees: Int): GridRenderable {
    val flipFactor = if (flip) -1f else 1f
    return GridRenderable(T7Textures.Thaumonomicon.CORNER_2X2.location, (to.x + from.x + flipFactor) / 2f, (to.y + from.y + flipFactor) / 2f, 2f * flipFactor, 2f * flipFactor, rotationDegrees)
  }
}
