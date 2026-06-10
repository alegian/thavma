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

  // Scroll animation: when new entries are pushed in, this offset springs from
  // ROW_HEIGHT toward 0, making existing entries appear to slide upward smoothly.
  private var scrollAnimation = 0f
  private var queuedNotifs = 0
  private var timestamp = 0L

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {
    val width = graphics.guiWidth()
    val height = graphics.guiHeight()
    val mc = Minecraft.getInstance()
    val level = mc.level ?: return
    val player = mc.player ?: return
    val currentTime = Util.getMillis()

    val activeNotifs = PlayerNotifications.pollActive(currentTime, player)
    if (activeNotifs.isEmpty()) return

    val passedTime = (currentTime - timestamp).coerceIn(0L, 100L).toFloat()
    timestamp = currentTime
    val scaledWidth = mc.window.guiScaledWidth
    val scaledHeight = mc.window.guiScaledHeight
    val font = mc.font

    //tohle celý můžu hodit do toho foreachindexed

    activeNotifs.mapIndexed { index, notif ->
      index to font.split(
        notif.text,
        (scaledWidth.toFloat() / 3f / notif.scale).toInt()
      )
    }.forEach { PlayerNotifications.addFormatted(it.second, activeNotifs[it.first]) }
    val activeListList = PlayerNotifications.pollActiveFormatted(currentTime, player)
    val activeFlatList = activeListList.flatten()
    val activeNumber = activeFlatList.size

    val ROW_HEIGHT = 8f * activeListList.first().first().scale  // screen-space pixels per notification row

    // Pulse the scroll animation whenever the queue grows
    if (activeNumber > queuedNotifs) {
      scrollAnimation += ROW_HEIGHT * (activeNumber - queuedNotifs)
    }
    queuedNotifs = activeNumber

    // Exponential decay — frame-rate independent
    //!!! <- originally 16f
    scrollAnimation *= 0.85f.pow(passedTime / 8f) //!!!
    if (scrollAnimation < 0.05f) scrollAnimation = 0f


    val intermediateList = activeFlatList.take(PlayerNotifications.MAX_VISIBLE)
    val visibleNotifs = mutableListOf<List<PlayerNotifications.FormattedCharSeqNotification>>()
    var counter = 0
    var tracker = intermediateList.size
    // count how many empty lines there will be
    while (tracker > 0) {
      if (tracker >= activeListList[counter].size) {
        tracker -= activeListList[counter].size
        visibleNotifs += activeListList[counter].toList()
        counter += 1
      } else {
        // take the remaining lines
        visibleNotifs += activeListList[counter].take(tracker)
        break
      }
    }

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

    var offsetHelp = 0

    visibleNotifs.forEachIndexed { index, notifications ->
      notifications.forEachIndexed { rank, notif ->
        // ── Alpha & slide ──────────────────────────────────────────────────
        var alpha = 255
        var shift = 0f  // upward nudge during fade-out

        // Fade-in: applies only to the newest visible entry while still arriving
        if (index == visibleNotifs.lastIndex && rank == notifications.lastIndex && notif.created > currentTime) {
          val progress = (notif.created - currentTime).toFloat() / PlayerNotifications.FADE_IN_DURATION_MS
          alpha = (255 - progress * 240f).toInt()
        }

        // Fade-out: begins one DISPLAY_DURATION before the entry expires
        if (notif.expire < currentTime + PlayerNotifications.DISPLAY_DURATION_MS) {
          val progress = (currentTime + PlayerNotifications.DISPLAY_DURATION_MS - notif.expire).toFloat() /
                  PlayerNotifications.DISPLAY_DURATION_MS
          alpha = (255 - progress * 240f).toInt()
          shift = -8f * (alpha / 255f)  // slide entry upward as it fades
          //scrollAnimation -= 8f * (alpha / 255f)
        }

        alpha = alpha.coerceIn(0, 255)

        // ── Color channels ─────────────────────────────────────────────────
        val red = (notif.color shr 16) and 0xFF
        val green = (notif.color shr 8) and 0xFF
        val blue = notif.color and 0xFF

        // Text: half-alpha, notification color (ARGB packed)
        val textArgb = ((alpha / 2) shl 24) or (red shl 16) or (green shl 8) or blue

        // ── Position ───────────────────────────────────────────────────────
//      val scale = notif.scale
//      val lines = font.split(notifications.text, (scaledWidth.toFloat() / 3f / scale).toInt())

        //for ((rank, line) in lines.withIndex()) {
        //val lineWidth = (font.width(notification.text) * scale).toInt() // actual rendered pixel width
        val lineWidth = font.width(notif.text) * notif.scale // actual rendered pixel width
        val verticalOffset = scaledHeight - (rank + offsetHelp) * ROW_HEIGHT + shift + scrollAnimation

        // ── Text ───────────────────────────────────────────────────────────
        // Translate to right-aligned position, then scale — text drawn at local (0,0)
        graphics.pose().pushPose()
        graphics.pose().translate((scaledWidth - lineWidth - 12), verticalOffset, 0f)
        graphics.pose().scale(notif.scale, notif.scale, 1f)
        graphics.drawString(font, notif.text, 0, 0, textArgb, true)
        graphics.pose().popPose()

        // ── Icon (optional) ────────────────────────────────────────────────
        // Tinted with notification color at half the text's alpha, matching original
        notif.image?.let { texture ->
          RenderSystem.setShaderColor(red / 255f, green / 255f, blue / 255f, alpha / 511f)
          graphics.blit(texture, scaledWidth - 18, verticalOffset.toInt() - 6, 0, 0, 16, 16)
          RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        }
      }
      offsetHelp += notifications.size + 1
    }

    RenderSystem.disableBlend()
  }
}