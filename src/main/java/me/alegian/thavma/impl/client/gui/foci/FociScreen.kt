package me.alegian.thavma.impl.client.gui.foci

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val TITLE_ID = "screen." + Thavma.MODID + ".title"
private val BACKGROUND = Texture("gui/foci/circle", 236, 236)
private const val SCALE = 0.5

class FociScreen : Screen(Component.translatable(TITLE_ID)) {
  var lastMouseX = width / 2
  var lastMouseY = height / 2
  val options = listOf(Items.DIAMOND.defaultInstance, Items.DIAMOND_PICKAXE.defaultInstance, Items.PUFFERFISH.defaultInstance, Items.TURTLE_HELMET.defaultInstance)

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(width / 2, height / 2)

      guiGraphics.usePose {
        scaleXY(SCALE)
        setRenderSystemColor(T7Colors.PURPLE)
        guiGraphics.blitCentered(BACKGROUND)
        resetRenderSystemColor()
      }

      translateXY(-8, -8)
      for ((i, stack) in options.withIndex()) {
        val radius = SCALE * BACKGROUND.width / 2
        val angle = 2 * PI / options.size
        guiGraphics.renderItem(
          stack,
          (sin(angle * i) * radius).toInt(),
          (cos(angle * i) * radius).toInt()
        )
      }
    }
    
    lastMouseX = mouseX
    lastMouseY = mouseY
  }

  override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
    if (keyCode == T7KeyMappings.FOCI.key.value) {
      onClose()
      return true
    }
    return super.keyReleased(keyCode, scanCode, modifiers)
  }
}