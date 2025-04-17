package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.Row
import me.alegian.thavma.impl.client.gui.layout.grow
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.TextPage
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable

object TextPageRenderer : PageRenderer<TextPage> {
  override fun initPage(screen: EntryScreen, page: TextPage) {
    Row({
      size = grow()
    }) {
     afterLayout {
       screen.addRenderableOnly(Renderable {guiGraphics, mouseX, mouseY, tickDelta ->
         guiGraphics.usePose {
           translateXY(position.x, position.y)
           guiGraphics.drawString(Minecraft.getInstance().font, page.text)
         }
       })
     }
    }
  }
}