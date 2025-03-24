package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.screen.layout.Box
import me.alegian.thavma.impl.client.screen.layout.debugRect
import me.alegian.thavma.impl.client.screen.layout.fixed
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
    Box({
      size = fixed(80f)
    }){
      addRenderableOnly(debugRect(0xFFFF0000.toInt()))
    }
//    Padding(BORDER) {
//      Row {
//        Box(Modifier().width(WOOD_SIZE).center()) {
//          Box(Modifier().width(SLOTS[0].width * 3).height(SLOTS[0].height * 3).center().color(0xFF0000FF.toInt())) {
//            addRenderableOnly(slotGrid(3, 3, menu.craftingContainer.range.slots) { i, j -> SLOTS[i * 3 + j] })
//          }
//        }
//        Box(Modifier().width(BORDER))
//        Box {
//          Box(Modifier().center()) {
//            Box(Modifier().size(RESULT_SLOT)) {
//              addRenderableOnly(slot(menu.resultContainer.range.slot, RESULT_SLOT))
//            }
//          }
//          Box(Modifier().maxHeight(0.5f).center()) {
//            Box(Modifier().size(WAND_SLOT)) {
//              addRenderableOnly(slot(menu.wandContainer.range.slot, WAND_SLOT))
//            }
//          }
//        }
//      }
//    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)
    renderAspects(guiGraphics)
  }

  // TODO: cleanup
  protected open fun renderAspects(guiGraphics: GuiGraphics) {
    val BASE_RADIUS = 56
    val ANGLE = 360f / PRIMAL_ASPECTS.size
    val middleSlot = menu.craftingContainer.range.slots[4]
    if (middleSlot !is DynamicSlot<*>) return

    guiGraphics.usePose {
      translateXY(leftPos.toDouble(), topPos.toDouble())
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