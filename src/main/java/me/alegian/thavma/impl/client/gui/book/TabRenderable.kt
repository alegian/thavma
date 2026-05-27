package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.entity.knowsResearch
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.core.Holder
import net.minecraft.world.entity.player.Player
import kotlin.math.pow

// represents the renderable content of a tab in the book
class TabRenderable(
  val screen: BookScreen,
  val category: ResearchCategory,
  val entries: List<Holder.Reference<ResearchEntry>>?,
  val player: Player
) : Renderable {
  companion object {
    private const val ZOOM_MULTIPLIER = 1.25
    const val maxScrollX = 600.0
    const val maxScrollY = 300.0
    private const val minZoom = 0.0
    private const val maxZoom = 5.0
    val TEXTURE: Texture = Texture("gui/book/tab_bg", 512, 512)
  }

  val entryWidgets = mutableListOf<EntryWidget>()

  init {
    entries?.forEach {
      var shown = player.knowsResearch(it)
      for (p in it.value().parents(player.level()))
        if (player.knowsResearch(p)) shown = true
      if (shown)
        entryWidgets.add(EntryWidget(screen, this, it, player))
    }
  }

  var scrollX = 0.0
    private set
  var scrollY = 0.0
    private set
  private var zoom = 2.0 // TODO: this is actually inverse zoom

  val dimensionX = entryWidgets.maxOf { it.x } - entryWidgets.minOf { it.x }
  val dimensionY = entryWidgets.maxOf { it.y } - entryWidgets.minOf { it.y }
  val average = (dimensionY + dimensionX) / 2

  fun drag(x: Double, y: Double) {
    val rawScrollX = scrollX - zoomFactor() * x * 1200 / (average * 7 + 50)
    val rawScrollY = scrollY - zoomFactor() * y * 1200 / (average * 7 + 50)

    scrollX = rawScrollX.coerceIn(-maxScrollX, maxScrollX)
    scrollY = rawScrollY.coerceIn(-maxScrollY, maxScrollY)
  }

  fun zoom(change: Double) {
    zoom = (zoom - change).coerceIn(minZoom, maxZoom)
  }

  fun zoomFactor(): Double {
    return ZOOM_MULTIPLIER.pow(zoom)
  }

  override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
    if (screen.currentTab != this) return

    val corner = FrameRenderable.CORNER_TEXTURE
    val edge = FrameRenderable.EDGE_TEXTURE
    graphics.enableCrop(corner.width / 2 + edge.height / 2, corner.height / 2 + edge.height / 2)

    graphics.usePose {
      val screenHeight = graphics.guiHeight()
      val screenWidth = graphics.guiWidth()

      // background stars
      translateXY(screenWidth / 2, screenHeight / 2)
      scaleXY(1 / zoomFactor())
      graphics.blit(
        TEXTURE.location,
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
    }

    graphics.disableCrop()
  }
}
