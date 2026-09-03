package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FADE_IN_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.STATIC_DELAY_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationOpacity
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationStart
import me.alegian.thavma.impl.client.texture.Texture
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.abs
import kotlin.math.pow

@OnlyIn(Dist.CLIENT)
object NotifAnimationLayer : LayeredDraw.Layer {

  val octSheet = Texture("layer/octant_spritesheet", 400, 485, 400, 8245)
  val shoeSheet = Texture("layer/horseshoe_spritesheet", 450, 470, 450, 5170)
  val fusedSymbol = Texture("layer/combined", 450, 470, 450, 470)
  val coloursOnly = Texture("layer/colours_only", 450, 470, 450, 470)

  // ── Position (fractions of screen dimensions — easy to read and tune) ──
  // 0 = top of screen, 1 = bottom
  private const val SYMBOL_Y_FRAC = 0.625f

  // Distance where each symbol ends up as a fraction of screen width
  private const val MAX_SEPARATION_FRAC = 0.10f

  // How fast sprites fade in/out before/after text starts/finishes scrolling
  const val ANIMATION_SPEED = 3.0f

  // ── Sprite geometry ────────────────────────────────────────────────────
  // Native texture size of each symbol in its sprite sheet
  private val OCT_ORIG_WIDTH = octSheet.width
  private val OCT_ORIG_HEIGHT = octSheet.height
  private val SHOE_ORIG_WIDTH = shoeSheet.width
  private val SHOE_ORIG_HEIGHT = shoeSheet.height

  // How many screen pixels each texture pixel maps to
  private const val SYMBOL_RENDER_SCALE = 0.075f
  private val OCT_DISPLAY_WIDTH get() = OCT_ORIG_WIDTH * SYMBOL_RENDER_SCALE
  private val OCT_DISPLAY_HEIGHT get() = OCT_ORIG_HEIGHT * SYMBOL_RENDER_SCALE
  private val SHOE_DISPLAY_WIDTH get() = SHOE_ORIG_WIDTH * SYMBOL_RENDER_SCALE
  private val SHOE_DISPLAY_HEIGHT get() = SHOE_ORIG_HEIGHT * SYMBOL_RENDER_SCALE

