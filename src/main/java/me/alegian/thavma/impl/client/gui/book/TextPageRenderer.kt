package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.world.phys.Vec2

object TextPageRenderer : PageRenderer<TextPage> {
  override fun asRenderable(page: TextPage, position: Vec2, size: Vec2): Renderable {
    return Renderable { guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, tickDelta: Float ->
      guiGraphics.usePose {
        translateXY(position.x, position.y)
        guiGraphics.drawString(Minecraft.getInstance().font, page.text)
      }
    }
  }
}