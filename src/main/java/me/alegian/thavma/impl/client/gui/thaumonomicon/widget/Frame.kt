package me.alegian.thavma.impl.client.gui.thaumonomicon.widget

import me.alegian.thavma.impl.client.texture.T7Textures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable

// the wooden frame around the contents
class Frame : Renderable {
  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float) {
    val screenHeight = guiGraphics.guiHeight()
    val screenWidth = guiGraphics.guiWidth()

    guiGraphics.blit(
      T7Textures.Thaumonomicon.FRAME.location,
      0,
      0,
      0,
      0f,
      0f,
      screenWidth,
      screenHeight,
      screenWidth,
      screenHeight
    )
  }
}
