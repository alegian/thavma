package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.screen.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.AspectStack
import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import me.alegian.thavma.impl.init.registries.deferred.Aspects.PRIMAL_ASPECTS
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import kotlin.math.abs
import kotlin.math.cos

private val WORKBENCH_BG = Texture("gui/container/arcane_workbench/bg", 214, 132, 256, 256)
private val RESULT_SLOT = Texture("gui/container/arcane_workbench/result_slot", 34, 34)
private val WAND_SLOT = Texture("gui/container/arcane_workbench/wand_slot", 20, 20)
private val ASPECT_SOCKET = Texture("gui/container/arcane_workbench/aspect_socket", 25, 25)
private val SLOTS = (0..8).map { Texture("gui/container/arcane_workbench/crafting_slots/$it", 21, 21) }
private const val BORDER = 5
private const val WOOD_SIZE = 122

open class WorkbenchScreen(val menu: WorkbenchMenu, pPlayerInventory: Inventory, pTitle: Component) : T7ContainerScreen<WorkbenchMenu>(menu, pPlayerInventory, pTitle, WORKBENCH_BG) {
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
        Box({
          width = fixed(SLOTS[0].width * 3)
          height = fixed(SLOTS[0].height * 3)
        }) {
          addRenderableOnly(slotGrid(3, 3, menu.craftingContainer.range.slots) { i, j -> SLOTS[i * 3 + j] })
        }
      }

      Column({
        size = grow()
        align = Alignment.CENTER
        gap = GAP
      }) {
        Box({
          width = fixed(WAND_SLOT.width)
          height = fixed(WAND_SLOT.height)
        }) {
          addRenderableOnly(slot(menu.wandContainer.range.slot, WAND_SLOT))
        }

        Box({
          width = fixed(RESULT_SLOT.width)
          height = fixed(RESULT_SLOT.height)
        }) {
          addRenderableOnly(slot(menu.resultContainer.range.slot, RESULT_SLOT))
        }
      }
    }
  }

  override fun init() {
    super.init()
    addRenderableOnly(renderAspects)
  }

  // TODO: cleanup
  protected open val renderAspects = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    val BASE_RADIUS = 56
    val ANGLE = 360f / PRIMAL_ASPECTS.size
    val middleSlot = menu.craftingContainer.range.slots[4]
    if (middleSlot !is DynamicSlot<*>) return@Renderable

    guiGraphics.usePose {
      translateXY(middleSlot.actualX.toDouble(), middleSlot.actualY.toDouble())

      // draw aspects at hexagon points (or N-gon if more primals are added by addons)
      for ((i, a) in PRIMAL_ASPECTS.withIndex()) {
        val requiredAmount = menu.requiredAspects[a.get()]
        val requiredStack = AspectStack(a.get(), requiredAmount)
        guiGraphics.usePose {
          rotateZ(ANGLE * i)
          val fac = abs(cos(2 * Math.PI / PRIMAL_ASPECTS.size * i))
          translateXY((BASE_RADIUS * (1 - 0.16 * fac * fac)), 0.0)
          rotateZ(-ANGLE * i)
          guiGraphics.usePose {
            translateXY((SLOTS[0].width - ASPECT_SOCKET.width) / 2.0, (SLOTS[0].height - ASPECT_SOCKET.height) / 2.0)
            guiGraphics.blit(ASPECT_SOCKET)
          }
          if (requiredAmount != 0) AspectRenderer.renderAspect(guiGraphics, requiredStack, (SLOTS[0].width - AspectRenderer.PIXEL_RESOLUTION) / 2, (SLOTS[0].height - AspectRenderer.PIXEL_RESOLUTION) / 2)
        }
      }
    }
  }
}