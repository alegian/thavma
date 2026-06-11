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
object NotificationLayer : LayeredDraw.Layer {

  // ── ① Fade-in ─────────────────────────────────────────────────────────────
  // How long each notification takes to reach full alpha after being added.
  // Per-notification: measured from Notification.addedTime, not batchStartTime.
  // Lines are stationary during this phase.
  private const val FADE_IN_PLAIN = 750L
  private const val FADE_IN_VOICE = 750L

  // ── ② Static hold ─────────────────────────────────────────────────────────
  // After FADE_IN_DURATION_MS has elapsed from batchStartTime, all lines hold
  // at full alpha for this long before anything moves.
  // Scroll begins at: batchStartTime + FADE_IN_DURATION_MS + STATIC_DELAY_MS
  private const val STATIC_DELAY_PLAIN = 2_000L
  private const val STATIC_DELAY_VOICE = 2_000L

  // ── ③ Scroll-out ──────────────────────────────────────────────────────────
  // SCROLL_SPEED_PX_PER_MS: how fast all lines drift downward (px per ms).
  // FADE_OUT_DISTANCE_PX:   screen pixels before the bottom edge where alpha
  //                          begins dropping. Fade duration in ms ≈ distance / speed.
  private const val SCROLL_SPEED_PX_PER_MS_PLAIN = 0.01f   // 60 px / second
  private const val SCROLL_SPEED_PX_PER_MS_VOICE = 0.01f   // 60 px / second
  private const val FADE_DISTANCE_PLAIN = 40f
  private const val FADE_DISTANCE_VOICE = 40f

  // How far above the screen's bottom edge lines initially rest (clears the hotbar).
  private const val BOTTOM_MARGIN_PLAIN = 50f
  private const val BOTTOM_MARGIN_VOICE = 50f

  private var batchStartTimePlain = -1L
  private var batchStartTimeVoice = -1L
  private var globalScrollOffsetPlain = 0f
  private var globalScrollOffsetVoice = 0f

  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val mc = Minecraft.getInstance()
    val player = mc.player ?: return
    val now = Util.getMillis()

    val notifications = PlayerNotifications.getForPlayer(player)

    if (notifications.isEmpty()) {
      batchStartTimePlain = -1L
      batchStartTimeVoice = -1L
      globalScrollOffsetPlain = 0f
      globalScrollOffsetVoice = 0f
      return
    }

    // Latch batch start time once on first non-empty render
    if (batchStartTimePlain < 0L) batchStartTimePlain = now
    if (batchStartTimeVoice < 0L) batchStartTimeVoice = now

    val scaledWidth = mc.window.guiScaledWidth
    val scaledHeight = mc.window.guiScaledHeight
    val font = mc.font
    val elapsedPlain = now - batchStartTimePlain
    val elapsedVoice = now - batchStartTimeVoice

    // ── Phase gate ────────────────────────────────────────────────────────
    val inScrollPlain = elapsedPlain >= FADE_IN_PLAIN + STATIC_DELAY_PLAIN
    val inScrollVoice = elapsedVoice >= FADE_IN_VOICE + STATIC_DELAY_VOICE

    globalScrollOffsetPlain = if (inScrollPlain)
      (elapsedPlain - FADE_IN_PLAIN - STATIC_DELAY_PLAIN).toFloat() * SCROLL_SPEED_PX_PER_MS_PLAIN
    else 0f

    globalScrollOffsetVoice = if (inScrollVoice)
      (elapsedVoice - FADE_IN_VOICE - STATIC_DELAY_VOICE).toFloat() * SCROLL_SPEED_PX_PER_MS_VOICE
    else 0f

    val partition = notifications.partition { it.isVoice }

    // Split each notification into wrapped lines (rendering artifact, not stored)
    // NEEDS SOME TWEAKING
    val plainNotifs = partition.second
      .take(PlayerNotifications.MAX_VISIBLE_PLAIN)
      .map { n -> n to font.split(n.text, (scaledWidth / 3f / n.scale).toInt()).reversed() }

