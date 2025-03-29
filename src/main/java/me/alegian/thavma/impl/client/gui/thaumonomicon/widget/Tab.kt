package me.alegian.thavma.impl.client.gui.thaumonomicon.widget

import me.alegian.thavma.impl.client.gui.thaumonomicon.grid.renderGrid
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.Mth
import kotlin.math.pow

private const val ZOOM_MULTIPLIER = 1.25f
private val n0 = Node(0, 0)
private val n2 = Node(2, -2, listOf(n0), false)
private val n5 = Node(3, 1, preferX = false)
private val nodes = listOf(
  n0,
  Node(1, -1),
  n2,
  Node(3, -3, listOf(n2)),
  Node(1, 3, listOf(n5)),
  n5
)

// represents the renderable content of a tab in the book
class Tab(private val maxScrollX: Float, private val maxScrollY: Float) : Renderable {
  var scrollX = 0.0
  var scrollY = 0.0
  private var zoom = 2f

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
      translateXY(-scrollX, -scrollY)
      renderGrid(nodes, graphics)

      graphics.disableCrop()
    }
  }
}

/**
 * By default, connections prefer to connect to children along the Y axis.
 * @param preferX makes connections prefer the X axis.
 * Straight lines will ignore this preference
 */
class Node(val x: Int, val y: Int, val children: List<Node> = listOf(), val preferX: Boolean = false)
