package me.alegian.thavma.impl.client.gui.thaumonomicon.grid

import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

// represents any element drawn in the Grid, even if it doesn't directly snap to it
// clockwise: 
open class GridRenderable(private val texture: ResourceLocation, private val x: Int, private val y: Int, sizeX: Int = 1, sizeY: Int = 1, flip: Boolean = false, private val rotationDegrees: Int = 0) {
  private val sizeX = if (flip) -sizeX else sizeX
  private val sizeY = if (flip) -sizeY else sizeY

  constructor(texture: ResourceLocation, x: Int, y: Int, rotationDegrees: Int) : this(texture, x, y, 1, 1, false, rotationDegrees)

  constructor(texture: ResourceLocation, x: Int, y: Int, flip: Boolean, rotationDegrees: Int) : this(texture, x, y, 1, 1, flip, rotationDegrees)

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
        cellSize * sizeX,
        cellSize * sizeY,
        cellSize * sizeX,
        cellSize * sizeY
      )
    }
  }
}
