package me.alegian.thavma.impl.client.gui.thaumonomicon.renderable

import me.alegian.thavma.impl.client.gui.thaumonomicon.CELL_SIZE
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.research.ResearchEntry
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.util.Mth
import kotlin.math.pow

private const val ZOOM_MULTIPLIER = 1.25
private const val maxScrollX = 300f
private const val maxScrollY = 300f

// represents the renderable content of a tab in the book
class TabRenderable() : Renderable {
  var scrollX = 0.0
    private set
  var scrollY = 0.0
    private set
  private var zoom = 2f // TODO: this is actually inverse zoom
  private val entries = mutableListOf<EntryRenderable>()

  init {
//    val n0 = makeEntry(0, 0, false)
//    val nn1 = makeEntry(4, -2, true)
//    val n5 = makeEntry(1, 12, false)
//    val n12 = makeEntry(12, 2, true)
//    val n6 = makeEntry(4, 7, true)
//    val n7 = makeEntry(7, 4, true)
//    makeEntry(2, -2, true, n0)
//    makeEntry(4, -4, true)
//    makeEntry(2, -4, true)
//    makeEntry(3, 3, false, n6, n7)
//    makeEntry(3, -3, false, nn1)
//    makeEntry(3, 1, false, n5)
//    makeEntry(-1, 4, false, n12)
  }

  fun setEntries(rawEntries: List<ResearchEntry>){
    for (rawEntry in rawEntries){
      // TODO: resolve children
      EntryRenderable(this, rawEntry.position, listOf(), rawEntry.preferX).also {
        entries.add(it)
      }
    }
  }

  fun drag(x: Double, y: Double) {
    val rawScrollX = scrollX - zoomFactor() * x
    val rawScrollY = scrollY - zoomFactor() * y

    scrollX = Mth.clamp(rawScrollX, (-1 * maxScrollX).toDouble(), maxScrollX.toDouble())
    scrollY = Mth.clamp(rawScrollY, (-1 * maxScrollY).toDouble(), maxScrollY.toDouble())
  }

  fun zoom(y: Float) {
    zoom = Mth.clamp(zoom - y, 0f, 5f)
  }

  fun zoomFactor(): Double {
    return ZOOM_MULTIPLIER.pow(zoom.toDouble())
  }

  override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
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
