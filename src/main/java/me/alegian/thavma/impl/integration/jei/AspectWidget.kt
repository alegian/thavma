package me.alegian.thavma.impl.integration.jei

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.common.aspect.AspectStack
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.widgets.IRecipeWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.ScreenPosition
import net.minecraft.network.chat.Component

class AspectWidget(private val aspectStack: AspectStack, private val x: Int, private val y: Int) : IRecipeWidget {
  override fun getPosition() = ScreenPosition(x, y)

  override fun drawWidget(guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double) {
    AspectRenderer.renderAspect(guiGraphics, aspectStack)
  }

  override fun getTooltip(tooltip: ITooltipBuilder, mouseX: Double, mouseY: Double) {
    if (mouseX >= 0 && mouseY >= 0 && mouseX <= 16 && mouseY <= 16)
      tooltip.add(Component.translatable(aspectStack.aspect.translationId))
  }
}