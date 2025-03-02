package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.Thavma.rl
import me.alegian.thavma.impl.client.texture.atlas.AspectAtlas
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.AspectStack
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.unaryMinus
import kotlin.math.ceil
import kotlin.math.min

object AspectRenderer {
  private const val ROW_SIZE: Int = 5 // max aspects per row
  private const val QUAD_SIZE: Float = .3f // aspect width is .3 blocks
  const val PIXEL_RESOLUTION: Int = 16 // pixels per block

  /**
   * Renders the Aspect contents of an AspectContainer, using GuiGraphics.<br></br>
   * GuiGraphics assumes integer pixel coordinates, so we multiply everything by 16 (so a block is 16x16).
   */
  fun renderAfterWeather(aspects: AspectMap, poseStack: PoseStack, camera: Camera, blockPos: BlockPos) {
    if (aspects.isEmpty) return

    val guiGraphics = GuiGraphics(Minecraft.getInstance(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource())

    guiGraphics.usePose {
      translate(-camera.position + blockPos.center + Vec3(0.0, 0.75 + QUAD_SIZE / 2, 0.0))
      val angle = calculatePlayerAngle(blockPos.center)
      mulPose(Axis.YP.rotation(angle))
      scale(QUAD_SIZE, QUAD_SIZE, 1f) // this puts us in "aspect space" where 1 means 1 aspect width

      // these offsets account for wrapping to new lines, and centering the aspects
      val offsets = calculateOffsets(aspects.size())

      for ((i, aspectStack) in aspects.displayedAspects().withIndex())
        guiGraphics.usePose {
          translate(offsets[i])

          // gui rendering is done in pixel space
          scale(1f / PIXEL_RESOLUTION, -1f / PIXEL_RESOLUTION, 1f)
          renderAspect(guiGraphics, aspectStack, -PIXEL_RESOLUTION / 2, -PIXEL_RESOLUTION / 2)
        }
    }
  }

  fun renderAspect(guiGraphics: GuiGraphics, aspectStack: AspectStack, pX: Int, pY: Int) {
    drawAspectIcon(guiGraphics, aspectStack.aspect, pX, pY)
    drawCount(guiGraphics, aspectStack.amount.toString(), pX, pY)
  }

  private fun drawAspectIcon(guiGraphics: GuiGraphics, aspect: Aspect, pX: Int, pY: Int) {
    val sprite = AspectAtlas.sprite(rl(aspect.id))

    RenderSystem.disableDepthTest()
    guiGraphics.blit(
      pX,
      pY,
      0,
      PIXEL_RESOLUTION,
      PIXEL_RESOLUTION,
      sprite,
      aspect.color
    )
    RenderSystem.enableDepthTest()
  }

  private fun calculateOffsets(numAspects: Int): Array<Vec3?> {
    val offsets = arrayOfNulls<Vec3>(numAspects)

    val rows = ceil((1f * numAspects / ROW_SIZE).toDouble()).toInt()
    for (i in 0..<rows) {
      val cols = min((numAspects - (i * ROW_SIZE)).toDouble(), ROW_SIZE.toDouble()).toInt()
      for (j in 0..<cols) {
        val xOffset = (cols - 1) / -2f + j
        offsets[i * ROW_SIZE + j] = Vec3(xOffset.toDouble(), i.toDouble(), 0.0)
      }
    }

    return offsets
  }

  private fun drawCount(guiGraphics: GuiGraphics, text: String, pX: Int, pY: Int) {
    guiGraphics.usePose {
      translate((pX + PIXEL_RESOLUTION).toFloat(), (pY + PIXEL_RESOLUTION).toFloat(), 0f) // bottom right, like item count
      scale(0.5f)
      val font = Minecraft.getInstance().font
      translate(-font.width(text).toFloat(), -font.lineHeight.toFloat(), 0f)
      guiGraphics.drawOutlinedSeethroughString(font, text, 0xFFFFFF, 0)
    }
  }
}
