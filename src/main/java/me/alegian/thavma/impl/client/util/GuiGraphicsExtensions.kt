package me.alegian.thavma.impl.client.util

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

fun GuiGraphics.usePose(block: PoseStack.() -> Unit) {
  pose().use(block)
}

fun GuiGraphics.drawString(font: Font, text: String, color: Int = 0) {
  drawString(font, text, 0, 0, color, false)
}

fun GuiGraphics.drawString(font: Font, text: Component, color: Int = 0) {
  drawString(font, text, 0, 0, color, false)
}

fun GuiGraphics.drawSeethroughString(font: Font, text: String, x: Float, y: Float, color: Int, shadow: Boolean){
  font.drawInBatch(
    text,
    x,
    y,
    color,
    shadow,
    pose().last().pose(),
    bufferSource(),
    Font.DisplayMode.SEE_THROUGH,
    0,
    15728880,
    font.isBidirectional
  )
  this.flush() // flushing here is necessary to prevent depth bugs
}


fun GuiGraphics.drawOutlinedSeethroughString(font: Font, text: String, color: Int = 0, outlineColor: Int = 0) {
  drawSeethroughString(font, text, -1f, 0f, outlineColor, false)
  drawSeethroughString(font, text, 1f, 0f, outlineColor, false)
  drawSeethroughString(font, text, 0f, -1f, outlineColor, false)
  drawSeethroughString(font, text, 0f, 1f, outlineColor, false)
  drawSeethroughString(font, text, 0f, 0f, color, false)
}

fun GuiGraphics.blit(texture: Texture) {
  blit(texture.location, 0, 0, 0f, 0f, texture.width, texture.height, texture.canvasWidth, texture.canvasHeight)
}