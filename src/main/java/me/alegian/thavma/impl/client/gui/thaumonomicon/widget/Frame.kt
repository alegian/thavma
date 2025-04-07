package me.alegian.thavma.impl.client.gui.thaumonomicon.widget

import com.mojang.math.Axis
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import org.joml.Vector3f

// the wooden frame around the contents
val frame = Renderable { guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float ->
  val screenHeight = guiGraphics.guiHeight()
  val screenWidth = guiGraphics.guiWidth()
  guiGraphics.usePose {
    translateXY(screenWidth / 2, screenHeight / 2)
    for (xDirection in listOf(-1f, 1f)) {
      for (yDirection in listOf(-1f, 1f)) {
        guiGraphics.usePose {
          scale(xDirection, yDirection, 1f)
          translateXY(-screenWidth / 2, -screenHeight / 2)

          renderCorner(guiGraphics)
          renderEdge(guiGraphics, xDirection * yDirection == -1f)
        }
      }
    }
  }
}

private fun renderCorner(guiGraphics: GuiGraphics) = guiGraphics.blit(
  T7Textures.Thaumonomicon.FRAME_CORNER
)

/**
 * draws as much edge as needed to cover screen boundaries
 */
private fun renderEdge(guiGraphics: GuiGraphics, reflect: Boolean) {
  val screenHeight = guiGraphics.guiHeight()
  val screenWidth = guiGraphics.guiWidth()
  val texture = T7Textures.Thaumonomicon.FRAME_EDGE
  val corner = T7Textures.Thaumonomicon.FRAME_CORNER
  val drawWidth =
    if (!reflect) screenWidth - corner.width * 2
    else screenHeight - corner.height * 2

  guiGraphics.usePose {
    if (reflect) mulPose(Axis.of(Vector3f(1f, 1f, 0f)).rotationDegrees(180f))
    translateXY(corner.width, (corner.height - texture.height) / 2)

    guiGraphics.blit(
      texture.location,
      0,
      0,
      0,
      0f,
      0f,
      drawWidth,
      texture.height,
      texture.width,
      texture.height
    )
  }
}
