package me.alegian.thavma.impl.client.gui.layout

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.Slot

/**
 * Useful functions that can be called inside Layout Elements,
 * from the Layout API. See LayoutAPI.kt for more.
 */

fun T7LayoutElement.debugRect(color: Int) = Renderable { guiGraphics, _, _, _ ->
  guiGraphics.fill(position.x.toInt(), position.y.toInt(), position.x.toInt() + size.x.toInt(), position.y.toInt() + size.y.toInt(), color)
}

fun T7LayoutElement.text(content: Component, color: Int = 0) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    translate(position.x.toDouble(), position.y.toDouble(), 0.0)
    guiGraphics.drawString(Minecraft.getInstance().font, content, color)
  }
}

fun T7LayoutElement.texture(texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    translate(position.x.toDouble(), position.y.toDouble(), 0.0)
    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    guiGraphics.blit(texture)
    RenderSystem.disableBlend()
  }
}

fun T7LayoutElement.slotGrid(rows: Int, columns: Int, slots: List<Slot>, bgLayers: List<Texture>, slotSize: Int, gap: Int, slotTexture: Texture?) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    translate(position.x.toDouble(), position.y.toDouble(), 0.0)

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

fun T7LayoutElement.slot(slot: Slot, texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
  guiGraphics.usePose {
    translate(position.x.toDouble(), position.y.toDouble(), 0.0)
    guiGraphics.blit(texture)
    if (slot is DynamicSlot<*>) {
      val pos = transformOrigin()
      slot.actualX = pos.x
      slot.actualY = pos.y
      slot.size = texture.width
    }
  }
}