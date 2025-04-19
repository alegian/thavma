package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import org.joml.Vector3f

// the frame around the scrollable content
object FrameRenderable : Renderable {
  val CORNER_TEXTURE = Texture("gui/book/frame_corner", 32, 32)
  val EDGE_TEXTURE = Texture("gui/book/frame_edge", 32, 24)

  /**
   * Renders the edge layer before the corners to avoid overlay bugs
   */
  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    val screenHeight = guiGraphics.guiHeight()
    val screenWidth = guiGraphics.guiWidth()
    // allows negative size drawing, which simplifies math
    RenderSystem.disableCull()
    guiGraphics.usePose {
      translateXY(screenWidth / 2, screenHeight / 2)

      renderIn4Directions(guiGraphics){xDirection, yDirection->
        renderEdge(guiGraphics, xDirection * yDirection == -1f)
      }

      renderIn4Directions(guiGraphics){_, _->
        renderCorner(guiGraphics)
      }
    }
    RenderSystem.enableCull()
  }
}

/**
 * flips x axis, y axis, both, or neither
 * before drawing something
 */
private fun renderIn4Directions(guiGraphics: GuiGraphics, drawCall: (xDirection:Float, yDirection:Float) -> Unit) {
  val screenHeight = guiGraphics.guiHeight()
  val screenWidth = guiGraphics.guiWidth()

  for (xDirection in listOf(-1f, 1f)) {
    for (yDirection in listOf(-1f, 1f)) {
      guiGraphics.usePose {
        scale(xDirection, yDirection, 1f)
        translateXY(-screenWidth / 2, -screenHeight / 2)

        drawCall(xDirection, yDirection)
      }
    }
  }
}

private fun renderCorner(guiGraphics: GuiGraphics) = guiGraphics.blit(
  FrameRenderable.CORNER_TEXTURE
)

/**
 * draws as much edge as needed to cover screen boundaries
 */
private fun renderEdge(guiGraphics: GuiGraphics, reflect: Boolean) {
  val screenHeight = guiGraphics.guiHeight()
  val screenWidth = guiGraphics.guiWidth()
  val texture = FrameRenderable.EDGE_TEXTURE
  val corner = FrameRenderable.CORNER_TEXTURE
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
