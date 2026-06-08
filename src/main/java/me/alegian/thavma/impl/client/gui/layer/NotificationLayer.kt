package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.Util
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.pow

@OnlyIn(Dist.CLIENT)
object NotificationLayer : LayeredDraw.Layer {

  private const val ROW_HEIGHT = 8f * 1.2f  // screen-space pixels per notification row

  // Scroll animation: when new entries are pushed in, this offset springs from
  // ROW_HEIGHT toward 0, making existing entries appear to slide upward smoothly.
  private var scrollAnim = 0f
  private var prevCount = 0
  private var lastFrameMs = 0L

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {
    val width = graphics.guiWidth()
    val height = graphics.guiHeight()
    val mc = Minecraft.getInstance()
    val level = mc.level ?: return
    val player = mc.player ?: return
    val now = Util.getMillis()
    val dt = (now - lastFrameMs).coerceIn(0L, 100L).toFloat()
    lastFrameMs = now

    val active = PlayerNotifications.pollActive(now)

    // Pulse the scroll animation whenever the queue grows
    if (active.size > prevCount) {
      scrollAnim += ROW_HEIGHT * (active.size - prevCount)
    }
    prevCount = active.size

    // Exponential decay — frame-rate independent
    scrollAnim *= 0.85f.pow(dt / 16f)
    if (scrollAnim < 0.05f) scrollAnim = 0f

    if (active.isEmpty()) return

    val sw = mc.window.guiScaledWidth
    val sh = mc.window.guiScaledHeight
    val font = mc.font

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

    val visible = active.take(PlayerNotifications.MAX_VISIBLE)

    visible.forEachIndexed { i, n ->

      // ── Alpha & slide ──────────────────────────────────────────────────
      var alpha = 255
      var shift = 0f  // upward nudge during fade-out

      // Fade-in: applies only to the newest visible entry while still arriving
      if (i == visible.lastIndex && n.created > now) {
        val progress = (n.created - now).toFloat() / PlayerNotifications.FADE_IN_DURATION_MS
        alpha = (255 - progress * 240f).toInt()
      }

      // Fade-out: begins one DISPLAY_DURATION before the entry expires
      if (n.expire < now + PlayerNotifications.DISPLAY_DURATION_MS) {
        val progress = (now + PlayerNotifications.DISPLAY_DURATION_MS - n.expire).toFloat() /
                PlayerNotifications.DISPLAY_DURATION_MS
        alpha = (255 - progress * 240f).toInt()
        shift = -8f * (alpha / 255f)  // slide entry upward as it fades
      }

      alpha = alpha.coerceIn(0, 255)

      // ── Color channels ─────────────────────────────────────────────────
      val cr = (n.color shr 16) and 0xFF
      val cg = (n.color shr 8) and 0xFF
      val cb = n.color and 0xFF

      // Text: half-alpha, notification color (ARGB packed)
      val textArgb = ((alpha / 2) shl 24) or (cr shl 16) or (cg shl 8) or cb

      // ── Position ───────────────────────────────────────────────────────
      val scale = n.fontSize
      val textPx = (font.width(n.text) * scale).toInt() // actual rendered pixel width
      val yPos = sh - i * ROW_HEIGHT + shift + scrollAnim

      // ── Text ───────────────────────────────────────────────────────────
      // Translate to right-aligned position, then scale — text drawn at local (0,0)
      graphics.pose().pushPose()
      graphics.pose().translate((sw - textPx - 12).toFloat(), yPos, 0f)
      graphics.pose().scale(scale, scale, 1f)
      graphics.drawString(font, n.text, 0, 0, textArgb, false)
      graphics.pose().popPose()

      // ── Icon (optional) ────────────────────────────────────────────────
      // Tinted with notification color at half the text's alpha, matching original
      n.image?.let { tex ->
        RenderSystem.setShaderColor(cr / 255f, cg / 255f, cb / 255f, alpha / 511f)
        graphics.blit(tex, sw - 18, yPos.toInt() - 6, 0, 0, 16, 16)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      }
    }

    RenderSystem.disableBlend()
  }
}