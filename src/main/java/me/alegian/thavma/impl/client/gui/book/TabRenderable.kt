package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.resources.ResourceKey
import kotlin.math.pow

private const val ZOOM_MULTIPLIER = 1.25
private const val maxScrollX = 300.0
private const val maxScrollY = 300.0
private const val minZoom = 0.0
private const val maxZoom = 5.0

// represents the renderable content of a tab in the book
class TabRenderable(val screen: BookScreen, val category: ResourceKey<ResearchCategory>) : Renderable {
  var scrollX = 0.0
    private set
  var scrollY = 0.0
    private set
  private var zoom = 2.0 // TODO: this is actually inverse zoom
  private val entries = mutableListOf<EntryRenderable>()

  fun setEntries(researchEntries: List<ResearchEntry>) {
    for (researchEntry in researchEntries) {
      entries.add(EntryRenderable.of(this, researchEntry))
    }
  }

  fun drag(x: Double, y: Double) {
    val rawScrollX = scrollX - zoomFactor() * x
    val rawScrollY = scrollY - zoomFactor() * y

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
    if (screen.currentCategory.compareTo(category) != 0) return

    val corner = T7Textures.Thaumonomicon.FRAME_CORNER
    val edge = T7Textures.Thaumonomicon.FRAME_EDGE
    graphics.enableCrop(corner.width / 2 + edge.height / 2, corner.height / 2 + edge.height / 2)

    graphics.usePose {
      val screenHeight = graphics.guiHeight()
      val screenWidth = graphics.guiWidth()

      // background stars
      translateXY(screenWidth / 2, screenHeight / 2)
      scaleXY(1 / zoomFactor())
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

      // research nodes and their connections
      translateXY(-scrollX, -scrollY)
      scaleXY(CELL_SIZE)
      for (entry in entries) {
        graphics.usePose {
          translateXY(entry.pos.x, entry.pos.y)
          entry.render(graphics, mouseX, mouseY, tickDelta)
        }
      }
    }

    graphics.disableCrop()

    for (entry in entries) {
      if (!isEntryHovered(graphics, entry, mouseX, mouseY)) continue
      graphics.usePose {
        translateXY(mouseX, mouseY)
        entry.renderTooltip(graphics)
      }
    }
  }

  private fun isEntryHovered(guiGraphics: GuiGraphics, entry: EntryRenderable, mouseX: Int, mouseY: Int): Boolean {
    val entryX = (entry.pos.x * CELL_SIZE - CELL_SIZE / 2 - scrollX) / zoomFactor() + guiGraphics.guiWidth() / 2
    val entryY = (entry.pos.y * CELL_SIZE - CELL_SIZE / 2 - scrollY) / zoomFactor() + guiGraphics.guiHeight() / 2
    val entryWidth = CELL_SIZE / zoomFactor()
    val entryHeight = CELL_SIZE / zoomFactor()

    return mouseX >= entryX && mouseX <= entryX + entryWidth &&
        mouseY >= entryY && mouseY <= entryY + entryHeight
  }
}