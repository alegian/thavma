package me.alegian.thavma.impl.client.gui.research_table

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blitCentered
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class ButtonWidget(position: Vec2, private val invert: Boolean, private val handleClick: () -> Unit) : AbstractWidget(position.x.toInt(), position.y.toInt(), TEXTURE.width, TEXTURE.height, Component.literal("button")) {
  init {
    tooltip = Tooltip.create(message)
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    RenderSystem.disableCull()
    guiGraphics.usePose {
      translateXY(x, y)
      translateXY(TEXTURE.width / 2, TEXTURE.height / 2)
      if (invert) scale(-1f, 1f, 1f)
      guiGraphics.blitCentered(TEXTURE)
    }
    RenderSystem.enableCull()
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    handleClick()
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  companion object {
    val TEXTURE = Texture("gui/research_table/button", 36, 12, 36, 12)
  }
}