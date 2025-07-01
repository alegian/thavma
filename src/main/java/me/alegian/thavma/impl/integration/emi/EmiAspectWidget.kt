package me.alegian.thavma.impl.integration.emi

import dev.emi.emi.api.widget.DrawableWidget
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.common.aspect.AspectStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Component

class EmiAspectWidget(x: Int, y: Int, private val aspectStack: AspectStack) : DrawableWidget(x, y, 16, 16, Renderer(aspectStack)) {
  override fun getTooltip(mouseX: Int, mouseY: Int) =
    listOf(ClientTooltipComponent.create(Component.translatable(aspectStack.aspect.translationId).visualOrderText))
}

private class Renderer(private val aspectStack: AspectStack) : DrawableWidget.DrawableWidgetConsumer {
  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    AspectRenderer.renderAspect(guiGraphics, aspectStack)
  }
}
