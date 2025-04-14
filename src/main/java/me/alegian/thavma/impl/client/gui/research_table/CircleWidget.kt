package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.aspect.Aspect
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
      if (value != null) researchScreen.reseachState[Pair(indices.x.toInt(), indices.y.toInt())] = value
    }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(x, y)
      guiGraphics.blit(TEXTURE)
      aspect?.let {
        translateXY(TEXTURE.width / 2, TEXTURE.height / 2)

        renderConnections(aspect, guiGraphics)

        scaleXY(0.8f)
        AspectRenderer.drawAspectIcon(guiGraphics, it, -8, -8)
      }
    }
  }

  private fun renderConnections(aspect: Aspect?, guiGraphics: GuiGraphics) {
    for (neighborOffset in axialNeighbors) {
      val neighborIdx = axial(indices) + neighborOffset
      val neighbor = researchScreen.circleWidgets[neighborIdx.toIntPair()]
      if (neighbor == null) continue
      if (neighbor.aspect != aspect) continue
      val dx = neighbor.position.x - position.x
      val dy = neighbor.position.y - position.y
      val angleDegrees = atan2(dy, dx) * 180 / Math.PI
      guiGraphics.usePose {
        rotateZ(angleDegrees)
        translateXY(0, -0.5)
        guiGraphics.hLine(0, TEXTURE.width + HEX_GRID_GAP, 0, 0xFF0000FF.toInt())
      }
    }
  }

  override fun onRelease(mouseX: Double, mouseY: Double) {
    aspect = researchScreen.selectedAspect
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  override fun playDownSound(handler: SoundManager) {
  }

  companion object {
    val TEXTURE = Texture("gui/research_table/circle", 18, 18, 18, 18)
  }
}