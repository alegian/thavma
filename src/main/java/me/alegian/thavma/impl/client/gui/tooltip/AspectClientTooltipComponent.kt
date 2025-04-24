package me.alegian.thavma.impl.client.gui.tooltip

import me.alegian.thavma.impl.client.renderer.AspectRenderer.renderAspect
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

private const val WIDTH = 16
private const val PADDING = 3

class AspectClientTooltipComponent(tooltip: AspectTooltipComponent) : ClientTooltipComponent {
  private val aspects = tooltip.aspectMap

  override fun getHeight(): Int {
    if (numAspects == 0) return 0
    return WIDTH + PADDING * 2
  }

  override fun getWidth(pFont: Font): Int {
    if (numAspects == 0) return 0
    return (WIDTH + PADDING) * numAspects
  }

  private val numAspects: Int
    get() = aspects?.size ?: 0

  override fun renderImage(pFont: Font, pX: Int, pY: Int, guiGraphics: GuiGraphics) {
    if (aspects == null) return

    for ((i, aspectStack) in aspects.withIndex()) {
      guiGraphics.usePose {
        translateXY(pX + i * (WIDTH + PADDING), pY + PADDING)
        renderAspect(guiGraphics, aspectStack)
      }
    }
  }
}
