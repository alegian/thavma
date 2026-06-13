package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object RegularNotifLayer : LayeredDraw.Layer {

  //EVERYTHING HERE IS DONE TWICE, ONCE FOR REGULAR NOTIFS (BOTTOM RIGHT)
  // AND ONCE FOR PRIORITY NOTIFS (CENTRE OF SCREEN)

  // ── ① Fade-in ─────────────────────────────────────────────────────────────
  // How long each notification takes to reach full alpha after being added.
  // Per-notification: measured from Notification.addedTime, not batchStartTime.
  // Lines are stationary during this phase.
  private const val FADE_IN_REG = 20L

  // ── ② Static hold ─────────────────────────────────────────────────────────
  // After FADE_IN_DURATION_MS has elapsed from batchStartTime, all lines hold
  // at full alpha for this long before anything moves.
  // Scroll begins at: batchStartTime + FADE_IN_DURATION_MS + STATIC_DELAY_MS
  private const val STATIC_DELAY_REG = 60L

  // ── ③ Scroll-out ──────────────────────────────────────────────────────────
  // SCROLL_SPEED_PX_PER_MS: how fast all lines drift downward (px per ms).
  // FADE_OUT_DISTANCE_PX:   screen pixels before the bottom edge where alpha
  //  begins dropping. Fade duration in ms ≈ distance / speed.
  private const val SCROLL_SPEED_REG = 0.4f
  private const val FADE_DISTANCE_REG = 10f

  // How far above the screen's bottom edge lines initially rest (clears the hotbar).
  private const val BOTTOM_MARGIN_REG = 15f

  private var batchStartTimeReg = -1L
  private var globalScrollOffsetReg = 0f

  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val mc = Minecraft.getInstance()
    val player = mc.player ?: return
    //val currentTime = Util.getMillis()
    val currentTime = player.level().gameTime

    val notifications = PlayerNotifications.getForPlayer(player).filter { !it.isPriority }

    if (notifications.isEmpty()) {
      batchStartTimeReg = -1L
      globalScrollOffsetReg = 0f
      return
    }

    if (batchStartTimeReg < 0L) batchStartTimeReg = currentTime

    val scaledWidth = mc.window.guiScaledWidth
    val scaledHeight = mc.window.guiScaledHeight
    val font = mc.font
    val elapsedReg = currentTime - batchStartTimeReg

    // ── Phase gate ────────────────────────────────────────────────────────
    val inScrollReg = elapsedReg >= FADE_IN_REG + STATIC_DELAY_REG

    globalScrollOffsetReg = if (inScrollReg)
      (elapsedReg - FADE_IN_REG - STATIC_DELAY_REG).toFloat() * SCROLL_SPEED_REG
    else 0f

    // Split each notification into wrapped lines (rendering artifact, not stored)
    // NEEDS SOME TWEAKING
    val regNotifs = notifications
      .take((PlayerNotifications.MAX_VISIBLE_REG / notifications.first().scale).toInt())
      .map { n -> n to font.split(n.text, (scaledWidth / 3.5f / n.scale).toInt()).reversed() }

    // Total pixel height of all rendered rows including inter-group gaps
    val totalHeightReg = regNotifs.sumOf { (n, lines) ->
      ((lines.size + 1) * n.scale * (font.lineHeight - 1.0))
    }.toFloat()

    // Once the topmost line has drifted past the bottom edge, the batch is done
    if (inScrollReg && globalScrollOffsetReg > BOTTOM_MARGIN_REG + totalHeightReg - 20) {
      PlayerNotifications.clearForPlayer(player, false)
      batchStartTimeReg = -1L
      globalScrollOffsetReg = 0f
      return
    }

    var pixelOffsetReg = 0f

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    graphics.enableScissor(scaledWidth * 2 / 3, scaledHeight / 4, scaledWidth, scaledHeight)

    regNotifs.forEach { (notif, lines) ->
      val rowHeight = notif.scale * font.lineHeight

      lines.forEachIndexed { lineIndex, line ->
        val baseY = scaledHeight.toFloat() - BOTTOM_MARGIN_REG -
                (pixelOffsetReg + lineIndex * rowHeight)
        val screenY = baseY + globalScrollOffsetReg

        // ── ① Per-notification fade-in (position fixed, only alpha changes) ──
        var alpha = (((currentTime - notif.addedTime).toFloat() / FADE_IN_REG).coerceIn(0f, 1f) * 255f).toInt()

        // ── ③ Scroll fade-out: alpha drops as line approaches screen bottom ──
        if (inScrollReg) {
          val fadeStartY = scaledHeight.toFloat() - FADE_DISTANCE_REG
          if (screenY > fadeStartY) {
            val fadeProgress =
              ((screenY - fadeStartY) / FADE_DISTANCE_REG).coerceIn(0f, 1f)
            alpha = (alpha * (1f - fadeProgress)).toInt()
          } else alpha = 255
        }

        alpha = alpha.coerceIn(0, 255)
        if (alpha == 0) return@forEachIndexed

        val cr = (notif.color shr 16) and 0xFF
        val cg = (notif.color shr 8) and 0xFF
        val cb = notif.color and 0xFF
        val textArgb = ((alpha / 2) shl 24) or (cr shl 16) or (cg shl 8) or cb
        val lineWidth = font.width(line) * notif.scale

        graphics.pose().pushPose()
        graphics.pose().translate(
          scaledWidth.toFloat() - lineWidth - 12f, screenY, 0f
        )
        graphics.pose().scale(notif.scale, notif.scale, 1f)
        graphics.drawString(font, line, 0, 0, textArgb, true)
        graphics.pose().popPose()

        if (lineIndex == 0) {
          notif.image?.let { tex ->
            RenderSystem.setShaderColor(
              cr / 255f, cg / 255f, cb / 255f, alpha / 511f
            )
            graphics.blit(tex, scaledWidth - 18, screenY.toInt() - 6, 0, 0, 16, 16)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
          }
        }
      }

      pixelOffsetReg += (lines.size + 1) * rowHeight
    }

    graphics.disableScissor()
    RenderSystem.disableBlend()
  }
}