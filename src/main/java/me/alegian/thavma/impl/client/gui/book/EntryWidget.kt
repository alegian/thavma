package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.pushScreen
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.minus
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput

/**
 * By default, connections prefer to connect to children along the Y axis.
 * @param preferX makes connections prefer the X axis.
 * Straight lines will ignore this preference
 */
class EntryWidget(private val screen: BookScreen, val tab: TabRenderable, val entry: ResearchEntry, val children: List<EntryWidget> = listOf()) :
  AbstractWidget(0, 0, CELL_SIZE.toInt(), CELL_SIZE.toInt(), entry.title) {
  init {
    tooltip = Tooltip.create(message)
  }

  private val pos = entry.position
  private val preferX = entry.preferX

  override fun getX(): Int {
    return ((pos.x * CELL_SIZE - CELL_SIZE / 2 - tab.scrollX) / tab.zoomFactor() + screen.width / 2).toInt()
  }

  override fun getY(): Int {
    return ((pos.y * CELL_SIZE - CELL_SIZE / 2 - tab.scrollY) / tab.zoomFactor() + screen.height / 2).toInt()
  }

  override fun getWidth(): Int {
    return (CELL_SIZE / tab.zoomFactor()).toInt()
  }

  override fun getHeight(): Int {
    return (CELL_SIZE / tab.zoomFactor()).toInt()
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    this.isHovered = guiGraphics.containsPointInScissor(mouseX, mouseY)
        && mouseX >= x
        && mouseY >= y
        && mouseX < x + getWidth()
        && mouseY < y + getHeight()

    guiGraphics.usePose {
      translateXY(screen.width / 2, screen.height / 2)
      scaleXY(1 / tab.zoomFactor())
      translateXY(-tab.scrollX, -tab.scrollY)
      scaleXY(CELL_SIZE)
      translateXY(pos.x, pos.y)

      renderEntry(guiGraphics)

      // allows negative size drawing, which greatly simplifies math
      RenderSystem.disableCull()
      for (child in children) {
        val dv = child.pos - pos
        guiGraphics.usePose {
          renderConnectionRecursive(dv.x, dv.y, guiGraphics, child.preferX, false)
        }
      }
      RenderSystem.enableCull()
    }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    pushScreen(EntryScreen(entry))
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  private fun renderEntry(guiGraphics: GuiGraphics) {
    renderGridElement(
      guiGraphics,
      1f,
      1f,
      TEXTURE.location,
      false
    )

    guiGraphics.usePose {
      scaleXY(1 / CELL_SIZE) // back to pixel space
      scaleXY(2f) // items are 16x, nodes are 32x
      guiGraphics.renderItem(entry.icon, -8, -8)
    }
  }

  companion object {
    val TEXTURE = Texture("gui/book/node", 32, 32)

    fun of(screen: BookScreen, tab: TabRenderable, entry: ResearchEntry): EntryWidget {
      return EntryWidget(screen, tab, entry, entry.resolveChildren().map { of(screen, tab, it) })
    }
  }
}