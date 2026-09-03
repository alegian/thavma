package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object PriorityNotifLayer : LayeredDraw.Layer {

  const val FADE_IN_PRIO = 40L
  const val STATIC_DELAY_PRIO = 60L
  private const val SCROLL_SPEED_PRIO = 0.4f
  private var batchStartTimePrio = -1L
  private var globalScrollOffsetPrio = 0f
  const val MARGIN_CONST = 2.4f

  var shouldPlayIntro = false
  var shouldPlayOutro = false
  var endCheckpoint = -1L
  var animationOpacity = 1f
  var animationStart = -1L

  val mc = Minecraft.getInstance()

  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val player = mc.player ?: return
    val currentTime = player.level().gameTime

    val notifications = PlayerNotifications.getForPlayer(player).filter { it.isPriority }

    if (notifications.isEmpty()) {
      batchStartTimePrio = -1L
      globalScrollOffsetPrio = 0f
      return
    }

    val scaledWidth = mc.window.guiScaledWidth
    val scaledHeight = mc.window.guiScaledHeight
    val font = mc.font

    // this defines the heights at which lines of text will fade in/out
    val bottomMargin = scaledHeight / MARGIN_CONST
    val fadeInStart = scaledHeight * 17 / 32f
    val fadeInEnd = scaledHeight * 9 / 16f
    val fadeOutEnd = scaledHeight * 3 / 4f - scaledHeight / 16f
    val fadeOutStart = scaledHeight * 3 / 4f - scaledHeight / 8f

    if (batchStartTimePrio < 0L) {
      batchStartTimePrio = currentTime
      endCheckpoint = -1L
    }

    val elapsedPrio = currentTime - batchStartTimePrio
    val inScrollPrio = elapsedPrio >= FADE_IN_PRIO + STATIC_DELAY_PRIO

    shouldPlayIntro = currentTime - batchStartTimePrio < FADE_IN_PRIO + STATIC_DELAY_PRIO
    animationOpacity =
      (NotifAnimationLayer.ANIMATION_SPEED * (currentTime - batchStartTimePrio) / (FADE_IN_PRIO + STATIC_DELAY_PRIO)).coerceIn(
        0f,
        1f
      )
    animationStart = batchStartTimePrio

    val prioNotifs = notifications
      .take(PlayerNotifications.MAX_LISTABLE_PRIO)
      .map { n -> n to font.split(n.text, (scaledWidth * 6 / 10f / n.scale).toInt()) }

    globalScrollOffsetPrio = if (inScrollPrio)
      (elapsedPrio - FADE_IN_PRIO - STATIC_DELAY_PRIO).toFloat() * SCROLL_SPEED_PRIO //* prioNotifs.first().first.scale
    else 0f

    val totalHeightPrio = prioNotifs.sumOf { (n, lines) ->
      ((lines.size + 1) * n.scale * (font.lineHeight - 1.0))
    }.toFloat()

    // Once the topmost line has drifted past the bottom edge, the batch is done
    if (inScrollPrio && globalScrollOffsetPrio > totalHeightPrio)//-prioNotifs.first().first.scale * (font.lineHeight - 1))
    {
      PlayerNotifications.clearForPlayer(player, true)
      batchStartTimePrio = -1L
      globalScrollOffsetPrio = 0f
      endCheckpoint = currentTime
      shouldPlayOutro = true
      return
    }

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

    graphics.enableScissor(0, scaledHeight * 9 / 16 + 1, scaledWidth, scaledHeight * 3 / 4 - scaledHeight / 15)

    var pixelOffsetPrio = 0f

    prioNotifs.forEach { (notif, lines) ->
      val rowHeight = notif.scale * (font.lineHeight - 1)

      lines.forEachIndexed { lineIndex, line ->
        val baseY = scaledHeight.toFloat() - bottomMargin +
                (pixelOffsetPrio + (lineIndex + 0.5f) * rowHeight)
        val screenY = baseY - globalScrollOffsetPrio

        var alpha = 255
        when {
          screenY < fadeInStart -> alpha = 0
          screenY in fadeInStart..fadeInEnd -> alpha =
            (255 - (fadeInEnd - screenY) / (fadeInEnd - fadeInStart) * 255).toInt()

          screenY in fadeInEnd..fadeOutStart -> alpha = 255
          screenY in fadeOutStart..fadeOutEnd -> alpha =
            ((fadeOutEnd - screenY) / (fadeOutEnd - fadeOutStart) * 255).toInt()

          screenY > fadeOutEnd -> alpha = 0
        }

        val multiplier = if (currentTime - batchStartTimePrio < FADE_IN_PRIO)
          ((currentTime - notif.addedTime).toFloat() / FADE_IN_PRIO).coerceIn(0f, 1f)
        else 1f

        alpha = (alpha * multiplier).toInt().coerceIn(0, 255)

        if (alpha == 0) return@forEachIndexed

        val cr = (notif.color shr 16) and 0xFF
        val cg = (notif.color shr 8) and 0xFF
        val cb = notif.color and 0xFF
        val textArgb = ((alpha * 5 / 6) shl 24) or (cr shl 16) or (cg shl 8) or cb
        val lineWidth = font.width(line) * notif.scale

        graphics.pose().pushPose()
        graphics.pose().translate(
          scaledWidth.toFloat() / 2f - lineWidth / 2f, screenY, 0f
        )
        graphics.pose().scale(notif.scale, notif.scale, 1f)
        graphics.drawString(font, line, 0, 0, textArgb, true)
        graphics.pose().popPose()
      }

      pixelOffsetPrio += (lines.size + 1) * rowHeight
    }
    graphics.disableScissor()
    RenderSystem.disableBlend()
  }
}