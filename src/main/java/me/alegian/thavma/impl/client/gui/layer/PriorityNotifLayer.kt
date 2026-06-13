package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.Util
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object PriorityNotifLayer : LayeredDraw.Layer {

  // ── ① Fade-in ─────────────────────────────────────────────────────────────
  // How long each notification takes to reach full alpha after being added.
  // Per-notification: measured from Notification.addedTime, not batchStartTime.
  // Lines are stationary during this phase.
  private const val FADE_IN_PRIO = 750L

  // ── ② Static hold ─────────────────────────────────────────────────────────
  // After FADE_IN_DURATION_MS has elapsed from batchStartTime, all lines hold
  // at full alpha for this long before anything moves.
  // Scroll begins at: batchStartTime + FADE_IN_DURATION_MS + STATIC_DELAY_MS
  private const val STATIC_DELAY_PRIO = 2_000L

  // ── ③ Scroll-out ──────────────────────────────────────────────────────────
  // SCROLL_SPEED_PX_PER_MS: how fast all lines drift downward (px per ms).
  // FADE_OUT_DISTANCE_PX:   screen pixels before the bottom edge where alpha
  //  begins dropping. Fade duration in ms ≈ distance / speed.
  private const val SCROLL_SPEED_PRIO = 0.01f
  private const val FADE_DISTANCE_PRIO = 40f

  // How far above the screen's bottom edge lines initially rest (clears the hotbar).
  private const val BOTTOM_MARGIN_PRIO = 50f


  private var batchStartTimePrio = -1L
  private var globalScrollOffsetPrio = 0f


  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val mc = Minecraft.getInstance()
    val player = mc.player ?: return
    val currentTime = Util.getMillis()

    val notifications = PlayerNotifications.getForPlayer(player).filter { it.isPriority }

    if (notifications.isEmpty()) {
      batchStartTimePrio = -1L
      globalScrollOffsetPrio = 0f
      return
    }

    if (batchStartTimePrio < 0L) batchStartTimePrio = currentTime

    val scaledWidth = mc.window.guiScaledWidth
    val scaledHeight = mc.window.guiScaledHeight
    val font = mc.font
    val elapsedPrio = currentTime - batchStartTimePrio

    val inScrollPrio = elapsedPrio >= FADE_IN_PRIO + STATIC_DELAY_PRIO

    globalScrollOffsetPrio = if (inScrollPrio)
      (elapsedPrio - FADE_IN_PRIO - STATIC_DELAY_PRIO).toFloat() * SCROLL_SPEED_PRIO
    else 0f

    val prioNotifs = notifications
      .take(PlayerNotifications.MAX_VISIBLE_PRIO)
      .map { n -> n to font.split(n.text, (scaledWidth / 3f / n.scale).toInt()).reversed() }

    val totalHeightPrio = prioNotifs.sumOf { (n, lines) ->
      ((lines.size + 1) * n.scale * 8.0)
    }.toFloat()

    // Once the topmost line has drifted past the bottom edge, the batch is done
    if (inScrollPrio && globalScrollOffsetPrio > BOTTOM_MARGIN_PRIO + totalHeightPrio) {
      PlayerNotifications.clearForPlayer(player, true)
      batchStartTimePrio = -1L
      globalScrollOffsetPrio = 0f
      return
    }

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

    var pixelOffsetPrio = 0f

    prioNotifs.forEach { (notif, lines) ->
      val rowHeight = notif.scale * 8f

      lines.forEachIndexed { lineIndex, line ->
        val baseY = scaledHeight.toFloat() - BOTTOM_MARGIN_PRIO -
                (pixelOffsetPrio + lineIndex * rowHeight)
        val screenY = baseY + globalScrollOffsetPrio

        // ── ① Per-notification fade-in (position fixed, only alpha changes) ──
        var alpha = if (!inScrollPrio) {
          val age = (currentTime - notif.addedTime).toFloat()
          ((age / FADE_IN_PRIO).coerceIn(0f, 1f) * 255f).toInt()
        } else 255  // snap to full at scroll start — no jump since age ≥ FADE_IN by then

        // ── ③ Scroll fade-out: alpha drops as line approaches screen bottom ──
        if (inScrollPrio) {
          val fadeStartY = scaledHeight.toFloat() - FADE_DISTANCE_PRIO
          if (screenY > fadeStartY) {
            val fadeProgress =
              ((screenY - fadeStartY) / FADE_DISTANCE_PRIO).coerceIn(0f, 1f)
            alpha = (alpha * (1f - fadeProgress)).toInt()
          }
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

      pixelOffsetPrio += (lines.size + 1) * rowHeight
    }

    RenderSystem.disableBlend()

  }
}