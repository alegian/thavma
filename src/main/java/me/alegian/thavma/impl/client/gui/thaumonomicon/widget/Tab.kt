package me.alegian.thavma.impl.client.gui.thaumonomicon.widget

import me.alegian.thavma.impl.client.gui.thaumonomicon.CELL_SIZE
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec2
import kotlin.math.pow

private const val ZOOM_MULTIPLIER = 1.25f

// represents the renderable content of a tab in the book
class Tab(private val maxScrollX: Float, private val maxScrollY: Float) : Renderable {
  var scrollX = 0.0
    private set
  var scrollY = 0.0
    private set
  private var zoom = 2f
  private val entryWidgets = mutableListOf<EntryWidget>()

  init {
    val n0 = makeWidget(0, 0, false)
    val nn1 = makeWidget(4, -2, true)
    val n5 = makeWidget(1, 12, false)
    val n12 = makeWidget(12, 2, true)
    val n6 = makeWidget(4, 7, true)
    val n7 = makeWidget(7, 4, true)
    makeWidget(2, -2, true, n0)
    makeWidget(4, -4, true)
    makeWidget(2, -4, true)
    makeWidget(3, 3, false, n6, n7)
    makeWidget(3, -3, false, nn1)
    makeWidget(3, 1, false, n5)
    makeWidget(-1, 4, false, n12)
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

  private fun makeWidget(x: Int, y: Int, preferX: Boolean, vararg children: EntryWidget): EntryWidget {
    return EntryWidget(this, Vec2(x.toFloat(), y.toFloat()), children.asList(), preferX).also {
      entryWidgets.add(it)
    }
  }

  override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
    graphics.usePose {
      val screenHeight = graphics.guiHeight()
      val screenWidth = graphics.guiWidth()

      val corner = T7Textures.Thaumonomicon.FRAME_CORNER
      val edge = T7Textures.Thaumonomicon.FRAME_EDGE
      graphics.enableCrop(corner.width / 2 + edge.height / 2, corner.height / 2 + edge.height / 2)

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
      scaleXY(CELL_SIZE)
      for (entry in entryWidgets) entry.render(graphics, mouseX, mouseY, tickDelta)

      graphics.disableCrop()
    }
  }
}
