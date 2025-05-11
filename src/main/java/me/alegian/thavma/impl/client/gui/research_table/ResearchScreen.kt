package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.gui.T7ContainerScreen
import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot
import me.alegian.thavma.impl.common.util.Dimensions
import me.alegian.thavma.impl.common.util.Indices
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
private val BG = Texture("gui/research_table/bg", 231, 154, 256, 256)
private val ASPECTS_BG = Texture("gui/research_table/aspects_bg", 72, 104, 72, 104)
private val PUZZLE_BG = Texture("gui/research_table/puzzle_bg", 144, 144, 144, 144)

open class ResearchScreen(val menu: ResearchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<ResearchMenu>(menu, pPlayerInventory, pTitle, BG) {
  private var page = 0
  private var maxPages = 1
  private var aspectsPerPage = 0
  private val aspectWidgets = mutableListOf<AspectWidget>()
  var selectedAspect: Aspect? = null
  val socketWidgets = mutableMapOf<Indices, SocketWidget>()

  override fun init() {
    aspectWidgets.clear()
    socketWidgets.clear()
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
            width = fixed(RuneSlot.TEXTURE.width)
            height = fixed(RuneSlot.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.runeContainer.range.slot, RuneSlot.TEXTURE))
          }

          Box({ width = grow() }) {}

          Box({
            width = fixed(ScrollSlot.TEXTURE.width)
            height = fixed(ScrollSlot.TEXTURE.height)
          }) {
            addRenderableOnly(slot(menu.scrollContainer.range.slot, ScrollSlot.TEXTURE))
          }
        }

        AspectsSection()
      }

      PuzzleSection()
    }
  }

  private fun AspectsSection() {
    Column({
      height = grow()
      gap = BORDER
    }) {
      TextureBox(ASPECTS_BG) {
        Row({
          size = grow()
          padding = (ASPECTS_BG.width - 16 * 4) / 2 // fits 4 aspects
        }) {
          Row({
            size = grow()
          }) {
            afterLayout {
              makeAspectWidgets(position, size)
            }
          }
        }
      }
      Row({ size = grow() }) {
        Box({
          width = fixed(ButtonWidget.LEFT_TEXTURE.width)
          height = fixed(ButtonWidget.LEFT_TEXTURE.height)
        }) {
          afterLayout {
            addRenderableWidget(ButtonWidget(position, false) {
              // add an extra maxPages to avoid negative modulos
              page = (page - 1 + maxPages) % maxPages
              updateAspectWidgets()
            })
          }
        }
        Box({ size = grow() }) {}
        Box({
          width = fixed(ButtonWidget.RIGHT_TEXTURE.width)
          height = fixed(ButtonWidget.RIGHT_TEXTURE.height)
        }) {
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

  private fun PuzzleSection() {
    TextureBox(PUZZLE_BG) {
      afterLayout {
        makeSocketWidgets(position, size)
      }
    }
  }

  private fun makeSocketWidgets(position: Vec2, containerSize: Vec2) {
    val textureSize = SocketWidget.TEXTURE.size
    val reps = Dimensions(5, 5)
    val gaps = vec2(0, HEX_GRID_GAP)
    val gridSize = reps.vec2
    val actualSize = textureSize * (gridSize + vec2(0, 0.5)) + gaps * (gridSize - 1)
    val offsets = position + (containerSize - actualSize) / 2f

    for (row in 0 until reps.rows) {
      for (col in 0 until reps.cols) {
        val indices = Indices(row, col)
        var totalOffset = offsets + (textureSize + gaps) * indices.vec2
        if (col % 2 == 1) totalOffset += vec2(0, textureSize.y / 2)
        val newWidget = addRenderableWidget(
          SocketWidget(
            totalOffset,
            indices,
            this
          )
        )
        socketWidgets[indices.axial] = newWidget
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)

    // layered rendering of puzzle elements, to avoid overlap issues
    for (widget in socketWidgets.values) widget.renderConnectionsDeferred(guiGraphics)
    for (widget in socketWidgets.values) widget.renderAspectDeferred(guiGraphics)

    renderSelectedAspect(guiGraphics, mouseX, mouseY)
  }

  /**
   * renders the aspect that is being dragged, on top of the mouse
   */
  private fun renderSelectedAspect(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
    selectedAspect?.let {
      guiGraphics.usePose {
        translateXY(mouseX - 8, mouseY - 8)
        AspectRenderer.drawAspectIcon(guiGraphics, it)
      }
    }
  }

  /**
   * makes sure that the event is properly propagated to the socket widget
   */
  override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
    if (selectedAspect != null) focused = null
    val result = super.mouseReleased(mouseX, mouseY, button)
    if (selectedAspect != null) selectedAspect = null
    return result
  }

  companion object {
    val translationId = "container." + Thavma.MODID + ".research_table"
  }
}

