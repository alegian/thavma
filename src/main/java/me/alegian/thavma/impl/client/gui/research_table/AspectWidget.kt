package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.common.aspect.Aspect
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class AspectWidget(position: Vec2, private val researchScreen: ResearchScreen, private val aspect: Aspect) : AbstractWidget(position.x.toInt(), position.y.toInt(), 16, 16, Component.empty()) {
  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    AspectRenderer.drawAspectIcon(guiGraphics, aspect, x, y)
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    researchScreen.selectedAspect = aspect
  }

  override fun playDownSound(handler: SoundManager) {
  }
}