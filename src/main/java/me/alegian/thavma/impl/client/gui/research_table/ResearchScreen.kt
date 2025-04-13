package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.client.gui.T7ContainerScreen
import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.menu.ResearchMenu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.trunc
import me.alegian.thavma.impl.common.util.vec2
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.sounds.SoundManager
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

  override fun init() {
    aspectWidgets.clear()
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
    val ALL_ASPECTS = 35
    val maxRows = (size.y / 16).toInt()
    aspectsPerPage = maxRows * 4
    maxPages = ceil(ALL_ASPECTS.toFloat() / aspectsPerPage).toInt()

    for (a in 0 until ALL_ASPECTS) {
      val i = a / 4
      val j = a % 4
      val newWidget = addRenderableWidget(
        AspectWidget(
          position + vec2(j * 16, 0) + vec2(0, (i % maxRows) * 16),
          this,
          Aspects.IGNIS.get()
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
    val gaps = vec2(2, -2)
    var reps = ((containerSize + gaps) / (textureSize + gaps)).trunc()
    val actualSize = textureSize * reps + (reps - 1) * gaps
    val offsets = position + (containerSize - actualSize) / 2f

    for (j in 0 until reps.y.toInt()) {
      var rowSize = reps.x.toInt()
      if (j % 2 == 1) rowSize--
      for (i in 0 until rowSize) {
        var totalOffset = offsets + (textureSize + gaps) * vec2(i, j)
        if (j % 2 == 1) totalOffset += vec2(textureSize.x / 2 + gaps.x / 2, 0)
        addRenderableWidget(
          CircleWidget(
            totalOffset,
            vec2(i, j),
            this
          )
        )
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)
    renderSelectedAspect(guiGraphics, mouseX, mouseY)
  }

  private fun renderSelectedAspect(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
    selectedAspect?.let {
      AspectRenderer.drawAspectIcon(guiGraphics, it, mouseX - 8, mouseY - 8)
    }
  }

  override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
    if (selectedAspect != null) focused = null
    val result = super.mouseReleased(mouseX, mouseY, button)
    if (selectedAspect != null) selectedAspect = null
    return result
  }
}

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
        scaleXY(0.8f)
        AspectRenderer.drawAspectIcon(guiGraphics, it, -8, -8)
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

class AspectWidget(position: Vec2, private val researchScreen: ResearchScreen, private val aspect: Aspect) : AbstractWidget(position.x.toInt(), position.y.toInt(), 16, 16, Component.empty()) {
  override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    AspectRenderer.drawAspectIcon(guiGraphics, aspect, x, y)
  }

  override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
  }

  override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
    researchScreen.selectedAspect = aspect
  }

  override fun playDownSound(handler: SoundManager) {
  }
}