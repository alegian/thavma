package me.alegian.thavma.impl.client.gui.foci

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import kotlin.math.*

private const val TITLE_ID = "screen." + Thavma.MODID + ".title"
private val BACKGROUND = Texture("gui/foci/circle", 236, 236)
private const val SCALE = 0.5
private const val DEGREES_PER_TICK = 0.5f
private val RADIUS = SCALE * BACKGROUND.width / 2

class FociScreen : Screen(Component.translatable(TITLE_ID)) {
  var ticks = 0
  var selectedIndex = 0
  val options = listOf(Items.DIAMOND.defaultInstance, Items.DIAMOND_PICKAXE.defaultInstance, Items.PUFFERFISH.defaultInstance, Items.TURTLE_HELMET.defaultInstance)

  override fun tick() {
    ticks++
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    val centeredMouseX = mouseX - width / 2.0
    val centeredMouseY = mouseY - height / 2.0
    val anglePerItem = 2 * PI / options.size
    var mouseAngle = atan2(centeredMouseY, centeredMouseX) + anglePerItem / 2
    if (mouseAngle < 0) mouseAngle += 2 * PI
    selectedIndex = floor(mouseAngle / anglePerItem).toInt()

    guiGraphics.usePose {
      translateXY(width / 2, height / 2)
      val renderTicks = ticks + partialTick

      guiGraphics.usePose {
        scaleXY(SCALE)
        rotateZ((renderTicks * DEGREES_PER_TICK) % 360)
        setRenderSystemColor(T7Colors.PURPLE)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        guiGraphics.blitCentered(BACKGROUND)
        resetRenderSystemColor()
      }

      for ((i, stack) in options.withIndex())
        guiGraphics.usePose {
          translateXY(
            (cos(anglePerItem * i) * RADIUS),
            (sin(anglePerItem * i) * RADIUS)
          )
          if (i == selectedIndex) scaleXY(1.5f)
          translateXY(-8, -8)
          guiGraphics.renderItem(stack, 0, 0)
        }
    }
  }

  override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
    if (keyCode == T7KeyMappings.FOCI.key.value) {
      onClose()
      return true
    }
    return super.keyReleased(keyCode, scanCode, modifiers)
  }
}