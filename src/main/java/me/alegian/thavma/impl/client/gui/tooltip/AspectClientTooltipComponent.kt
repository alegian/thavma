package me.alegian.thavma.impl.client.gui.tooltip

import com.google.common.collect.ImmutableList
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.renderer.AspectRenderer.renderAspect
import me.alegian.thavma.impl.common.aspect.AspectStack
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

private const val WIDTH = AspectRenderer.PIXEL_RESOLUTION
private const val PADDING = 3

class AspectClientTooltipComponent(tooltip: AspectTooltipComponent) : ClientTooltipComponent {
  private val displayedAspects: ImmutableList<AspectStack> = tooltip.aspectMap.displayedAspects()

  override fun getHeight(): Int {
    if (isEmpty) return 0
    return WIDTH + PADDING * 2
  }

  override fun getWidth(pFont: Font): Int {
    if (isEmpty) return 0
    return (WIDTH + PADDING) * displayedAspects.size
  }

  private val isEmpty: Boolean
    get() = displayedAspects.isEmpty()

  override fun renderImage(pFont: Font, pX: Int, pY: Int, guiGraphics: GuiGraphics) {
    if (isEmpty) return

    for ((i, aspectStack) in displayedAspects.withIndex()) {
      renderAspect(guiGraphics, aspectStack, pX + i * (WIDTH + PADDING), pY + PADDING)
    }
  }
}
