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

  private val n0 = EntryWidget(this, Vec2(0f, 0f))
  private val n1 = EntryWidget(this, Vec2(4f, -4f), preferX = true)
  private val nn = EntryWidget(this, Vec2(2f, -4f), preferX = true)
  private val nn1 = EntryWidget(this, Vec2(4f, -2f), preferX = true)
  private val n2 = EntryWidget(this, Vec2(2f, -2f), listOf(n0), true)
  private val n5 = EntryWidget(this, Vec2(1f, 12f), preferX = false)
  private val n12 = EntryWidget(this, Vec2(12f, 2f), preferX = true)
  private val n6 = EntryWidget(this, Vec2(4f, 7f), preferX = true)
  private val n7 = EntryWidget(this, Vec2(7f, 4f), preferX = true)
  private val entryWidgets = listOf(
    n0,
    EntryWidget(this, Vec2(1f, -1f)),
    n2,
    n5,
    n12,
    n6,
    n7,
    nn1,
    EntryWidget(this, Vec2(3f, 3f), listOf(n6, n7)),
    EntryWidget(this, Vec2(3f, -3f), listOf(nn1)),
    EntryWidget(this, Vec2(3f, 1f), listOf(n5)),
    EntryWidget(this, Vec2(-1f, 4f), listOf(n12)),
  )

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
