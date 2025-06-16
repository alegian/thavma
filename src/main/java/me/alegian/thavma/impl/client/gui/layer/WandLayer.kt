package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw

private val CIRCLE = Texture("gui/overlay/circle", 147, 147)
private val BAR_FRAME = Texture("gui/overlay/bar_frame", 45, 103)
private val BAR_CONTENT = Texture("gui/overlay/bar_content", 18, 64)

object WandLayer : LayeredDraw.Layer {
  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val aspectContainer = AspectContainer.getAspectContainerInHand(Minecraft.getInstance().player)
    if (aspectContainer == null || Minecraft.getInstance().options.hideGui) return

    val aspectMap = aspectContainer.aspects
    val maxAmount = aspectContainer.capacity

    graphics.usePose {
      RenderSystem.enableBlend()
      RenderSystem.defaultBlendFunc()

      // draw the circle
      scaleXY(0.5f)
      graphics.blit(CIRCLE)

      rotateZ(-8f)
      // draw the bars
      for (deferredAspect in Aspects.PRIMAL_ASPECTS) {
        val a = deferredAspect.get()
        graphics.usePose {
          translateXY(0.0, CIRCLE.height)
          graphics.setColor(a.color)
          graphics.blit(
            BAR_CONTENT.location,
            -BAR_CONTENT.width / 2,
            (BAR_FRAME.height - BAR_CONTENT.height) / 2,
            0f, 0f,
            BAR_CONTENT.width,
            BAR_CONTENT.height * aspectMap[a] / maxAmount,
            BAR_CONTENT.width,
            BAR_CONTENT.height
          )
          graphics.resetColor()
          graphics.blit(BAR_FRAME.location, -BAR_FRAME.width / 2, 0, 0f, 0f, BAR_FRAME.width, BAR_FRAME.height, BAR_FRAME.width, BAR_FRAME.height)
        }
        rotateZ(-16f)
      }
    }
  }
}