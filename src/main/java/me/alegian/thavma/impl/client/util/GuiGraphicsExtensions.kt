package me.alegian.thavma.impl.client.util

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor

fun GuiGraphics.usePose(block: PoseStack.() -> Unit) {
  pose().use(block)
}

fun GuiGraphics.drawString(font: Font, text: String, color: Int = 0) {
  drawString(font, text, 0, 0, color, false)
}

fun GuiGraphics.drawString(font: Font, text: Component, color: Int = 0) {
  drawString(font, text, 0, 0, color, false)
}

fun GuiGraphics.drawSeethroughString(font: Font, text: String, x: Float, y: Float, color: Int, shadow: Boolean) {
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

fun GuiGraphics.blit(
  pX: Int,
  pY: Int,
  pBlitOffset: Int,
  pWidth: Int,
  pHeight: Int,
  pSprite: TextureAtlasSprite,
  color: Int
) = innerBlit(
  pSprite.atlasLocation(),
  pX,
  pX + pWidth,
  pY,
  pY + pHeight,
  pBlitOffset,
  pSprite.u0,
  pSprite.u1,
  pSprite.v0,
  pSprite.v1,
  FastColor.ARGB32.red(color) / 255f,
  FastColor.ARGB32.green(color) / 255f,
  FastColor.ARGB32.blue(color) / 255f,
  FastColor.ARGB32.alpha(color) / 255f
)

fun GuiGraphics.setColor(color: Int) = setColor(
  FastColor.ARGB32.red(color) / 255f,
  FastColor.ARGB32.green(color) / 255f,
  FastColor.ARGB32.blue(color) / 255f,
  FastColor.ARGB32.alpha(color) / 255f
)

fun GuiGraphics.resetColor() = setColor(1f, 1f, 1f, 1f)

fun GuiGraphics.enableCrop(pX: Int, pY: Int) =
  enableScissor(pX, pY, guiWidth() - pX, guiHeight() - pY)

fun GuiGraphics.disableCrop() = disableScissor()

