package me.alegian.thavma.impl.client.gui.tooltip

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.renderer.AspectRenderer.renderAspect
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

private const val WIDTH = AspectRenderer.PIXEL_RESOLUTION
private const val PADDING = 3

class AspectClientTooltipComponent(tooltip: AspectTooltipComponent) : ClientTooltipComponent {
  private val displayedAspects = tooltip.aspectMap?.displayedAspects()

  override fun getHeight(): Int {
    if (numAspects == 0) return 0
    return WIDTH + PADDING * 2
  }

  override fun getWidth(pFont: Font): Int {
    if (numAspects == 0) return 0
    return (WIDTH + PADDING) * numAspects
  }

  private val numAspects: Int
    get() = displayedAspects?.size ?: 0

  override fun renderImage(pFont: Font, pX: Int, pY: Int, guiGraphics: GuiGraphics) {
    if (displayedAspects == null) return

    for ((i, aspectStack) in displayedAspects.withIndex()) {
      renderAspect(guiGraphics, aspectStack, pX + i * (WIDTH + PADDING), pY + PADDING)
    }
  }
}
