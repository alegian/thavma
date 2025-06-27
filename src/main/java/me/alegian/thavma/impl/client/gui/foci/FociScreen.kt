package me.alegian.thavma.impl.client.gui.foci

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.payload.FocusPayload
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth.lerp
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.PickaxeItem
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.*

private const val TITLE_ID = "screen." + Thavma.MODID + ".title"
private val BACKGROUND = Texture("gui/foci/circle", 236, 236)
private const val SCALE = 0.5f
private const val DEGREES_PER_TICK = 0.5f
private val MAX_RADIUS = SCALE * BACKGROUND.width / 2
private val ANIMATION_DURATION = 5

class FociScreen : Screen(Component.translatable(TITLE_ID)) {
  var ticks = 0
  var selectedIndex: Int? = null
  val foci = getFociFromLocalInventory()
  var animationProgress = 0
  var prevAnimationProgress = 0

  override fun init() {
    T7KeyMappings.FOCI.isDown = true
  }

  override fun tick() {
    ticks++
    prevAnimationProgress = animationProgress
    animationProgress =
      if (T7KeyMappings.FOCI.isDown) {
        min(animationProgress + 1, ANIMATION_DURATION)
      } else {
        max(0, animationProgress - 1)
      }

    if (animationProgress == 0) {
      onClose()

      val selectedIndex = selectedIndex ?: return
      if (foci.isNotEmpty())
        PacketDistributor.sendToServer(FocusPayload(foci[selectedIndex]))
    }
  }

  override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    val partialProgress = lerp(partialTick, prevAnimationProgress.toFloat(), animationProgress.toFloat())
    val animatedRadius = lerp(partialProgress / ANIMATION_DURATION, 0f, MAX_RADIUS)
    val deadRadius = animatedRadius / 2

    val centeredMouseX = mouseX - width / 2.0
    val centeredMouseY = mouseY - height / 2.0
    val mouseRadius = hypot(centeredMouseX, centeredMouseY)
    val anglePerItem = 2 * PI / foci.size
    var mouseAngle = atan2(centeredMouseY, centeredMouseX) + anglePerItem / 2
    if (mouseAngle < 0) mouseAngle += 2 * PI
    selectedIndex = floor(mouseAngle / anglePerItem).toInt()
    if (mouseRadius <= deadRadius) selectedIndex = null

    guiGraphics.usePose {
      translateXY(width / 2, height / 2)
      val renderTicks = ticks + partialTick

      guiGraphics.usePose {
        scaleXY(SCALE * animatedRadius / MAX_RADIUS)
        rotateZ((renderTicks * DEGREES_PER_TICK) % 360)
        setRenderSystemColor(T7Colors.PURPLE)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        guiGraphics.blitCentered(BACKGROUND)
        resetRenderSystemColor()
      }

      for ((i, stack) in foci.withIndex())
        guiGraphics.usePose {
          translateXY(
            (cos(anglePerItem * i) * animatedRadius),
            (sin(anglePerItem * i) * animatedRadius)
          )
          if (i == selectedIndex) scaleXY(1.5f)
          translateXY(-8, -8)
          guiGraphics.renderItem(stack, 0, 0)
        }

      val focus = Minecraft.getInstance().player?.mainHandItem?.get(T7DataComponents.FOCUS)?.nonEmptyItems()?.firstOrNull()
      if (focus == null) return@usePose
      translateXY(-8, -8)
      guiGraphics.renderItem(focus, 0, 0)
    }
  }

  override fun shouldCloseOnEsc() = false

  override fun isPauseScreen() = false
}

fun getFociFromLocalInventory(): List<ItemStack> {
  val foci = mutableListOf<ItemStack>()
  val player = Minecraft.getInstance().player ?: return foci

  player.inventory.run {
    for (i in 0..<containerSize) {
      val stack = getItem(i)
      if (stack.item is PickaxeItem)
        foci.add(stack)
    }
  }

  return foci
}