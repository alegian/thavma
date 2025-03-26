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
    val n0 = grid.add(Node(0f, 0f))
    grid.add(Node(1f, -1f))
    val n2 = grid.add(Node(2f, -2f, listOf(n0)))
    grid.add(Node(3f, -3f, listOf(n2)))
    grid.add(Node(3f, 3f))
    grid.add(Node(1f, 3f, listOf(n2)))
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


}

fun arrowHead(to: GridRenderable, from: GridRenderable, flip: Boolean): GridRenderable {
  val flipFactor = if (flip) -1f else 1f
  return GridRenderable(T7Textures.Thaumonomicon.ARROW_HEAD.location, (to.x + from.x) / 2f+flipFactor, (to.y + from.y) / 2f, flipFactor, flipFactor,90 )
}

fun connectionLine(x: Float, y: Float, rotationDegrees: Int): GridRenderable {
  return GridRenderable(T7Textures.Thaumonomicon.LINE.location, x, y, rotationDegrees = rotationDegrees)
}

fun connectionCorner1x1(to: GridRenderable, from: GridRenderable, flip: Boolean, rotationDegrees: Int): GridRenderable {
  val flipFactor = if (flip) -1f else 1f
  return GridRenderable(T7Textures.Thaumonomicon.CORNER_1X1.location, (to.x + from.x + flipFactor) / 2f, (to.y + from.y + flipFactor) / 2f, flipFactor, flipFactor, rotationDegrees)
}

fun connectionCorner2x2(to: GridRenderable, from: GridRenderable, flip: Boolean, rotationDegrees: Int): GridRenderable {
  val flipFactor = if (flip) -1f else 1f
  return GridRenderable(T7Textures.Thaumonomicon.CORNER_2X2.location, (to.x + from.x + flipFactor) / 2f, (to.y + from.y + flipFactor) / 2f, 2f * flipFactor, 2f * flipFactor, rotationDegrees)
}

class Node(x: Float, y: Float, children: List<GridRenderable> = listOf()) : GridRenderable(T7Textures.Thaumonomicon.NODE.location, x, y, children = children)
