package me.alegian.thavma.impl.client.gui

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

open class WorkbenchScreen(val menu: WorkbenchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<WorkbenchMenu>(menu, pPlayerInventory, pTitle, WORKBENCH_BG) {
  companion object {
    private const val GAP = 4
    private val WORKBENCH_BG = Texture("gui/arcane_workbench/bg", 214, 132, 256, 256)
    private val RESULT_SLOT = Texture("gui/arcane_workbench/result_slot", 34, 34)
    private val WAND_SLOT = Texture("gui/arcane_workbench/wand_slot", 20, 20)
    private val ASPECT_SOCKET = Texture("gui/arcane_workbench/aspect_socket", 25, 25)
    private val GRID = Texture("gui/arcane_workbench/grid", 62, 62)
    private val GRID_OVERLAY = Texture("gui/arcane_workbench/grid_overlay", 62, 62)
    private const val BORDER = 5
    private const val WOOD_SIZE = 122
  }

  override fun layout() {
    Row({
      size = grow()
      padding = BORDER
      gap = BORDER
    }) {
      Column({
        width = fixed(WOOD_SIZE)
        height = grow()
        align = Alignment.CENTER
      }) {
        SlotGrid(3, 3, menu.craftingContainer.range.slots, listOf(GRID, GRID_OVERLAY), 1, slotSize = 20)
      }

      Column({
        size = grow()
        align = Alignment.CENTER
        gap = GAP
      }) {
        Slot(menu.wandContainer.range.slot, WAND_SLOT)

        Slot(menu.resultContainer.range.slot, RESULT_SLOT)
      }
    }
  }

  override fun init() {
    super.init()
    addRenderableOnly(renderAspects)
  }

  // TODO: cleanup
  protected open val renderAspects = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    val BASE_RADIUS = 50
    val ANGLE = 360f / Aspects.PRIMAL_ASPECTS.size
    val middleSlot = menu.craftingContainer.range.slots[4]
    if (middleSlot !is DynamicSlot<*>) return@Renderable

    guiGraphics.usePose {
      translateXY(middleSlot.actualX.toDouble(), middleSlot.actualY.toDouble() + 5)

      // draw aspects at pentagon points (or N-gon if more primals are added by addons)
      for ((i, a) in Aspects.PRIMAL_ASPECTS.withIndex()) {
        val requiredAmount = menu.requiredAspects[a.get()]
        val requiredStack = AspectStack(a.get(), requiredAmount)
        guiGraphics.usePose {
          rotateZ(ANGLE * i)
          translateXY(0.0, -BASE_RADIUS)
          rotateZ(-ANGLE * i)
          guiGraphics.usePose {
            translateXY((middleSlot.size - ASPECT_SOCKET.width) / 2.0, (middleSlot.size - ASPECT_SOCKET.height) / 2.0)
            guiGraphics.blit(ASPECT_SOCKET)
          }
          if (requiredAmount != 0) {
            translateXY((middleSlot.size - 16) / 2, (middleSlot.size - 16) / 2)
            AspectRenderer.renderAspect(guiGraphics, requiredStack)
          }
        }
      }
    }
  }
}