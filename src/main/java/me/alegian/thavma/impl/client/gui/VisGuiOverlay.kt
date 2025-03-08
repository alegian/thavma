package me.alegian.thavma.impl.client.gui

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.LayeredDraw

object VisGuiOverlay {
  private val STAR = Texture("gui/overlay/star", 130, 130)
  private val BAR_FRAME = Texture("gui/overlay/bar_frame", 96, 96)
  private val BAR_CONTENT = Texture("gui/overlay/bar_content", 18, 64)

  val LAYER = LayeredDraw.Layer { graphics, _ ->
    val aspectContainer = AspectContainer.getAspectContainerInHand(Minecraft.getInstance().player)
    if (aspectContainer == null || Minecraft.getInstance().options.hideGui) return@Layer

    val vis = aspectContainer.aspects
    val maxAmount = aspectContainer.capacity

    graphics.usePose {
      // draw the star
      scaleXY(0.5f)
      translateXY(32.0, 16.0)
      graphics.resetColor()
      graphics.blit(STAR)

      // draw the bars
      translateXY(STAR.width / 2.0, STAR.height / 2.0)
      rotateZ(15f)

      for (deferredAspect in PRIMAL_ASPECTS) {
        val a = deferredAspect.get()
        graphics.usePose {
          translateXY(0.0, STAR.height / 2.0)
          graphics.setColor(a.color)
          graphics.blit(BAR_CONTENT.location, -BAR_CONTENT.width / 2, (BAR_FRAME.height - BAR_CONTENT.height) / 2, 0f, 0f, BAR_CONTENT.width, BAR_CONTENT.height * vis[a] / maxAmount, BAR_CONTENT.width, BAR_CONTENT.height)
          graphics.resetColor()
          graphics.blit(BAR_FRAME.location, -BAR_FRAME.width / 2, 0, 0f, 0f, BAR_FRAME.width, BAR_FRAME.height, BAR_FRAME.width, BAR_FRAME.height)
        }
        rotateZ(-24f)
      }
    }
  }
}
