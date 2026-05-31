package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.clientSound
import me.alegian.thavma.impl.client.gui.tooltip.T7Tooltip
import me.alegian.thavma.impl.client.pushScreen
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.resetRenderSystemColor
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.entity.knowsResearch
import me.alegian.thavma.impl.common.payload.ResearchScrollPayload
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.minus
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.sounds.SoundManager
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.neoforged.neoforge.network.PacketDistributor

/**
 * By default, connections prefer to connect to children along the Y axis.
 * entry.preferX makes connections prefer the X axis.
 * Straight lines will ignore this preference
 */
class EntryWidget(private val screen: BookScreen, val tab: TabRenderable, val entry: Holder<ResearchEntry>) :
  AbstractWidget(0, 0, CELL_SIZE, CELL_SIZE, entry.value().title) {
  private var gaveScroll = false
  val knowsResearch =
    ClientHelper.player()?.knowsResearch(entry) ?: false
  val knowsParents =
    ClientHelper.player()?.let { player ->
      val parents = entry.value().parents(player.level())
      parents.all { player.knowsResearch(it) }
    } ?: false
  val children = entry.value().children

  init {
    val components = mutableListOf(entry.value().title)
    if (!knowsParents) components.add(Component.translatable(ResearchEntry.PARENTS_UNKNOWN_TRANSLATION).withStyle(ChatFormatting.GRAY))
    tooltip = T7Tooltip(components)
  }

  private val pos = entry.value().position

  override fun getX(): Int {
    return ((pos.x * CELL_SIZE - CELL_SIZE / 2 - tab.scrollX) / tab.zoomFactor() + screen.width / 2).toInt()
  }

  override fun getY(): Int {
    return ((pos.y * CELL_SIZE - CELL_SIZE / 2 - tab.scrollY) / tab.zoomFactor() + screen.height / 2).toInt()
  }

  override fun getWidth(): Int {
    return (CELL_SIZE / tab.zoomFactor()).toInt()
  }

  override fun getHeight(): Int {
    return (CELL_SIZE / tab.zoomFactor()).toInt()
  }

  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    this.isHovered = guiGraphics.containsPointInScissor(mouseX, mouseY)
        && mouseX >= x
        && mouseY >= y
        && mouseX < x + getWidth()
        && mouseY < y + getHeight()

    guiGraphics.usePose {
      translateXY(screen.width / 2, screen.height / 2)
      scaleXY(1 / tab.zoomFactor())
      translateXY(-tab.scrollX, -tab.scrollY)
      scaleXY(CELL_SIZE)
      translateXY(pos.x, pos.y)

      renderEntry(guiGraphics)

      if (!knowsResearch) return@usePose
      // allows negative size drawing, which greatly simplifies math
      RenderSystem.disableCull()
      for (child in children) {
        val dv = child.value().position - pos
        guiGraphics.usePose {
          renderConnectionRecursive(dv.x, dv.y, guiGraphics, child.value().preferX, false)
        }
      }
      RenderSystem.enableCull()
    }
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    if (!knowsResearch && knowsParents && !gaveScroll) {
      PacketDistributor.sendToServer(ResearchScrollPayload(entry))
      clientSound(SoundEvents.BOOK_PAGE_TURN, SoundSource.AMBIENT, 1f, 1f)
      gaveScroll = true
      tooltip = T7Tooltip(entry.value().title, Component.translatable(ResearchEntry.SCROLL_GIVEN_TRANSLATION).withStyle(ChatFormatting.GRAY))
      return
    }
    if (knowsResearch)
      pushScreen(EntryScreen(entry))
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  private fun renderEntry(guiGraphics: GuiGraphics) {
    var brightness = 1f
    if (!knowsResearch) brightness = 0.4f
    RenderSystem.setShaderColor(brightness, brightness, brightness, 1f)

    renderGridElement(
      guiGraphics,
      1f,
      1f,
      TEXTURE.location,
      false
    )

    guiGraphics.usePose {
      scaleXY(1f / CELL_SIZE) // back to pixel space
      scaleXY(2 * 0.7) // items are 16x, nodes are 32x, but we don't want full size
      guiGraphics.renderItem(entry.value().icon, -8, -8)
    }

    resetRenderSystemColor()
  }

  override fun playDownSound(handler: SoundManager) {
    handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0f, 1.0f))
  }

  companion object {
    val TEXTURE = Texture("gui/book/node", 32, 32)
  }
}