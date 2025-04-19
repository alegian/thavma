package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.research.ResearchCategory
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput

class TabSelectorWidget(x: Int, y: Int, private val category: ResearchCategory, private val handleClick: () -> Unit) : AbstractWidget(x, y, TEXTURE.width, TEXTURE.height, category.title) {
  init {
    tooltip = Tooltip.create(message)
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(x, y)
      guiGraphics.blit(TEXTURE)

      translateXY(TEXTURE.width / 2, TEXTURE.height / 2)
      scaleXY(2 * 0.8) // tabs are 32x, items are 16x, but we dont want full size
      guiGraphics.renderItem(category.icon, -8, -8)
    }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    handleClick()
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  companion object {
    val TEXTURE = Texture("gui/book/tab_selector", 32, 32, 32, 32)
  }
}