package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.connectionCorner1x1
import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.connectionCorner2x2
import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.connectionLine
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
      for (node in contents) {
        node.render(pGuiGraphics, cellSize, false, pPartialTick)
        for (child in node.children) {
          Connection(this@Grid, child, node).render()
        }
      }
      renderDebug(pGuiGraphics, cellSize)
    }
  }

  fun add(r: GridRenderable): GridRenderable {
    contents.add(r)
    return r
  }
}

class Connection(val grid: Grid, val to: GridRenderable, val from: GridRenderable) {
  fun render() {
    val diffX = to.x - from.x
    val diffY = to.y - from.y
    if (diffX > 2 && diffY > 2) throw IllegalStateException()
    if (diffX > 2) {
      grid.add(connectionLine(from.x + 1, from.y, 0))
    } else if (diffY > 2) {
      grid.add(connectionLine(from.x, from.y + 1, 90))
    } else if (diffY == 2f && diffX == 2f) {
      grid.add(connectionCorner2x2(to, from, false, 0))
    } else if (diffY == 2f) {
      grid.add(connectionLine(from.x, from.y + 1, 90))
    } else if (diffX == 2f) {
      grid.add(connectionLine(from.x + 1, from.y, 0))
    } else {
      grid.add(connectionCorner1x1(to, from, false, 0))
    }
  }
}

private fun renderDebug(guiGraphics: GuiGraphics, cellSize: Int) {
  guiGraphics.usePose {
    guiGraphics.fill(-5, -5, 5, 5, 0xFFFF0000.toInt())

    for (i in -31..31) guiGraphics.hLine(-10000, 10000, i * cellSize, -0x1)

    for (i in -31..31) guiGraphics.vLine(i * cellSize, -10000, 10000, -0x1)
  }
}
