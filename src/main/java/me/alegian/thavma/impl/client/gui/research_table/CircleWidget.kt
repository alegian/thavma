package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.gui.tooltip.T7Tooltip
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.aspect.Aspect
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
import kotlin.math.atan2

class CircleWidget(val position: Vec2, private val indices: Vec2, private val researchScreen: ResearchScreen) : AbstractWidget(position.x.toInt(), position.y.toInt(), TEXTURE.width, TEXTURE.height, Component.empty()) {
  var aspect
    get() = researchScreen.reseachState[Pair(indices.x.toInt(), indices.y.toInt())]
    set(value) {
      researchScreen.reseachState[Pair(indices.x.toInt(), indices.y.toInt())] = value
    }

  /**
   * this renders only the background of the widget.
   * rendering more things here causes overlaps,
   * so we defer the rest of the rendering to the screen,
   * to do in batches
   */
  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(x, y)
      guiGraphics.blit(TEXTURE)
    }
  }

  /**
   * render the connections (above the background)
   * called by the screen in batch
   */
  fun renderConnectionsDeferred(guiGraphics: GuiGraphics) {
    aspect?.let {
      guiGraphics.usePose {
        translateXY(x, y)
        translateXY(TEXTURE.width / 2, TEXTURE.height / 2)
        renderConnections(it, guiGraphics)
      }
    }
  }

  /**
   * render the aspects (above the connections)
   * called by the screen in batch
   */
  fun renderAspectDeferred(guiGraphics: GuiGraphics) {
    aspect?.let {
      guiGraphics.usePose {
        translateXY(x, y)
        translateXY(TEXTURE.width / 2, TEXTURE.height / 2)
        scaleXY(0.8f)
        AspectRenderer.drawAspectIcon(guiGraphics, it, -8, -8)
      }
    }
  }

  private fun renderConnections(aspect: Aspect, guiGraphics: GuiGraphics) {
    for (neighborOffset in axialNeighbors) {
      val neighborIdx = axial(indices) + neighborOffset
      val neighbor = researchScreen.circleWidgets[neighborIdx.toIntPair()]
      if (neighbor == null) continue
      if (neighbor.aspect?.components?.map { it.get() }?.contains(aspect) != true) continue
      val dx = neighbor.position.x - position.x
      val dy = neighbor.position.y - position.y
      val angleDegrees = atan2(dy, dx) * 180 / Math.PI
      guiGraphics.usePose {
        rotateZ(angleDegrees)
        translateXY(0, -0.5)
        guiGraphics.hLine(0, TEXTURE.width + HEX_GRID_GAP, 0, T7Colors.GREEN or (0xff shl 24))
      }
    }
  }

  override fun onRelease(mouseX: Double, mouseY: Double) {
    if (aspect == null)
      aspect = researchScreen.selectedAspect?.also {
        tooltip = T7Tooltip(
          Component.translatable(it.translationId),
          Component.translatable(removeTranslationId).withStyle(ChatFormatting.GRAY)
        )

        playDownSound(Minecraft.getInstance().soundManager)
      }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    if (aspect != null) {
      aspect = null
      tooltip = null

      playDownSound(Minecraft.getInstance().soundManager)
    }
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  override fun playDownSound(handler: SoundManager) {
  }

  companion object {
    private val namespace = ".circleWidget"
    val TEXTURE = Texture("gui/research_table/circle", 22, 22, 22, 22)
    val removeTranslationId = ResearchScreen.translationId + namespace + ".remove"
  }
}