package me.alegian.thavma.impl.client.gui.research_table

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.block.ResearchTableBlock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class ButtonWidget(position: Vec2, private val invert: Boolean, private val handleClick: () -> Unit) :
  AbstractWidget(position.x.toInt(), position.y.toInt(), LEFT_TEXTURE.width, LEFT_TEXTURE.height, Component.translatable(if (invert) rightTranslationId else leftTranslationId)) {
  init {
    tooltip = Tooltip.create(message)
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    RenderSystem.disableCull()
    guiGraphics.usePose {
      translateXY(x, y)
      val texture = if (invert) RIGHT_TEXTURE else LEFT_TEXTURE
      guiGraphics.blit(texture)
    }
    RenderSystem.enableCull()
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    handleClick()
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  companion object {
    val LEFT_TEXTURE = Texture("gui/research_table/left_button", 35, 12, 35, 12)
    val RIGHT_TEXTURE = Texture("gui/research_table/right_button", 35, 12, 35, 12)
    private val namespace = ".buttonWidget"
    val leftTranslationId = ResearchTableBlock.CONTAINER_TITLE + namespace + ".left"
    val rightTranslationId = ResearchTableBlock.CONTAINER_TITLE + namespace + ".right"
  }
}