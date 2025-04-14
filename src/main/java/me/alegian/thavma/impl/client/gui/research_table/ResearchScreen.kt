package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.gui.T7ContainerScreen
import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.vec2
import me.alegian.thavma.impl.init.registries.T7Registries
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.div
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.times
import kotlin.math.ceil

private const val BORDER = 5
private val BG = Texture("gui/research_table/bg", 243, 166, 256, 256)
private val ASPECTS_BG = Texture("gui/research_table/aspects_bg", 72, 121, 72, 121)
private val PUZZLE_BG = Texture("gui/research_table/puzzle_bg", 156, 156, 156, 156)

open class ResearchScreen(val menu: ResearchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<ResearchMenu>(menu, pPlayerInventory, pTitle, BG) {
  private var page = 0
  private var maxPages = 1
  private var aspectsPerPage = 0
  private val aspectWidgets = mutableListOf<AspectWidget>()
  var selectedAspect: Aspect? = null
  val reseachState = mutableMapOf<Pair<Int, Int>, Aspect>()
  val circleWidgets = mutableMapOf<Pair<Int, Int>, CircleWidget>()

  override fun init() {
    aspectWidgets.clear()
    circleWidgets.clear()
    super.init()
  }

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

        AspectsSection()
      }

      PuzzleSection()
    }
  }

  fun AspectsSection() {
    Column({ height = grow() }) {
      TextureBox(ASPECTS_BG) {
        afterLayout {
          makeAspectWidgets(position, size)
        }
      }
      Row({ size = grow() }) {
        Box({ size = grow() }) {
          afterLayout {
            addRenderableWidget(ButtonWidget(position, false) {
              // add an extra maxPages to avoid negative modulos
              page = (page - 1 + maxPages) % maxPages
              updateAspectWidgets()
            })
          }
        }
        Box({ size = grow() }) {
          afterLayout {
            addRenderableWidget(ButtonWidget(position, true) {
              page = (page + 1) % maxPages
              updateAspectWidgets()
            })
          }
        }
      }
    }
  }

  /**
   * places the aspect widgets, calculates pages required
   */
  private fun makeAspectWidgets(position: Vec2, size: Vec2) {
    val allAspects = T7Registries.ASPECT.toList()
    val maxRows = (size.y / 16).toInt()
    aspectsPerPage = maxRows * 4
    maxPages = ceil(allAspects.size.toFloat() / aspectsPerPage).toInt()

    for (a in allAspects.indices) {
      val i = a / 4
      val j = a % 4
      val newWidget = addRenderableWidget(
        AspectWidget(
          position + vec2(j * 16, 0) + vec2(0, (i % maxRows) * 16),
          this,
          allAspects[a]
        )
      )
      aspectWidgets.add(newWidget)
    }

    updateAspectWidgets()
  }

  /**
   * hides/unhides aspects as pages change
   */
  private fun updateAspectWidgets() {
    for (i in aspectWidgets.indices) {
      val enabled = i >= page * aspectsPerPage && i < (page + 1) * aspectsPerPage
      aspectWidgets[i].active = enabled
      aspectWidgets[i].visible = enabled
    }
  }

  fun PuzzleSection() {
    TextureBox(PUZZLE_BG) {
      afterLayout {
        makePuzzleWidgets(position, size)
      }
    }
  }

  private fun makePuzzleWidgets(position: Vec2, containerSize: Vec2) {
    val textureSize = CircleWidget.TEXTURE.size
    val reps = vec2(7, 5)
    val gaps = vec2(0, HEX_GRID_GAP)
    val actualSize = textureSize * (reps + vec2(0, 0.5)) + gaps * (reps - 1)
    val offsets = position + (containerSize - actualSize) / 2f

    for (row in 0 until reps.y.toInt()) {
      for (col in 0 until reps.x.toInt()) {
        val indices = vec2(col, row)
        var totalOffset = offsets + (textureSize + gaps) * indices
        if (col % 2 == 1) totalOffset += vec2(0, textureSize.y / 2)
        val newWidget = addRenderableWidget(
          CircleWidget(
            totalOffset,
            indices,
            this
          )
        )
        circleWidgets[axial(indices).toIntPair()] = newWidget
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)
    renderSelectedAspect(guiGraphics, mouseX, mouseY)
  }

  /**
   * renders the aspect that is being dragged, on top of the mouse
   */
  private fun renderSelectedAspect(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
    selectedAspect?.let {
      AspectRenderer.drawAspectIcon(guiGraphics, it, mouseX - 8, mouseY - 8)
    }
  }

  /**
   * makes sure that the event is properly propagated to the circle widget
   */
  override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
    if (selectedAspect != null) focused = null
    val result = super.mouseReleased(mouseX, mouseY, button)
    if (selectedAspect != null) selectedAspect = null
    return result
  }
}

