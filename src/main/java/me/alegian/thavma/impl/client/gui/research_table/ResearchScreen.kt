package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.gui.T7ContainerScreen
import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.plus
import me.alegian.thavma.impl.common.util.trunc
import me.alegian.thavma.impl.common.util.vec2
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.div
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.times

private const val BORDER = 5
private val BG = Texture("gui/research_table/bg", 243, 166, 256, 256)
private val ASPECTS_BG = Texture("gui/research_table/aspects_bg", 72, 121, 72, 121)
private val PUZZLE_BG = Texture("gui/research_table/puzzle_bg", 156, 156, 156, 156)

open class ResearchScreen(val menu: ResearchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<ResearchMenu>(menu, pPlayerInventory, pTitle, BG) {
  override fun layout() {
    Row({
      size = grow()
      padding = BORDER
      gap = BORDER
    }) {
      Column({
        gap = BORDER
        height = grow()
      }) {
        Row({ width = grow() }) {
          Box({
            width = fixed(RuneSlot.Companion.TEXTURE.width)
            height = fixed(RuneSlot.Companion.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.runeContainer.range.slot, RuneSlot.Companion.TEXTURE))
          }

          Box({ width = grow() }) {}

          Box({
            width = fixed(ScrollSlot.Companion.TEXTURE.width)
            height = fixed(ScrollSlot.Companion.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.scrollContainer.range.slot, ScrollSlot.Companion.TEXTURE))
          }
        }
        Column({ height = grow() }) {
          TextureBox(ASPECTS_BG) { }
          Row({ size = grow() }) {
            Box({ size = grow() }) {
              afterLayout {
                addRenderableWidget(ButtonWidget(position, false) { })
              }
            }
            Box({ size = grow() }) {
              afterLayout {
                addRenderableWidget(ButtonWidget(position, true) { })
              }
            }
          }
        }
      }

      TextureBox(PUZZLE_BG) {
        Row({
          size = grow()
        }) {
          Box({ size = grow() }) {
            afterLayout {
              makePuzzleWidgets(position, size)
            }
          }
        }
      }
    }
  }

  private fun makePuzzleWidgets(position: Vec2, containerSize: Vec2) {
    val textureSize = CircleWidget.TEXTURE.size
    val gaps = 4f
    var reps = ((containerSize + gaps) / (textureSize + gaps)).trunc()
    val actualSize = textureSize * reps + (reps - 1) * gaps
    val offsets = position + (containerSize - actualSize) / 2f

    for (i in 0 until reps.x.toInt()) {
      for (j in 0 until reps.y.toInt()) {
        addRenderableWidget(
          CircleWidget(
            offsets + (textureSize + gaps) * vec2(i, j)
          )
        )
      }
    }
  }
}

class CircleWidget(val position: Vec2) : AbstractWidget(position.x.toInt(), position.y.toInt(), TEXTURE.width, TEXTURE.height, Component.empty()) {
  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    guiGraphics.usePose {
      translateXY(x, y)
      guiGraphics.blit(TEXTURE)
    }
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  companion object {
    val TEXTURE = Texture("gui/research_table/circle", 18, 18, 18, 18)
  }
}