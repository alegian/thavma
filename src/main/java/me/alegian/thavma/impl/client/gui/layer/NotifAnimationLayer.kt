package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.DISPLAY_H
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.DISPLAY_W
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.SPRITE_H_TEX
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.SPRITE_W_TEX
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FADE_IN_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FusedSymbol
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.STATIC_DELAY_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationOpacity
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationStart
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.pow

@OnlyIn(Dist.CLIENT)
object NotifAnimationLayer : LayeredDraw.Layer {

  val timeLimit = FADE_IN_PRIO + STATIC_DELAY_PRIO

  // ── Position (fractions of screen dimensions — easy to read and tune) ──
  // 0 = top of screen, 1 = bottom
  private const val SYMBOL_Y_FRAC = 0.6f

  // How far each symbol travels from centre, as a fraction of screen width
  private const val MAX_SEPARATION_FRAC = 0.15f

  // ── Sprite geometry ────────────────────────────────────────────────────
  // Native texture size of each symbol in its sprite sheet
  private const val SPRITE_W_TEX = 8
  private const val SPRITE_H_TEX = 7

  // How many screen pixels each texture pixel maps to
  private const val SYMBOL_RENDER_SCALE = 2
  private val DISPLAY_W get() = SPRITE_W_TEX * SYMBOL_RENDER_SCALE  // 16 px
  private val DISPLAY_H get() = SPRITE_H_TEX * SYMBOL_RENDER_SCALE  // 14 px

  // ── Easing library — swap freely ───────────────────────────────────────
  // All take t ∈ [0,1] and return a value in [0,1]
  private fun easeOutQuad(t: Float) = (1f - t) * (1f - t)
  private fun easeOutCubic(t: Float) = (1f - t).pow(3)
  private fun easeOutQuart(t: Float) = (1f - t).pow(4)
  private fun easeOutQuint(t: Float) = (1f - t).pow(5)
  private fun easeOutSex(t: Float) = (1f - t).pow(6)
  private fun easeInOutCubic(t: Float) =
    if (t < 0.5f) 4f * t * t * t
    else 1f - (-2f * t + 2f).pow(3) / 2f

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {

    val mc = Minecraft.getInstance()
    val player = mc.player ?: return
    val currentTime = player.level().gameTime

    val scaledWidth = graphics.guiWidth()
    val scaledHeight = graphics.guiHeight()
    val centerX = scaledWidth / 2f //- DISPLAY_W / 2f
    val centerY = scaledHeight * SYMBOL_Y_FRAC //- DISPLAY_H / 2f

    if (PriorityNotifLayer.shouldPlayIntro) {
      if (animationOpacity < 1f) {


        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)

        blitSprite(
          graphics, FusedSymbol.location,
          centerX, centerY.toInt()
        )

//        graphics.pose().pushPose()
//        val xPos = 230f
//        val yPos = 160f
//        graphics.pose().translate(xPos, yPos, 1f)
//        graphics.pose().scale(1 / 14f, 0.0625f, 1f)
//        graphics.blit(FusedSymbol)
//        graphics.blit(
//          ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_fused.png"),
//          0,
//          0,
//          0,
//          0,
//          256,
//          256
//        )
//        graphics.pose().popPose()

        RenderSystem.disableBlend()
      }

//      RenderSystem.enableBlend()
//      RenderSystem.defaultBlendFunc()
//      RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)
//
//      if (animationOpacity < 1f) {
//        graphics.pose().pushPose()
//        graphics.pose().scale(2f, 2f, 1f)
//        graphics.blit(
//          FusedSymbol.location,
//          scaledWidth / 2 - 4,
//          (scaledHeight - scaledHeight / 2.4f - 3).toInt(),
//          0,
//          0,
//          8,
//          7
//        )
//        graphics.pose().popPose()
//      }
      else {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        //RenderSystem.setShaderColor(1f, 1f, 1f, 1 / 0.85.ow(5).toFloat())

        // ── Phase 2: symbols separate outward from centre ──────────────
        val timeLimit = (FADE_IN_PRIO + STATIC_DELAY_PRIO).toFloat()
        // Fade-in phase ends at this many ticks after animationStart
        val fadeInLength = timeLimit / PriorityNotifLayer.ANIMATION_SPEED  // ≈ 33 ticks
        // Separation phase runs for the remaining ticks
        val splitLength = timeLimit - fadeInLength                         // ≈ 67 ticks

        // Raw linear progress through the split phase, 0 → 1
        val rawT = ((currentTime - animationStart).toFloat() - fadeInLength)
          .coerceAtLeast(0f) / splitLength
        // Eased progress — swap easing function here freely
        val easedT = easeOutSex(rawT.coerceIn(0f, 1f))

        // Separation in screen pixels at this frame (0 at start, maxSep at end)
        val separationA = (scaledWidth * MAX_SEPARATION_FRAC * (1 - easedT))
        val separationO = (scaledWidth * (1 - MAX_SEPARATION_FRAC) * (1 - easedT))

        RenderSystem.setShaderColor(1f, 1f, 1f, 1 - easeInOutCubic(rawT))

        // Alpha moves right from centre
        blitSprite(
          graphics, PriorityNotifLayer.Asymbol.location,
          centerX.toInt() * easedT + separationA, centerY.toInt()
        )

        // Omega moves left from centre
        blitSprite(
          graphics, PriorityNotifLayer.Osymbol.location,
          centerX.toInt() * easedT + separationO, centerY.toInt()
        )

//        graphics.pose().pushPose()
//        val xPos = 230f
//        val yPos = 160f
//        graphics.pose().translate(xPos, yPos, 1f)
//        graphics.pose().scale(1 / 14f, 0.0625f, 1f)
////        graphics.blit(
////          ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_alpha.png"),
////          (xPos + 0.5.pow(-dexponential.toDouble().pow(-5)).toInt()).toInt(),
////          0,
////          0,
////          0,
////          256,
////          256
////        )
//        graphics.blit(
//          ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_omega.png"),
//          ((6440*easedT+ (6440) * (1-easedT)).toInt()),
//          0,
//          0,
//          0,
//          256,
//          256
//        )
//        graphics.pose().popPose()
      }

      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()
  }
  }

  /**
   * Renders an [SPRITE_W_TEX]×[SPRITE_H_TEX] region from a 256×256 texture
   * at [DISPLAY_W]×[DISPLAY_H] screen pixels — all in plain screen coordinates,
   * no confusing pose matrix scaling involved.
   */
  private fun blitSprite(graphics: GuiGraphics, location: ResourceLocation, x: Float, y: Int) {
    graphics.blit(
      location,
      x.toInt(), y,               // screen position
      DISPLAY_W, DISPLAY_H,   // how large to draw on screen (2× scale)
      0f, 0f,                  // UV origin in the texture
      SPRITE_W_TEX, SPRITE_H_TEX, // how many texture pixels to sample
      8, 7                // full texture dimensions
    )
  }
}
//}