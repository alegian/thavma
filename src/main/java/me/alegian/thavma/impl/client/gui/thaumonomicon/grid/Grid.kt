package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics

/*
 * The invisible grid that renders the contents of the Thaumonomicon.
 * The research nodes are by definition 1x1.
 * Contains a list of GridRenderable objects that are drawn.
 */
class Grid(private val cellSize: Int) {
  private val contents = ArrayList<GridRenderable>()

  fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float, scrollX: Double, scrollY: Double) {
    pGuiGraphics.usePose {
      translateXY(-scrollX, -scrollY)
      for (r in contents) {
        r.render(pGuiGraphics, cellSize, false, pPartialTick)
      }
      renderDebug(pGuiGraphics, cellSize)
    }
  }

  fun addCell(r: GridRenderable) {
    contents.add(r)
  }
}

private fun renderDebug(guiGraphics: GuiGraphics, cellSize: Int) {
  guiGraphics.usePose {
    guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())
    translateXY(-cellSize / 2.0, -cellSize / 2.0)

    for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * cellSize, -0x1)

    for (i in -31..31) guiGraphics.vLine(i * cellSize, -10000, 10000, -0x1)
  }
}
