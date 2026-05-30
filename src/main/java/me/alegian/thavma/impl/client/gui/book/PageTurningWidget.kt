package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class PageTurningWidget(position: Vec2, private val pointsRight: Boolean, private val handleClick: () -> Unit) :
  AbstractWidget(
    position.x.toInt(),
    position.y.toInt(),
    LEFT_TEXTURE.width,
    LEFT_TEXTURE.height,
    Component.translatable(if (pointsRight) rightTranslationId else leftTranslationId)
  ) {

  init {
    tooltip = Tooltip.create(message)
  }

  override fun renderWidget(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    partialTick: Float
  ) {
    guiGraphics.usePose {
      translateXY(x, y)
      val texture = if (pointsRight) RIGHT_TEXTURE else LEFT_TEXTURE
      RenderSystem.enableBlend()
      RenderSystem.defaultBlendFunc()

      guiGraphics.blit(texture)

      RenderSystem.disableBlend()
    }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    handleClick()
  }

  companion object {
    val LEFT_TEXTURE = Texture("gui/book/alpha_arrows4best", 36, 22, 36, 22)
    val RIGHT_TEXTURE = Texture("gui/book/omega_arrows2best", 36, 22, 36, 22)
    private val namespace = ".page_turning_widget"
    val leftTranslationId = "book$namespace.left"
    val rightTranslationId = "book$namespace.right"
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }
}