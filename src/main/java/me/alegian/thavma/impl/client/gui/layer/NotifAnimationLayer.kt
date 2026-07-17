package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.OCT_DISPLAY_HEIGHT
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.OCT_DISPLAY_WIDTH
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.OCT_ORIG_HEIGHT
import me.alegian.thavma.impl.client.gui.layer.NotifAnimationLayer.OCT_ORIG_WIDTH
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FADE_IN_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.STATIC_DELAY_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationOpacity
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationStart
import me.alegian.thavma.impl.client.texture.Texture
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.abs
import kotlin.math.pow

@OnlyIn(Dist.CLIENT)
object NotifAnimationLayer : LayeredDraw.Layer {

//  val Asymbol = Texture("layer/symbol_alpha", 8, 7, 8, 7)
//  val Osymbol = Texture("layer/symbol_omega", 8, 7, 8, 7)
//  val FusedSymbol = Texture("layer/symbol_fused", 8, 7, 8, 7)

  // octant - 400 wide, 485 high
  // horseshoe - 450 wide, 470 high
  val octSymbol = Texture("layer/octant", 400, 485, 400, 485)
  val shoeSymbol = Texture("layer/horseshoe", 450, 470, 450, 470)
  val octSheet = Texture("layer/octant_spritesheet", 400, 485, 400, 8245)
  val shoeSheet = Texture("layer/horseshoe_spritesheet", 450, 470, 450, 5170)

  val FusedSymbol = Texture("layer/combined", 400, 470, 450, 470)

  val timeLimit = FADE_IN_PRIO + STATIC_DELAY_PRIO

  // ── Position (fractions of screen dimensions — easy to read and tune) ──
  // 0 = top of screen, 1 = bottom
  private const val SYMBOL_Y_FRAC = 0.625f

  // Distance where each symbol ends up as a fraction of screen width
  private const val MAX_SEPARATION_FRAC = 0.10f

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

  private fun easeOutQuadratic(t: Float) = (1f - t) * (1f - t)
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
    val centerXoct = scaledWidth / 2f - OCT_DISPLAY_WIDTH / 2f
    val centerYoct = scaledHeight * SYMBOL_Y_FRAC - SHOE_DISPLAY_HEIGHT / 2f
    val centerXshoe = scaledWidth / 2f - SHOE_DISPLAY_WIDTH / 2f
    val centerYshoe = scaledHeight * SYMBOL_Y_FRAC - SHOE_DISPLAY_HEIGHT / 2f

    if (PriorityNotifLayer.shouldPlayIntro) {
      if (animationOpacity < 1f) {

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)

        graphics.blit(
          FusedSymbol.location,
          centerXshoe.toInt(), centerYshoe.toInt(),               // screen position
          SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),   // how large to draw on screen (2× scale)
          0f, 0f,                  // UV origin in the texture
          SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT, // how many texture pixels to sample
          shoeSheet.width, shoeSymbol.height              // full texture dimensions
        )

