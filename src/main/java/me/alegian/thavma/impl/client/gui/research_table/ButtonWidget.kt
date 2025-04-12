package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class ButtonWidget(x: Int, y: Int, private val handleClick: () -> Unit): AbstractWidget(x, y, TEXTURE.width, TEXTURE.height, Component.literal("button")) {
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
    val TEXTURE = Texture("gui/research_table/button", 36, 8, 36, 8)
  }
}