  val timeLimit = (FADE_IN_PRIO + STATIC_DELAY_PRIO).toFloat()
  val fadeInLength = timeLimit / ANIMATION_SPEED  // ≈ 33 ticks
  val splitLength = timeLimit - fadeInLength

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {
    val player = ClientHelper.player() ?: return
    val currentTime = player.level().gameTime

    val scaledWidth = graphics.guiWidth()
    val scaledHeight = graphics.guiHeight()
    val centerXoct = scaledWidth / 2f - OCT_DISPLAY_WIDTH / 2f
    val centerYoct = scaledHeight * SYMBOL_Y_FRAC - SHOE_DISPLAY_HEIGHT / 2f
    val centerXshoe = scaledWidth / 2f - SHOE_DISPLAY_WIDTH / 2f
    val centerYshoe = scaledHeight * SYMBOL_Y_FRAC - SHOE_DISPLAY_HEIGHT / 2f

    // when "a" first priority notification appears, play intro
    if (PriorityNotifLayer.shouldPlayIntro) {
      if (animationOpacity < 1f) {
        renderSystemStart()
        RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)

        // Symbols fade in as one joined sprite
        graphics.blit(
          fusedSymbol.location,
          centerXshoe.toInt(), centerYshoe.toInt(),
          SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),
          0f, 0f,
          SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT,
          shoeSheet.width, shoeSheet.height
        )

        RenderSystem.disableBlend()
      } else {
        renderSystemStart()

        // Raw linear progress through the split phase, 0 → 1
        val rawT = ((currentTime - animationStart).toFloat() - fadeInLength)
          .coerceAtLeast(0f) / splitLength
        val easedT = easeOutSex(rawT.coerceIn(0f, 1f))

        val separationOct = (scaledWidth * MAX_SEPARATION_FRAC * (1 - easedT))
        val separationShoe = ((scaledWidth - SHOE_DISPLAY_WIDTH) * (1 - MAX_SEPARATION_FRAC) * (1 - easedT))

        RenderSystem.setShaderColor(1f, 1f, 1f, 1 - easeInOutCubic(rawT))

        // Horseshoe moves right from centre
        blitSprite(
          graphics,
          shoeSheet.location,
          centerXshoe.toInt() * easedT + separationShoe,
          centerYshoe.toInt(),
          rawT, false
        )

        // Octant moves left from centre
        blitSprite(
          graphics,
          octSheet.location,
          centerXoct.toInt() * easedT + separationOct,
          centerYoct.toInt(),
          rawT, true
        )
      }

      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()
    }

    // when the last line of priority notifications is gone, play outro
    if (PriorityNotifLayer.shouldPlayOutro && !PriorityNotifLayer.shouldPlayIntro) {
      renderSystemStart()

      // Raw linear progress through the split phase, 0 → 1
      val rawT = ((currentTime - PriorityNotifLayer.endCheckpoint).toFloat())
        .coerceAtLeast(0f) / splitLength
      val easedT = easeOutQuadratic(rawT.coerceIn(0f, 1f))

      val separationOct = (scaledWidth * MAX_SEPARATION_FRAC * easedT)
      val separationShoe = (scaledWidth * (1 - MAX_SEPARATION_FRAC) * easedT)

      RenderSystem.setShaderColor(1f, 1f, 1f, rawT)

      if (currentTime - PriorityNotifLayer.endCheckpoint <= splitLength) {

        // Horseshoe moves left from edge toward centre
        blitSprite(
          graphics, shoeSheet.location,
          centerXshoe.toInt() * (1 - easedT) + separationShoe,
          centerYshoe.toInt(), rawT, false
        )

        // Octant moves right from edge toward centre
        blitSprite(
          graphics, octSheet.location,
          centerXoct.toInt() * (1 - easedT) + separationOct,
          centerYoct.toInt(), rawT, true
        )
      }
      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()

      if (currentTime - PriorityNotifLayer.endCheckpoint >= splitLength) {
        renderSystemStart()
        val alpha = (currentTime - PriorityNotifLayer.endCheckpoint - splitLength) / fadeInLength

        RenderSystem.setShaderColor(
          1f,
          1f,
          1f,
          1f - 1.5f * alpha.coerceIn(0f, 1f)
        )

        // Symbols fade out as one joined sprite
        graphics.blit(
          fusedSymbol.location,
          centerXshoe.toInt(), centerYshoe.toInt(),
          SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),
          0f, 0f,
          SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT,
          shoeSheet.width, shoeSheet.height
        )

        RenderSystem.setShaderColor(
          1f,
          1f,
          1f,
          if (alpha <= 0.5f) 2 * alpha.coerceIn(0f, 1f) else 2 * (1 - alpha.coerceIn(0f, 1f))
        )

        // Coloured highlights blink shortly
        graphics.blit(
          coloursOnly.location,
          centerXshoe.toInt(), centerYshoe.toInt(),
          SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),
          0f, 0f,
          SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT,
          shoeSheet.width, shoeSheet.height
        )

        RenderSystem.disableBlend()
      }
    }
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
    if (currentTime - PriorityNotifLayer.endCheckpoint > timeLimit) PriorityNotifLayer.shouldPlayOutro = false
  }

  private fun blitSprite(
    graphics: GuiGraphics,
    location: ResourceLocation,
    x: Float,
    y: Int,
    rawT: Float,
    isOctant: Boolean
  ) {
    if (isOctant) {
      val easedQuad = easeOutQuadratic(rawT)
      val pingPong = 1f - abs((easedQuad * 2f) - 1f)
      val currentFrame = (pingPong * 16).toInt()
      val spriteSheetOffset = 485f * (1 + currentFrame).toFloat()

      graphics.blit(
        location,
        x.toInt(), y,                                                       // screen position
        OCT_DISPLAY_WIDTH.toInt(), OCT_DISPLAY_HEIGHT.toInt(),   // how large to draw on screen (2× scale)
        0f, spriteSheetOffset,                                // UV origin in the texture
        OCT_ORIG_WIDTH, OCT_ORIG_HEIGHT,                      // how many texture pixels to sample
        octSheet.width, octSheet.canvasHeight       // full texture dimensions
      )
    } else {
      val frameIndex = (rawT * 40).toInt() % 9 + 1
      val spriteSheetOffset = (SHOE_ORIG_HEIGHT * frameIndex).toFloat()

      graphics.blit(
        location,
        x.toInt(), y,
        SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),
        0f, spriteSheetOffset,
        SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT,
        shoeSheet.width, shoeSheet.canvasHeight
      )
    }
  }

  private fun easeOutQuadratic(t: Float) = (1f - t) * (1f - t)
  private fun easeOutSex(t: Float) = (1f - t).pow(6)
  private fun easeInOutCubic(t: Float) =
    if (t < 0.5f) 4f * t * t * t
    else 1f - (-2f * t + 2f).pow(3) / 2f

  private fun renderSystemStart() {
    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
  }
}