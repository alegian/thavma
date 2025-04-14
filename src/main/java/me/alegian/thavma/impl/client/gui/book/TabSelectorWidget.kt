package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.research.ResearchCategory
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class TabSelectorWidget(x: Int, y: Int, category: ResearchCategory, private val handleClick: () -> Unit) : AbstractWidget(x, y, TEXTURE.width, TEXTURE.height, Component.literal(category.title)) {
  init {
    tooltip = Tooltip.create(message)
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(x, y)
      guiGraphics.blit(TEXTURE)
    }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    handleClick()
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  companion object {
    val TEXTURE = T7Textures.Thaumonomicon.FRAME_CORNER
  }
}