        RenderSystem.disableBlend()
      } else {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

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
        val separationO = ((scaledWidth - SHOE_DISPLAY_WIDTH) * (1 - MAX_SEPARATION_FRAC) * (1 - easedT))

        RenderSystem.setShaderColor(1f, 1f, 1f, 1 - easeInOutCubic(rawT))

        // Omega moves left from centre
        blitSprite(
          graphics,
          shoeSheet.location,
          centerXshoe.toInt() * easedT + separationO,
          centerYshoe.toInt(),
          false,
          470f / SYMBOL_RENDER_SCALE * 3 * ((rawT * 50).toInt() % 10)
        )

        // Alpha moves right from centre
        blitSprite(
          graphics,
          octSheet.location,
          centerXoct.toInt() * easedT + separationA,
          centerYoct.toInt(),
          true,
          485f * -(1 + ((abs(85f - easeOutQuadratic(rawT) * 170).toInt() / 6) % 17))
        )
      }

      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()
    }

    if (PriorityNotifLayer.shouldPlayOutro && !PriorityNotifLayer.shouldPlayIntro) {
      RenderSystem.enableBlend()
      RenderSystem.defaultBlendFunc()

      // ── Phase 2: symbols separate outward from centre ──────────────
      val timeLimit = (FADE_IN_PRIO + STATIC_DELAY_PRIO).toFloat()
      // Fade-in phase ends at this many ticks after animationStart
      val fadeInLength = timeLimit / PriorityNotifLayer.ANIMATION_SPEED  // ≈ 33 ticks
      // Separation phase runs for the remaining ticks
      val splitLength = timeLimit - fadeInLength                         // ≈ 67 ticks

      // Raw linear progress through the split phase, 0 → 1
      val rawT = ((currentTime - PriorityNotifLayer.endCheckpoint).toFloat())
        .coerceAtLeast(0f) / splitLength
      // Eased progress — swap easing function here freely
      val easedT = easeOutQuadratic(rawT.coerceIn(0f, 1f))

      val separationA = (scaledWidth * MAX_SEPARATION_FRAC * easedT)
      val separationO = (scaledWidth * (1 - MAX_SEPARATION_FRAC) * easedT)

      RenderSystem.setShaderColor(1f, 1f, 1f, rawT)

      if (currentTime - PriorityNotifLayer.endCheckpoint <= splitLength) {

        // Omega moves left from centre
        blitSprite(
          graphics, shoeSheet.location,
          centerXshoe.toInt() * (1 - easedT) + separationO, centerYshoe.toInt(), false, 0f
        )

        // Alpha moves right from centre
        blitSprite(
          graphics, octSheet.location,
          centerXoct.toInt() * (1 - easedT) + separationA, centerYoct.toInt(), true, 485f
        )
      }
      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()

      if (currentTime - PriorityNotifLayer.endCheckpoint >= splitLength) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        val alpha = (currentTime - PriorityNotifLayer.endCheckpoint - splitLength) / fadeInLength

        RenderSystem.setShaderColor(
          1f,
          1f,
          1f,
          1f - alpha.coerceIn(0f, 1f)
        )

        graphics.blit(
          FusedSymbol.location,
          centerXshoe.toInt(), centerYshoe.toInt(),               // screen position
          SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),   // how large to draw on screen (2× scale)
          0f, 0f,                  // UV origin in the texture
          SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT, // how many texture pixels to sample
          shoeSheet.width, shoeSymbol.height              // full texture dimensions
        )

        RenderSystem.disableBlend()
      }
    }
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    RenderSystem.disableBlend()
    if (currentTime - PriorityNotifLayer.endCheckpoint > timeLimit) PriorityNotifLayer.shouldPlayOutro = false
  }


  /**
   * Renders an [OCT_ORIG_WIDTH]×[OCT_ORIG_HEIGHT] region from a 256×256 texture
   * at [OCT_DISPLAY_WIDTH]×[OCT_DISPLAY_HEIGHT] screen pixels — all in plain screen coordinates,
   * no confusing pose matrix scaling involved.
   */
  private fun blitSprite(
    graphics: GuiGraphics,
    location: ResourceLocation,
    x: Float,
    y: Int,
    isOctant: Boolean,
    spriteSheetOffset: Float
  ) {
    if (isOctant)
      graphics.blit(
        location,
        x.toInt(), y,               // screen position
        OCT_DISPLAY_WIDTH.toInt(), OCT_DISPLAY_HEIGHT.toInt(),   // how large to draw on screen (2× scale)
        0f, spriteSheetOffset,                  // UV origin in the texture
        OCT_ORIG_WIDTH, OCT_ORIG_HEIGHT, // how many texture pixels to sample
        octSheet.width, octSheet.canvasHeight              // full texture dimensions
      )
    else graphics.blit(
      location,
      x.toInt(), y,               // screen position
      SHOE_DISPLAY_WIDTH.toInt(), SHOE_DISPLAY_HEIGHT.toInt(),   // how large to draw on screen (2× scale)
      0f, spriteSheetOffset,                  // UV origin in the texture
      SHOE_ORIG_WIDTH, SHOE_ORIG_HEIGHT, // how many texture pixels to sample
      shoeSheet.width, shoeSheet.canvasHeight              // full texture dimensions
    )
  }
}