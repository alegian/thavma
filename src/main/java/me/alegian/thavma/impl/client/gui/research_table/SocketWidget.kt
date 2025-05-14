package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.gui.tooltip.T7Tooltip
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.*
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.payload.SocketStatePayload
import me.alegian.thavma.impl.common.research.SocketState
import me.alegian.thavma.impl.common.util.Indices
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.phys.Vec2
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.atan2

class SocketWidget(val position: Vec2, private val indices: Indices, private val screen: ResearchScreen) : AbstractWidget(position.x.toInt(), position.y.toInt(), TEXTURE.width, TEXTURE.height, Component.empty()) {
  var state
    get() = screen.menu.researchState?.get(indices) ?: SocketState(indices)
    set(value) = PacketDistributor.sendToServer(SocketStatePayload(value))

  /**
   * this renders only the background of the widget.
   * rendering more things here causes overlaps,
   * so we defer the rest of the rendering to the screen,
   * to do in batches
   */
  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    if (screen.menu.researchState == null) return
    if (screen.menu.completed && state.aspect == null) return
    updateTooltip()
    guiGraphics.usePose {
      translateXY(x, y)
      var texture = TEXTURE
      if (state.broken) texture = BROKEN_TEXTURE
      guiGraphics.blit(texture)
    }
  }

  /**
   * render the connections (above the background)
   * called by the screen in batch
   */
  fun renderConnectionsDeferred(guiGraphics: GuiGraphics) {
    state.aspect?.let {
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
    state.aspect?.let {
      guiGraphics.usePose {
        translateXY(x, y)
        translateXY(TEXTURE.width / 2, TEXTURE.height / 2)
        scaleXY(0.8f)
        translateXY(-8, -8)
        AspectRenderer.drawAspectIcon(guiGraphics, it)
      }
    }
  }

  private fun renderConnections(aspect: Aspect, guiGraphics: GuiGraphics) {
    for (neighborIdx in indices.axial.axialNeighbors) {
      val neighbor = screen.socketWidgets[neighborIdx]
      if (neighbor == null) continue
      if (neighbor.state.aspect?.components?.map { it.get() }?.contains(aspect) != true) continue
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
    if (state.aspect != null || state.broken || state.locked) return
    val carriedAspect = screen.selectedAspect ?: return

    state = state.withAspect(carriedAspect)

    Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(SoundEvents.CHISELED_BOOKSHELF_INSERT, 1.0f, 1.0f))
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    if (state.aspect != null && !state.locked) {
      state = state.withAspect(null).withBroken(true)

      Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(SoundEvents.DEEPSLATE_BREAK, 1.0f, 1.0f))
    }
  }

  private fun updateTooltip() {
    val aspect = state.aspect
    tooltip = if (aspect != null) T7Tooltip(
      Component.translatable(aspect.translationId),
      Component.translatable(removeTranslationId).withStyle(ChatFormatting.GRAY)
    ) else null
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  override fun playDownSound(handler: SoundManager) {
  }

  companion object {
    private const val NAMESPACE = ".socketWidget"
    val TEXTURE = Texture("gui/research_table/socket", 22, 22, 22, 22)
    val BROKEN_TEXTURE = Texture("gui/research_table/broken_socket", 22, 22, 22, 22)
    val removeTranslationId = ResearchScreen.translationId + NAMESPACE + ".remove"
  }
}