package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.util.minus
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import org.joml.Vector2i

/**
 * By default, connections prefer to connect to children along the Y axis.
 * @param preferX makes connections prefer the X axis.
 * Straight lines will ignore this preference
 */
class EntryRenderable(val tab: TabRenderable, val pos: Vector2i, val children: List<EntryRenderable> = listOf(), val preferX: Boolean = false) : Renderable {
  val tooltipComponent = Component.literal("This is research").withStyle(Rarity.UNCOMMON.styleModifier)

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    renderEntry(guiGraphics)
    for (child in children) {
      val dv = child.pos - pos
      guiGraphics.usePose {
        renderConnectionRecursive(dv.x, dv.y, guiGraphics, child.preferX, false)
      }
    }
  }

  private fun renderEntry(guiGraphics: GuiGraphics) {
    renderGridElement(
      guiGraphics,
      1f,
      1f,
      T7Textures.Thaumonomicon.NODE.location,
      false
    )

    guiGraphics.usePose {
      scaleXY(1 / CELL_SIZE) // back to pixel space
      scaleXY(2f) // items are 16x, nodes are 32x
      guiGraphics.renderItem(Items.DIAMOND.defaultInstance, -8, -8)
    }
  }

  fun renderTooltip(guiGraphics: GuiGraphics) {
    guiGraphics.usePose {
      scaleXY(2f / tab.zoomFactor()) // items are 16x, nodes are 32x
      guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipComponent, 0, 0)
    }
  }

  companion object {
    // TODO: first argument should not be required
    fun of(tab: TabRenderable, entry: ResearchEntry): EntryRenderable {
      return EntryRenderable(tab, entry.position, entry.resolveChildren().map { of(tab, it) }, entry.preferX)
    }
  }
}