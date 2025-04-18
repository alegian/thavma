package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.gui.tooltip.T7Tooltip
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.common.aspect.Aspect
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class AspectWidget(position: Vec2, private val researchScreen: ResearchScreen, private val aspect: Aspect) : AbstractWidget(position.x.toInt(), position.y.toInt(), 16, 16, Component.translatable(aspect.translationId)) {
  init {
    tooltip = T7Tooltip(
      message,
      Component.translatable(descriptionTranslationId).withStyle(ChatFormatting.GRAY),
      Component.translatable(costTranslationId).append(" ${aspect.rank}").withStyle(ChatFormatting.GRAY)
    )
  }

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

  companion object {
    private val namespace = ".aspectWidget"
    val descriptionTranslationId = ResearchScreen.translationId + namespace + ".description"
    val costTranslationId = ResearchScreen.translationId + namespace + ".cost"
  }
}