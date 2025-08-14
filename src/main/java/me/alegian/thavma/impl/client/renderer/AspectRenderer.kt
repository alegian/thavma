package me.alegian.thavma.impl.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.texture.atlas.AspectAtlas
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.util.vec2
import me.alegian.thavma.impl.rl
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import me.alegian.thavma.impl.common.util.plus
import me.alegian.thavma.impl.common.util.unaryMinus
import kotlin.math.ceil
import kotlin.math.min

private const val ROW_SIZE: Int = 5 // max aspects per row
private const val QUAD_SIZE: Float = .3f // aspect width in-world is .3 blocks

/**
 * Renders Aspects, either in-world or in GUIs.
 * The draw call always has size 16x16, use pose manipulation to change size
 */
object AspectRenderer {
  fun renderAfterWeather(aspects: AspectMap, poseStack: PoseStack, camera: Camera, blockPos: BlockPos) {
    if (aspects.isEmpty) return

    val guiGraphics = GuiGraphics(Minecraft.getInstance(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource())

    guiGraphics.usePose {
      translate(-camera.position + blockPos.center + Vec3(0.0, 0.75, 0.0))
      val angle = calculatePlayerAngle(blockPos.center)
      mulPose(Axis.YP.rotation(angle))

      scaleXY(QUAD_SIZE) // this puts us in "aspect space"
      scaleXY(1 / 16f) // this puts us in "pixel space"
      scale(1f, -1f, 1f) // world Y to GUI Y
      renderAspectsWrapped(aspects, guiGraphics)
    }
  }

  fun renderAspectsWrapped(aspects: AspectMap, guiGraphics: GuiGraphics) {
    // these offsets account for wrapping to new lines, and centering the aspects
    val offsets = calculateOffsets(aspects.size)

    for ((i, aspectStack) in aspects.withIndex())
      guiGraphics.usePose {
        translateXY(offsets[i])
        translateXY(-8, 0)
        renderAspect(guiGraphics, aspectStack)
      }
  }

  fun renderAspect(guiGraphics: GuiGraphics, aspectStack: AspectStack) {
    drawAspectIcon(guiGraphics, aspectStack.aspect)
    drawCount(guiGraphics, aspectStack.amount.toString())
  }

  fun drawAspectIcon(guiGraphics: GuiGraphics, aspect: Aspect) {
    val sprite = AspectAtlas.getSprite(rl(aspect.id))

    RenderSystem.disableDepthTest()
    guiGraphics.blit(
      0,
      0,
      0,
      16,
      16,
      sprite,
      aspect.color
    )
    RenderSystem.enableDepthTest()
  }

  private fun calculateOffsets(numAspects: Int): List<Vec2> {
    val offsets = MutableList(numAspects) { Vec2.ZERO }

    val rows = ceil((1f * numAspects / ROW_SIZE)).toInt()
    for (i in 0..<rows) {
      val cols = min((numAspects - (i * ROW_SIZE)), ROW_SIZE)
      for (j in 0..<cols) {
        val xOffset = (cols - 1) / -2f + j
        offsets[i * ROW_SIZE + j] = vec2(16 * xOffset, -16 * i - 8)
      }
    }

    return offsets
  }

  private fun drawCount(guiGraphics: GuiGraphics, text: String) {
    guiGraphics.usePose {
      translateXY(16, 16) // bottom right, like item count
      scale(0.5f)
      val font = Minecraft.getInstance().font
      translateXY(-font.width(text), -font.lineHeight)
      guiGraphics.drawOutlinedSeethroughString(font, text, 0xFFFFFF, 0)
    }
  }
}
