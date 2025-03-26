package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

// represents any element drawn in the Grid, even if it doesn't directly snap to it
// clockwise: 
open class GridRenderable(
  private val texture: ResourceLocation,
  val x: Float,
  val y: Float,
  val sizeX: Float = 1f,
  val sizeY: Float = 1f,
  private val rotationDegrees: Int = 0
) {
  fun render(graphics: GuiGraphics, cellSize: Int, hovered: Boolean, tickDelta: Float) {
    val xPos = (cellSize * (x - sizeX / 2f)).toDouble()
    val yPos = (cellSize * (y - sizeY / 2f)).toDouble()

    graphics.usePose {
      rotateZ(rotationDegrees.toFloat())
      translateXY(xPos, yPos)
      graphics.blit(
        texture,
        0,
        0,
        0,
        0f,
        0f,
        (cellSize * sizeX).toInt(),
        (cellSize * sizeX).toInt(),
        (cellSize * sizeX).toInt(),
        (cellSize * sizeX).toInt()
      )
    }
  }
}