    val voiceNotifs = partition.first
      .take(PlayerNotifications.MAX_VISIBLE_VOICE)
      .map { n -> n to font.split(n.text, (scaledWidth / 3f / n.scale).toInt()).reversed() }

    // Total pixel height of all rendered rows including inter-group gaps
    val totalHeightPlain = plainNotifs.sumOf { (n, lines) ->
      ((lines.size + 1) * n.scale * 8.0)
    }.toFloat()
    val totalHeightVoice = voiceNotifs.sumOf { (n, lines) ->
      ((lines.size + 1) * n.scale * 8.0)
    }.toFloat()

    // Once the topmost line has drifted past the bottom edge, the batch is done
    if (inScrollPlain && globalScrollOffsetPlain > BOTTOM_MARGIN_PLAIN + totalHeightPlain) {
      PlayerNotifications.clearForPlayer(player, false)
      batchStartTimePlain = -1L
      globalScrollOffsetPlain = 0f
      return
    }

    if (inScrollVoice && globalScrollOffsetVoice > BOTTOM_MARGIN_VOICE + totalHeightVoice) {
      PlayerNotifications.clearForPlayer(player, true)
      batchStartTimeVoice = -1L
      globalScrollOffsetVoice = 0f
      return
    }

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

    var pixelOffsetPlain = 0f
    var pixelOffsetVoice = 0f

    plainNotifs.forEach { (notif, lines) ->
      val rowHeight = notif.scale * 8f

      lines.forEachIndexed { lineIndex, line ->
        val baseY = scaledHeight.toFloat() - BOTTOM_MARGIN_PLAIN -
                (pixelOffsetPlain + lineIndex * rowHeight)
        val screenY = baseY + globalScrollOffsetPlain

        // ── ① Per-notification fade-in (position fixed, only alpha changes) ──
        var alpha = if (!inScrollPlain) {
          val age = (now - notif.addedTime).toFloat()
          ((age / FADE_IN_PLAIN).coerceIn(0f, 1f) * 255f).toInt()
        } else 255  // snap to full at scroll start — no jump since age ≥ FADE_IN by then

        // ── ③ Scroll fade-out: alpha drops as line approaches screen bottom ──
        if (inScrollPlain) {
          val fadeStartY = scaledHeight.toFloat() - FADE_DISTANCE_PLAIN
          if (screenY > fadeStartY) {
            val fadeProgress =
              ((screenY - fadeStartY) / FADE_DISTANCE_PLAIN).coerceIn(0f, 1f)
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

      pixelOffsetPlain += (lines.size + 1) * rowHeight
    }

    //MUST TURN THIS WHOLE THING INTO VOICE VERSION
    plainNotifs.forEach { (notif, lines) ->
      val rowHeight = notif.scale * 8f

      lines.forEachIndexed { lineIndex, line ->
        val baseY = scaledHeight.toFloat() - BOTTOM_MARGIN_PLAIN -
                (pixelOffsetPlain + lineIndex * rowHeight)
        val screenY = baseY + globalScrollOffsetPlain

        // ── ① Per-notification fade-in (position fixed, only alpha changes) ──
        var alpha = if (!inScrollPlain) {
          val age = (now - notif.addedTime).toFloat()
          ((age / FADE_IN_PLAIN).coerceIn(0f, 1f) * 255f).toInt()
        } else 255  // snap to full at scroll start — no jump since age ≥ FADE_IN by then

        // ── ③ Scroll fade-out: alpha drops as line approaches screen bottom ──
        if (inScrollPlain) {
          val fadeStartY = scaledHeight.toFloat() - FADE_DISTANCE_PLAIN
          if (screenY > fadeStartY) {
            val fadeProgress =
              ((screenY - fadeStartY) / FADE_DISTANCE_PLAIN).coerceIn(0f, 1f)
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

      pixelOffsetPlain += (lines.size + 1) * rowHeight
    }

    RenderSystem.disableBlend()
  }
}