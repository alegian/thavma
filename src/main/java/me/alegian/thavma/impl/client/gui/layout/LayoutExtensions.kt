package me.alegian.thavma.impl.client.gui.layout

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.Slot
import net.minecraft.world.phys.Vec2

/**
 * Useful functions that can be called inside Layout Elements,
 * from the Layout API. See LayoutAPI.kt for more.
 */

object LayoutExtensions {
  var currScreen: Screen? = null
}

fun debugRect(size: Vec2, color: Int) = Renderable { guiGraphics, _, _, _ ->
  guiGraphics.fill(0, 0, size.x.toInt(), size.y.toInt(), color)
}

fun text(content: Component, color: Int = 0) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.drawString(Minecraft.getInstance().font, content, color)
}

private fun renderableTexture(texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  RenderSystem.enableBlend()
  RenderSystem.defaultBlendFunc()
  guiGraphics.blit(texture)
  RenderSystem.disableBlend()
}

fun slotGrid(rows: Int, columns: Int, slots: List<Slot>, bgLayers: List<Texture>, slotSize: Int, gap: Int, slotTexture: Texture?) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    for (bgTexture in bgLayers)
      guiGraphics.blit(bgTexture)

    for (i in 0 until rows) {
      guiGraphics.usePose {
        for (j in 0 until columns) {
          slotTexture?.let(guiGraphics::blit)
          val slot = slots[i * columns + j]
          if (slot is DynamicSlot<*>) {
            val pos = transformOrigin()
            slot.actualX = pos.x
            slot.actualY = pos.y
            slot.size = slotSize
          }
          translate((slotSize + gap).toDouble(), 0.0, 0.0)
        }
      }
      translate(0.0, (slotSize + gap).toDouble(), 0.0)
    }
  }
}

fun slot(slot: Slot, texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    guiGraphics.blit(texture)
    if (slot is DynamicSlot<*>) {
      val pos = transformOrigin()
      slot.actualX = pos.x
      slot.actualY = pos.y
      slot.size = texture.width
    }
  }
}

fun relativeRenderable(renderable: Renderable) {
  afterLayout {
    val screen = LayoutExtensions.currScreen ?: throw IllegalStateException("Thavma Exception: cannot add renderable without setting LayoutExtensions.currScreen first!")
    screen.renderables.add(Renderable { guiGraphics, mouseX, mouseY, partialTick ->
      guiGraphics.usePose {
        translateXY(position.x, position.y)
        renderable.render(guiGraphics, mouseX, mouseY, partialTick)
      }
    })
  }
}

fun TextureBox(texture: Texture, children: T7LayoutElement.() -> Unit) =
  Row({
    width = fixed(texture.width)
    height = fixed(texture.height)
  }) {
    relativeRenderable(renderableTexture(texture))
    children()
  }
