package me.alegian.thavma.impl.client.gui.thaumonomicon.renderable

import me.alegian.thavma.impl.client.gui.thaumonomicon.CELL_SIZE
import me.alegian.thavma.impl.client.gui.thaumonomicon.renderConnectionRecursive
import me.alegian.thavma.impl.client.gui.thaumonomicon.renderGridElement
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus

/**
 * By default, connections prefer to connect to children along the Y axis.
 * @param preferX makes connections prefer the X axis.
 * Straight lines will ignore this preference
 */
class EntryRenderable(val tab: Tab, val pos: Vec2, val children: List<EntryRenderable> = listOf(), val preferX: Boolean = false) : Renderable {
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
}
