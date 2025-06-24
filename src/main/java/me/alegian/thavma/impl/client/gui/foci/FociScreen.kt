package me.alegian.thavma.impl.client.gui.foci

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blitCentered
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

private const val TITLE_ID = "screen." + Thavma.MODID + ".title"
private val BACKGROUND = Texture("gui/foci/circle", 236, 236)

class FociScreen : Screen(Component.translatable(TITLE_ID)) {
  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(width / 2, height / 2)
      guiGraphics.blitCentered(BACKGROUND)
    }
  }

  override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
    if (keyCode == T7KeyMappings.FOCI.key.value){
      onClose()
      return true
    }
    return super.keyReleased(keyCode, scanCode, modifiers)
  }
}